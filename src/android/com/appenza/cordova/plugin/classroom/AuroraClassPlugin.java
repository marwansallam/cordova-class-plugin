package com.appenza.classroom;

import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class AuroraClassPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
       Log.d("Aurora Class", "Init Aurora Class Plugin");
        super.initialize(cordova, webView);
      
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("get_launch_params")) {
	}
        return super.execute(action, args, callbackContext);
    }
}

