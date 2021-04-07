package com.example.appdemo01.webview;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MyWebviewSettings {
    public static void enableMySettings(WebView myWebView){
        WebSettings webSettings = myWebView.getSettings();
        // JavaScript is disabled in a WebView by default. You can enable it through the WebSettings attached to your WebView
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setSupportMultipleWindows(true);

//        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//        //缩放操作
//        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100

//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

//        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
//        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
//        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        // 5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        /* 缓存
         *      其中，WebView设置缓存，当加载 html 页面时，WebView会在/data/data/package/下生成 database 与 cache 两个文件夹
         *      请求的URL记录保存在WebViewCache.db，而URL的内容是保存在WebViewCache文件夹下。
         * 缓存模式如下：
         *      LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         *      LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         *      LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         *      LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
//         //优先使用缓存:
//         webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//         //不使用缓存:
//         webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // todo
//        if (NetStatusUtil.isConnected(getApplicationContext())) {
//            // 有网状态
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
//        } else {
//            // 没网，则从本地获取，即离线加载
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
//        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
//        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
//        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        // 注意：每个 Application 只调用一次 WebSettings.setAppCachePath()，WebSettings.setAppCacheMaxSize()
//        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
//        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        //其他操作
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setGeolocationEnabled(true);//允许网页执行定位操作
//        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");//设置User-Agent


        //不允许访问本地文件（不影响assets和resources资源的加载）
//        webSettings.setAllowFileAccess(false);
//        webSettings.setAllowFileAccessFromFileURLs(false);
//        webSettings.setAllowUniversalAccessFromFileURLs(false);
    }
}
