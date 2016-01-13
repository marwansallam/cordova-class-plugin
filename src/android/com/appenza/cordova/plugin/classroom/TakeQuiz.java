package com.lms.appenza.hotspotfiletransfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TakeQuiz extends AppCompatActivity {

    /**CONSTANTS**/

    private static final String TAG = MainActivity.class.getCanonicalName();

    /**MEMBERS**/

    private WebView mWebView;

    private ProgressBar mLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);
        mWebView = (WebView)findViewById(R.id.wvPortal);

        mLoading = (ProgressBar) findViewById(R.id.pbLoading);

        //mWebView.loadUrl("http://www.google.com");



        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("file:///android_asset/app/index.html");


    }

    private String readAssetFile(String fileName) throws IOException {
        StringBuilder buffer = new StringBuilder();
        InputStream fileInputStream = getAssets().open(fileName);
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
        String str;

        while ((str=bufferReader.readLine()) != null) {
            buffer.append(str);
        }
        fileInputStream.close();

        return buffer.toString();
    }
}
