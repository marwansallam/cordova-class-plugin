package com.appenza.classroom;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class ReceiveFile extends Service {
    String LOG_TAG = "HOTSPOTMM";
    WifiManager manager;
    ServerSocket serverSocket = null;
    boolean fileReceived = false;
    public ReceiveFile() {
    }

    public static void unzip(String source, String destination, String password) {
        try {
            ZipFile zipFile = new ZipFile(source);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration netConfig = new WifiConfiguration();
        netConfig.SSID = AuroraClassActivity.studentSSID;
        Log.d(LOG_TAG, netConfig.SSID + "--- this is SSID");
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        setWifiApEnabled(netConfig, true);
        new FileServerTask().execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

    @Override
    public boolean stopService(Intent name) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setWifiApEnabled(null, false);
        Log.d(LOG_TAG, "Stoping service");
        this.stopSelf();
        this.onDestroy();
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setWifiApEnabled(null, false);
        Log.d(LOG_TAG, "Stoping service");
        this.stopSelf();
    }

    private class FileServerTask extends AsyncTask<Void, Void, File> {

        public static final String LOG_TAG = "HOTSPOTMM server";

        @Override
        protected File doInBackground(Void... params) {
            try {
                serverSocket = new ServerSocket(9000);
                Log.d(LOG_TAG, "Server: socket opened");
                Socket client = serverSocket.accept();
                Log.d(LOG_TAG, manager.getDhcpInfo().toString());
                Log.d(LOG_TAG, "Manager All Info : " + manager.toString());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                BufferedInputStream inputStream = new BufferedInputStream(client.getInputStream());
                byte[] dataBuffer = new byte[1000];
                inputStream.read(dataBuffer);
                String header = new String(dataBuffer, Charset.forName("UTF-8"));
                String filename = header.substring(0, header.indexOf("</FILENAME>"));
                int size = Integer.parseInt(header.split("<Size>")[1]);
                Log.d(LOG_TAG, size + " bytes will be sent to you ");
                Log.d(LOG_TAG, header + " is the file name received");
                Log.d(LOG_TAG, "Teacher Mac Address: " + AuroraClassActivity.teacherMacAddress);
                File file = new File(Environment.getExternalStorageDirectory() + "/HotspotSharedFiles/ " + filename);
                Log.d(LOG_TAG, "Filename is : " + filename);
                File dirs = new File(file.getParent());
                if (!dirs.exists())
                    dirs.mkdirs();
                if (file.createNewFile())
                    Log.d(LOG_TAG, "file created");
                else
                    Log.d(LOG_TAG, "file not created");
                FileOutputStream outputStream = new FileOutputStream(file);
                if (receiveFile(inputStream, outputStream, size)) {
                    Log.d(LOG_TAG, "File received");
                    String source = file.getPath();
                    String destination = file.getParent() + "/ZipFiles/";
                    String password = "password";
                    unzip(source, destination, password);
                    dos.flush();
                } else {
                    Log.d(LOG_TAG, "File not received");
                    dos.writeUTF("<FTR>");
                    dos.flush();
                }
                client.close();
                serverSocket.close();
                Log.d(LOG_TAG, "Server Conn closed");
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, e.toString());
            }
            return null;
        }

        private boolean receiveFile(BufferedInputStream inputStream, OutputStream out, int size) {
            int totalLen = 0;
            byte[] buf = new byte[32768];
            int len;
            try {
                while ((len = inputStream.read(buf)) != -1) {
                    out.write(buf, 0, len);
                    totalLen += len;
                    if (totalLen >= size)
                        break;
                    String v = new String(buf, Charset.forName("UTF-8"));
                    Log.d(LOG_TAG, totalLen + " bytes received");
                    Log.d(LOG_TAG, v);
                }
                out.flush();
                out.close();
                inputStream.close();

            } catch (IOException e) {
                Log.d(LOG_TAG, e.toString());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(File f) {
            AuroraClassActivity.waitingForQuiz.setVisibility(View.INVISIBLE);
            if (fileReceived) {
                Toast.makeText(getApplicationContext(), "Teacher MAC : " + AuroraClassActivity.teacherMacAddress, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Quiz Received", Toast.LENGTH_SHORT).show();
            }
            AuroraClassActivity.receiveBtn.setText("Start Receiving");
            if (f != null) {
                Log.d(LOG_TAG, this.getStatus().toString() + "---- Stopping Service !!!!!");
                setWifiApEnabled(null, false);
                stopSelf();

            }
        }

    }
}
