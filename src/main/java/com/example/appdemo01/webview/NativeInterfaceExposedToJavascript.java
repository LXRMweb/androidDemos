package com.example.appdemo01.webview;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author lxrm
 * @date
 */
public class NativeInterfaceExposedToJavascript {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    /** Instantiate the interface and set the context */
    NativeInterfaceExposedToJavascript(Context c) {
        mContext = c;
    }

    //    =============== JavaScript中调用android native函数 start =====================
    /** Show a toast from the web page
     *
     * Caution: If you've set your targetSdkVersion to 17 or higher,
     * you must add the @JavascriptInterface annotation to any method that you want available to your JavaScript,
     * and the method must be public. If you do not provide the annotation,
     * the method is not accessible by your web page when running on Android 4.2 or higher.
     * */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public void postHttp(String url, String param) {
        Toast.makeText(mContext, "模拟请求：" + url, Toast.LENGTH_SHORT).show();
        String rst = "模拟请求结果：{code:000000,data:{null}}";
        Toast.makeText(mContext,rst,Toast.LENGTH_LONG).show();
//        WebView myWebview = WebviewUtils.getWebViewInstance(mContext);
    }
    @JavascriptInterface
    public void postHttpv1(String url, String param) {
        Toast.makeText(mContext, "模拟请求：" + url, Toast.LENGTH_SHORT).show();
        String rst = "模拟请求结果：{code:000000,data:{null}}";
        WebView myWebview = WebviewUtils.getWebViewInstance(mContext);
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
//        if (version < 18) {
//            myWebview.loadUrl("javascript:nativeCallJs()");
//        } else {
//            myWebview.evaluateJavascript("javascript:nativeCallJs()", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    //此处为 js 返回的结果
//                    Toast.makeText(mContext,"javascript:nativeCallJs()@return="+value,Toast.LENGTH_LONG).show();
//                    Log.v(TAG,"javascript:nativeCallJs()？)@return="+value);
//                }
//            });
//        }
    }
    //    =============== JavaScript中调用android native函数 end =====================
}
