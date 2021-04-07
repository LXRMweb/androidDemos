package com.example.appdemo01.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdemo01.R;
import com.example.appdemo01.webview.MyWebChromeClient;
import com.example.appdemo01.webview.MyWebviewClient;
import com.example.appdemo01.webview.WebviewUtils;

import org.apache.commons.lang3.ArrayUtils;

public class DisplayWebviewActivity extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_webview);
//        AnimatedCircleLoadingView animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        //代码中控制显隐藏
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        myWebView = (WebView) findViewById(R.id.webview);
        WebviewUtils.enableMyWebviewSettings(myWebView);
        // loading动画
//        myWebView.setWebViewClient(new MyWebviewClient(this,animatedCircleLoadingView));
        myWebView.setWebViewClient(new MyWebviewClient(this,mProgressBar));
        myWebView.setWebChromeClient(new MyWebChromeClient(this, mProgressBar));
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String url = intent.getStringExtra(WebviewUtils.EXTRA_URL);

        myWebView.loadUrl(url);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //激活WebView为活跃状态，能正常执行网页的响应
        myWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        myWebView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。
        * */
        if (myWebView != null) {
            myWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            // 清除当前webview访问的历史记录
            myWebView.clearHistory();
//          //  清除网页访问留下的缓存（由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序）
//            myWebView.clearCache(true);
            //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
//        myWebView.clearFormData();
            /*销毁Webview
             *  在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
             *  但是注意：webview调用destory时,webview仍绑定在Activity上
             *  这是由于自定义webview构建时传入了该Activity的context对象
             *  因此需要先从父容器中移除webview,然后再销毁webview:
             */
            ViewGroup parent = (ViewGroup) myWebView.getParent();
            parent.removeView(myWebView);
            myWebView.destroy();
            myWebView = null;
        }
    } // end method

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 不做任何处理前提下 ，浏览网页时点击系统的“Back”键,整个 Browser 会调用 finish()而结束自身
        // 做出如下处理后：点击返回后，是网页回退而不是退出浏览器
        boolean flag = myWebView.canGoBack();
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            Toast.makeText(this,"goback",Toast.LENGTH_LONG).show();
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}