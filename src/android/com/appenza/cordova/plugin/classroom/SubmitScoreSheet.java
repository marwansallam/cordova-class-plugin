package com.lms.appenza.hotspotfiletransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.cordova.api.LOG;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitScoreSheet extends AppCompatActivity {
    public static final String LOG_TAG = "HOTSPOTMM";
    List<ScanResult> scanResults;
    WifiManager manager;
    WifiConfiguration conf;
    int netId;
    TextView sentTextView;
    Map<String, String> json = new HashMap<>();
    String currentTeacher, teacherMacAddress="test";
    Boolean foundTeacher= false;
    public static boolean isSending = true;
    String results ="{\"_type\":\"Quiz\",\"id\":308,\"name\":\"r2r23r32\",\"description\":\"32r23\",\"summary\":null,\"time_limit\":222222223,\"time_per_question\":null,\"enable_comments\":null,\"randomise\":true,\"show_correct_answer\":null,\"hide_points\":null,\"start_date\":\"2016-01-08T06:00:00.000+02:00\",\"end_date\":\"2016-01-09T06:00:00.000+02:00\",\"created_at\":\"2015-12-31T16:12:51.755+02:00\",\"updated_at\":\"2015-12-31T16:12:51.755+02:00\",\"state\":\"planned\",\"scoresheets_count\":0,\"grade_id\":2,\"subject_id\":2,\"is_offline&quo\n" +
            "t;:false,\"shares\":{\"groups\":0,\"users\":0,\"total_users\":0},\"grade\":{\"_type\":\"Grade\",\"id\":2,\"school_id\":1,\"name\":\"Grade 2\"},\"groups\":[],\"user\":{\"_type\":\"User\",\"id\":15,\"name\":\"teacher\",\"email\":\"teacher@appenza.com\",\"last_sign_in_at\":\"2016-01-10T16:25:36.425+02:00\",\"is_student\":false,\"is_teacher\":true,\"is_parent\":false,\"license_key\":\"3tehu9za1xm07klqnb\",\"avatar\":{\"small\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/015/small/avatar1449570185.jpeg?AWSAccessKeyId=AKIAIZ7RSCTUJMEVSZCQ&Expires=1452444189&Signature=PutdLuzAXiXWir3mZu4FiWwwJmw%3D\",\"original\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/015/original/avatar1449570185.jpeg?AWSAccessKeyId=AKIAIZ7RSCTUJMEVSZCQ&Expires=1452444189&Signature=sD38pJKxpV2DXOicjKuHmiEJndE%3D\"},\"school\":{\"_type\":\"School\",\"id\":1,\"organization_id\":1,\"name\":\"New School\",\"description\":\"\"},\"role\":{\"_type\":\"Role\",\"id\":2,\"name\":\"Teacher\"},\"kids\":[{\"_type\":\"User\",\"id\":399,\"name\":\"Nada Magdi Shaker\",\"email\":\"nada.magdi@auroralms.com\",\"last_sign_in_at\":null,\"is_student\":true,\"is_teacher\":false,\"is_parent\":false,\"license_key\":\"zg0y13diqoxa79nlp2\",\"school\":{\"_type\":\"School\",\"id\":3,\"organization_id\":1,\"name\":\"El Resala School\",\"description\":null},\"role\":{\"_type\":\"Role\",\"id\":1,\"name\":\"Student\"},\"class\":{\"_type\":\"Stream\",\"id\":1,\"name\":\"Class A\",\"grade_id\":3}},{\"_type\":\"User\",\"id\":1,\"name\":\"Omar\",\"email\":\"owahab@gmail.com\",\"last_sign_in_at\":\"2016-01-10T16:45:51.890+02:00\",\"is_student\":true,\"is_teacher\":false,\"is_parent\":false,\"license_key\":\"zwy8i1bdt0hqfxepml\",\"avatar\":{\"small\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/001/small/avatar1441878432.jpeg?AWSAccessKeyId=AKIAIZ7RSCTUJMEVSZCQ&Expires=1452444189&Signature=FdQ%2BWVWkMPIVfOgd%2Bt1PO5dnNPs%3D\",\"original\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/001/original/avatar1441878432.jpeg?AWSAccessKeyId=AKIAIZ7RSCTUJMEVSZCQ&Expires=1452444189&Signature=ouII5%2BQWklnnjT59oZNsNQE%2BGME%3D\"},\"school\":{\"_type\":\"School\",\"id\":1,\"organization_id\":1,\"name\":\"New School\",\"description\":\"\"},\"grade\":{\"_type\":\"Grade\",\"id\":3,\"school_id\":1,\"name\":\"Grade 3\"},\"role\":{\"_type\":\"Role\",\"id\":1,\"name\":\"Student\"},\"class\":{\"_type\":\"Stream\",\"id\":21,\"name\":\"Class D\",\"grade_id\":4}},{\"_type\":\"User\",\"id\":432,\"name\":\"radwa zakaria\",\"email\":\"radwa.zakaria@appenza-studio.com\",\"last_sign_in_at\":\"2015-11-24T12:53:39.213+02:00\",\"is_student\":true,\"is_teacher\":false,\"is_parent\":false,\"license_key\":\"qof3-6c5f\",\"avatar\":{\"small\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/432/small/avatar1450347739.jpeg?AWSAccessKeyId=AKIAIZ7RSCTUJMEVSZCQ&Expires=1452444189&Signature=EugiGAWt08bs5%2BcWqmpvogiP9FU%3D\",\"original\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/432/original/avatar1450347739.jpeg?AWSAccessKeyId=AKIAIZ7RSCTUJMEVSZCQ&Expires=1452444189&Signature=ZJyfC5bduarc0%2B5zS4uHJALyFZM%3D\"},\"school\":{\"_type\":\"School\",\"id\":1,\"organization_id\":1,\"name\":\"New School\",\"description\":\"\"},\"grade\":{\"_type\":\"Grade\",\"id\":3,\"school_id\":1,\"name\":\"Grade 3\"},\"role\":{\"_type\":\"Role\",\"id\":1,\"name\":\"Student\"},\"class\":{\"_type\":\"Stream\",\"id\":3,\"name\":\"Class C\",\"grade_id\":3},\"kids\":[{\"_type\":\"User\",\"id\":384,\"name\":\"Ziad Ayman Jameel\",\"email\":\"ziad.ayman@auroralms.com\",\"last_sign_in_at\":null,\"is_student\":true,\"is_teacher\":false,\"is_parent\":false,\"license_key\":\"kmg2jqaz1o9r75eycn\",\"school\":{\"_type\":\"School\",\"id\":3,\"organization_id\":1,\"name\":\"El Resala School\",\"description\":null},\"role\":{\"_type\":\"Role\",\"id\":1,\"name\":\"Student\"},\"class\":{\"_type\":\"Stream\",\"id\":1,\"name\":\"Class A\",\"grade_id\":3}}]}]},\"subject\":{\"_type\":\"Subject\",\"id\":2,\"name\":\"Georgaphy\"}}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_score_sheet);
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        scanResults = new ArrayList<ScanResult>();
        conf = new WifiConfiguration();
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                scanResults = manager.getScanResults();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        sentTextView = (TextView)findViewById(R.id.sentTextView);
        scan();
    }

    //Show Available Networks Within Range

    public void scanBtn(View view) {
        scan();
    }
    private void scan() {
        if (!manager.isWifiEnabled())
            manager.setWifiEnabled(true);

        manager.startScan();
        Log.d(LOG_TAG, "size ---> " + scanResults.size());
        for (int sr = 0; sr < scanResults.size(); sr++)
            Log.d(LOG_TAG, sr + " -----> " + scanResults.get(sr).SSID + " : " + scanResults.get(sr).BSSID);
    }


    public void submitAnswersToTeacher(View view) {
        int CTR=0;

        while (true) {
            if(CTR>100)
                break;
            Log.d(LOG_TAG, CTR++ + "");
            manager.startScan();
            for (int i = 0; i < scanResults.size(); i++) {
                if (scanResults.get(i).SSID.contains("test")) {
                    foundTeacher = true;
                    Log.d(LOG_TAG, "Found Teacher : SSID ->" + scanResults.get(i).SSID + " BSSID ->" + scanResults.get(i).BSSID + String.valueOf(netId));
<<<<<<< HEAD
//                    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

=======
>>>>>>> 7790c096bb0d4f0e6d8f40e276cf454ce36858f9
                    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    conf.preSharedKey = "\"12345678\"";
                    conf.SSID = "\"" + scanResults.get(i).SSID + "\"";
                    conf.status = WifiConfiguration.Status.ENABLED;

                    // connect to and enable the connection
                    netId = manager.addNetwork(conf);
                    manager.saveConfiguration();
                    manager.disconnect();
                    manager.enableNetwork(netId, true);
                    manager.reconnect();

                    boolean isWifiConnected = false;
                    while (!isWifiConnected) {
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected()) {
                            isWifiConnected = true;
                        }
                    }
                    Log.d(LOG_TAG,"Connected to " + conf.SSID);

                    Log.d(LOG_TAG, "--------------------Sending Answers Started -----------------");
                    if (isWifiConnected)
                        for (Map.Entry entry : json.entrySet()) {
                            if (scanResults.get(i).BSSID.equals(entry.getValue()))
                                currentTeacher = entry.getKey().toString();
                        }
                    currentTeacher=scanResults.get(i).SSID;
                    Log.d(LOG_TAG, "Sending Answers To : " + currentTeacher);


                    new ClientTask().execute();
                    break;
                } else
                    Log.d(LOG_TAG, "Not A Teacher");
            }
            if(foundTeacher)
                break;
        }
    }


    private class ClientTask extends AsyncTask<Void, Void, Void> {
        private boolean isConnected = false;
        boolean fileCopied = false;
        Socket socket;

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Thread.sleep(1000);
                socket = new Socket("192.168.173.1", 9000);
<<<<<<< HEAD
                //android: 192.168.43.1
                //windows: 192.168.173.1
=======
>>>>>>> 7790c096bb0d4f0e6d8f40e276cf454ce36858f9
                Log.d(LOG_TAG, "Client: socket opened");
                isConnected = true;

                while (isConnected) {

                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(outputStream);
<<<<<<< HEAD

                    QuizModel model = new QuizModel();
                    String toSend = model.QuizString;
                    results=toSend;
                    int size  = toSend.length();
                    int start = 0;
                    int end = 100;
//                    while (true){
//                        if(end>=size) {
//                            end = size - 1;
//                            String sub = model.QuizString.substring(start,end);
//                            dos.writeUTF(sub);
//                            break;
//                        }
//                        String sub = model.QuizString.substring(start,end);
//                        end += 100;
//
//                        start = end;
//                        dos.writeUTF(sub);
//
//                    }
                    dos.flush();
                    toSend += "<EOF>";
                    dos.writeUTF(toSend);
                    Log.d(LOG_TAG, toSend.length() + "");

                    File file = new File(Environment.getExternalStorageDirectory() + "/HotspotSharedFiles/" + "senttext.text");
                    File dirs = new File(file.getParent());
                    if (!dirs.exists())
                        dirs.mkdirs();
                    if (file.createNewFile())
                        Log.d(LOG_TAG, "file created");
                    else
                        Log.d(LOG_TAG, "file not created");

                    //file.createNewFile();
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(toSend);
                    fileWriter.flush();
                    fileWriter.close();

                    //

                    Log.d(LOG_TAG, toSend + "<EOF>");
        //
        //            dos.flush();

                  //  dos.writeUTF("StudentName:Marwan");
//                    dos.writeUTF("Grade:10/15");
//                    dos.writeUTF("EndOfText");
                    Log.d(LOG_TAG, "Grade Submitted Successfully");
                    dos.close();
=======
                    dos.writeUTF("StartOfText");
                    dos.writeUTF("StudentName:Marwan");
                    dos.writeUTF("Grade:10/15");
                    dos.writeUTF("EndOfText");
                    Log.d(LOG_TAG,"Grade Submitted Successfully");
>>>>>>> 7790c096bb0d4f0e6d8f40e276cf454ce36858f9
                    outputStream.close();

                    Thread.sleep(10000);
                    socket.close();
                    isConnected = false;
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            manager.disableNetwork(netId);
            fileCopied = false;
            isSending = false;
            sentTextView.setText(results);
            File dir = new File(Environment.getExternalStorageDirectory() + "/HotspotSharedFiles");
            DeleteRecursive(dir);
        }


        void DeleteRecursive(File fileOrDirectory) {

            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    DeleteRecursive(child);

            fileOrDirectory.delete();

        }
    }

}
