package com.appenza.classroom;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class CollectAnswers extends Activity {
    static TextView studentsSubmittedAnswersTxt, studentsReceivedQuizTxt;
    String QuizName, LOG_TAG = "HOTSPOTMM";
    WifiManager manager;
    ServerSocket serverSocket;
    File quizFile;
    FileWriter fileWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_answers);

        studentsReceivedQuizTxt = (TextView)findViewById(R.id.srqShowTxt);
        studentsSubmittedAnswersTxt = (TextView)findViewById(R.id.ssaShowTxt);
        WifiConfiguration netConfig = new WifiConfiguration();
        netConfig.SSID ="test";
        Log.d(LOG_TAG, netConfig.SSID + "--- this is SSID");
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

        setWifiApEnabled(netConfig, true);
        QuizName = "Quiz1.txt";
        quizFile = new File(Environment.getExternalStorageDirectory() + "/QuizzesAnswers/" + QuizName);
        try {
            File dirs = new File(quizFile.getParent());
            if (!dirs.exists())
                dirs.mkdirs();
            if (quizFile.createNewFile())
                Log.d(LOG_TAG, "file created");
            else
                Log.d(LOG_TAG, "file not created");
            fileWriter = new FileWriter(quizFile);
            fileWriter.append("This is a test :) " + System.getProperty("line.separator"));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new FileServerTask().execute();
    }

    public boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
        try {
            if (enabled) { // disable WiFi in any case
                manager.setWifiEnabled(false);
            }
            Method method = manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (Boolean) method.invoke(manager, wifiConfig, enabled);
        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
            return false;
        }
    }

    public class FileServerTask extends AsyncTask<Void, Void, File> {

        public static final String LOG_TAG = "HOTSPOTMM server";

        @Override
        protected File doInBackground(Void... params) {
            int CTR = 0;
            try {
                serverSocket = new ServerSocket(8000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {

                    Log.d(LOG_TAG, "Server: socket opened" + CTR++);
                    Socket client;
                    client = serverSocket.accept();
                    Server server = new Server(client);
                    server.start();
                    // client.close();
                    // serverSocket.close();
                    // Log.d(LOG_TAG, "Server Conn closed");
                    //  return quizFile;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
                //return null;
            }
        }

        @Override
        protected void onPostExecute(File f) {
        }

        public void updateUI(String studentName, String studentGrade) {
            AuroraClassActivity.waitingForQuiz.setVisibility(View.INVISIBLE);
            studentsSubmittedAnswersTxt.append(studentName + " has submitted the answers successfully and his grade is : " + studentGrade);
            Toast.makeText(getApplicationContext(), "Received Answers from : " + studentName, Toast.LENGTH_SHORT).show();
        }


    }
}

