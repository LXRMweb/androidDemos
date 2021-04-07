package com.example.appdemo01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdemo01.R;
import com.example.appdemo01.webview.MyWebviewClient;
import com.example.appdemo01.webview.MyWebviewClientV1;
import com.example.appdemo01.webview.WebviewUtils;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

public class DisplayWebviewActivityV1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_webview_v1);
        AnimatedCircleLoadingView animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        WebView myWebView = (WebView) findViewById(R.id.webview_v1);
        WebviewUtils.enableMyWebviewSettings(myWebView);
        // loading动画
        myWebView.setWebViewClient(new MyWebviewClientV1(this, animatedCircleLoadingView));
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String url = intent.getStringExtra(WebviewUtils.EXTRA_URL);

        myWebView.loadUrl(url);
    }

}