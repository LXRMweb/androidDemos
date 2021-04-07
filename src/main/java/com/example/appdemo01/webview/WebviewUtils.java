package com.example.appdemo01.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.appdemo01.activities.DisplayWebviewActivity;
import com.example.appdemo01.activities.DisplayWebviewActivityV1;

import org.apache.commons.lang3.ArrayUtils;

public class WebviewUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static WebView myWebView;
    public static final String EXTRA_URL = "com.example.appdemo01.webview.WebviewUtils.URL";

    /*获取配置好的WebView实例
     * */
    public static WebView getWebViewInstance(Context context) {
        myWebView = new WebView(context);
        enableMyWebviewSettings(myWebView);
        myWebView.setWebViewClient(new MyWebviewClient(context));
        myWebView.setWebChromeClient(new MyWebChromeClient(context));
        return myWebView;
    }

    /*配置WebView
     *   1. 使其支持JavaScript（默认不支持JavaScript，不能打开包含JS代码的web网页）
     * */
    public static void enableMyWebviewSettings(WebView myWebView) {
        MyWebviewSettings.enableMySettings(myWebView);
    }

    //    =============== APP中展示web网页 start =====================

    /**
     * 展示web页面
     * 方法一，使用xml的<Webview>在APP中展示网页
     */
    public static void showWebviewWithLayoutXML(Context context, String url) {
        Intent intent = new Intent(context, DisplayWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    /**
     * 展示web页面
     * 方法一，使用xml的<Webview>在APP中展示网页
     */
    public static void showWebviewWithLayoutXMLV1(Context context, String url) {
        Intent intent = new Intent(context, DisplayWebviewActivityV1.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    /**
     * 展示web页面
     * 方法二，Java中使用Webview展示网页
     */
    public static void showWebviewPageWithJava(Context context, String url) {
        myWebView = getWebViewInstance(context);
        Activity activity = (Activity) context;
        activity.setContentView(myWebView);
        myWebView.loadUrl(url);
    }

    /**
     * 展示web页面
     * 方法三，Java中使用Webview展示html code
     */
    public static void showWebviewDataWithJava(Context context, String unencodedHtml) {
        myWebView = getWebViewInstance(context);
        Activity activity = (Activity) context;
        activity.setContentView(myWebView);
        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                Base64.NO_PADDING);
        myWebView.loadData(encodedHtml, "text/html", "base64");
    }
    //    =============== APP中展示web网页 end =====================

    //    =============== android native调用JavaScript函数 start =====================
    public static void callJSMethod(Context context, String jsFuncName, String[] args) {
        myWebView = getWebViewInstance(context);
//        myWebView.loadUrl("javascript:" + jsFuncName + "('" + ArrayUtils.toString(args) + "')");
        myWebView.loadUrl("javascript:" + jsFuncName + "('" + "')");
    }
    //    =============== android native调用JavaScript函数 end =====================

    //    =============== 将android native函数暴露给JavaScript，使得JavaScript中可以调用android native函数 start =====================
    // 在MyWebviewClient的生命周期函数中进行注册（在其onPageStarted()函数中注册）
//    public static void exposeAndroidNativeFuncToJS(Context context){
//        myWebView = getWebViewInstance(context);
//        myWebView.addJavascriptInterface(new NativeInterfaceExposedToJavascript(context), "Android");
//        Toast.makeText(context,"注册本地方法至JavaScript",Toast.LENGTH_SHORT);
//        Log.v(TAG,"注册本地方法至JavaScript");
//    }
    //    =============== 将android native函数暴露给JavaScript，使得JavaScript中可以调用android native函数 end =====================
}
