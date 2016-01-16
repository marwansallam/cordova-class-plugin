package com.appenza.classroom;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marwansallam on 1/14/16.
 */
public class ConnectionManager extends Service {
    static String LOG_TAG = "HOTSPOTMM";
    static List<ScanResult> scanResults;
    static WifiManager manager;
    static WifiConfiguration conf;
    static WifiInfo wifiInfo;
    int netId;

    public ConnectionManager() {
    }

    public static boolean connectBySSID(String SSID) {
        return false;
    }

    public static boolean connectByMacAddress(String MacAddress) {
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        conf = new WifiConfiguration();
        wifiInfo = manager.getConnectionInfo();
        scanResults = new ArrayList<ScanResult>();
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                scanResults = manager.getScanResults();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }


    public static boolean connectBySSID(String SSID){
        return false;
    }

    public static boolean connectByMacAddress(String MacAddress){
        return false;
    }

    public static void scan() {
        if (!manager.isWifiEnabled())
            manager.setWifiEnabled(true);
        manager.startScan();
        Log.d(LOG_TAG, "size ---> " + scanResults.size());
        for (int sr = 0; sr < scanResults.size(); sr++)
            Log.d(LOG_TAG, sr + " -----> " + scanResults.get(sr).SSID + " : " + scanResults.get(sr).BSSID);
    }

    public  boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
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
    public IBinder onBind(Intent intent) {
        return null;
    }
}
