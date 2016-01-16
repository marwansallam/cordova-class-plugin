package com.appenza.classroom;

import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by marwansallam on 12/27/15.
 * This Thread represents a separate connection with each client to the server
 * and handles file transfer between them
 */
public class Server extends Thread {
    String LOG_TAG = "HOTSPOTMM";
    String studentName, studentGrade;
    boolean answerReceived = false;
    private Socket socket = null;

    public Server(Socket socket) {
        super("Server");
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader st = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String temp;
            String studentAnswer = "";
            if (st.readLine().contains("StartOfText")) {
                while (true) {
                    temp = st.readLine();
                    Log.d(LOG_TAG, temp);
                    if (temp.contains("StudentName")) {
                        studentName = (temp.split(":"))[1];
                    }
                    if (temp.contains("Grade")) {
                        studentGrade = (temp.split(":"))[1];
                    }
                    if (temp.contains("EndOfText")) {
                        answerReceived = true;
                        break;
                    }
                    studentAnswer += temp;
                }
                Log.d(LOG_TAG, "Student Answer : " + studentAnswer);
//              fileWriter.append("Student Answer <" + studentAnswer + ">" + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        AuroraClassActivity.waitingForQuiz.setVisibility(View.INVISIBLE);
        if (answerReceived) {
            // CollectAnswers.FileServerTask.updateUI(studentName,studentGrade);
            // Toast.makeText(getA, "Received Answers from : " +studentName, Toast.LENGTH_SHORT).show();
        }
    }
}
