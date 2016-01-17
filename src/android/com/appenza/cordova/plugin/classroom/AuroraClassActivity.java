package com.appenza.classroom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AuroraClassActivity extends Activity {
    public static final String LOG_TAG = "HOTSPOTMM";
    public static final int CHOOSE_FILE_REQUEST_CODE = 10;
    public static Uri uri;
    static String studentSSID;
    static Button receiveBtn;
    static TextView waitingForQuiz;
    static String teacherMacAddress;
    boolean locked = false;
    ConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aurora_class);

        Intent connectionManagerIntent = new Intent(this, ConnectionManager.class);
        startService(connectionManagerIntent);

        studentSSID = "samsungStudent";
        receiveBtn = (Button) findViewById(R.id.receiveBtn);
        waitingForQuiz = (TextView) findViewById(R.id.waitForQuizTxt);
        waitingForQuiz.setVisibility(View.INVISIBLE);
//      teacherMacAddress = connectionManager.wifiInfo.getMacAddress();
        Log.d(LOG_TAG, "mac address 1: " + teacherMacAddress);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CHOOSE_FILE_REQUEST_CODE) {
            uri = data.getData();
            Log.d(LOG_TAG, "Uri: " + uri.toString());
            Log.d(LOG_TAG, "Uri: " + data.toString());
            startActivity(new Intent(this, StudentList.class));
        }
    }

    public void send(View view) {
        if (!connectionManager.manager.isWifiEnabled()) {
            connectionManager.setWifiApEnabled(null, false);
            connectionManager.manager.setWifiEnabled(true);
        }
        chooseFile();
    }

    public void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
    }

    public void receive(View view) {
        teacherMacAddress = "00:00:00:00:00:00";
        Intent intent = new Intent(this, ReceiveFile.class);

        if(!receiveBtn.getText().toString().contains("Stop")) {
            //startActivity(new Intent(this, Receiving.class));
            receiveBtn.setText("Stop Receiving");
            startService(intent);
            waitingForQuiz.setVisibility(View.VISIBLE);
        } else {
            waitingForQuiz.setVisibility(View.INVISIBLE);
            stopService(intent);
            receiveBtn.setText("Start Receiving");
            waitingForQuiz.setText("Waiting for Quiz ...");
        }
    }


    public void goToSubmit(View view) {
        startActivity(new Intent(this, SubmitScoreSheet.class));
    }

    public void goToTakeQuiz(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void lockApp(View view) {
        if (locked) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}