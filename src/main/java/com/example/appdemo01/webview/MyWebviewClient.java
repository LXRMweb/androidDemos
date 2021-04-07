package com.example.appdemo01.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appdemo01.R;

/** WebViewClient类 * 作用：处理各种通知 & 请求事件
 * */
public class MyWebviewClient extends WebViewClient {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private ProgressBar progressBar;

    public MyWebviewClient(Context context){
        this.context = context;
    }

    public MyWebviewClient(Context context, ProgressBar progressBar){
        this.context = context;
        this.progressBar = progressBar;
    }

    /** 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
     * */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    /**开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
     * */
    @Override
    public void  onPageStarted(WebView view, String url, Bitmap favicon) {
        //设定加载开始的操作
        // 展示loading 动画
        if(progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        // 注册native方法，使得JS中可以调用 android native method
        view.addJavascriptInterface(new NativeInterfaceExposedToJavascript(context), "Android");
        Toast.makeText(context,"注册本地方法至JavaScript",Toast.LENGTH_LONG).show();
        Log.v(TAG,"注册本地方法至JavaScript:name-Android");
    }


    /**在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
     * */
    @Override
    public void onPageFinished(WebView view, String url) {
        //设定加载结束的操作
//        progressBar.setVisibility(View.INVISIBLE);
        if(progressBar!=null){
            progressBar.setVisibility(View.GONE);
        }
        testNativeCallJS(view);
    }
    // 测试：安卓原生代码中调用JS的函数
    private void testNativeCallJS(WebView view){
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            view.loadUrl("javascript:nativeCallJs()");
        } else {
            view.evaluateJavascript("javascript:nativeCallJs()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    Toast.makeText(context,"javascript:nativeCallJs()@捕获JS函数返回值return="+value,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    /**重写此方法才能处理浏览器中的按键事件
     * */
//    @Override
//    public void shouldOverrideKeyEvent(WebView view, String url) {
//
//    }

    /**加载页面的服务器出现错误（比如404）时回调
     *      App里面使用webview控件的时候遇到了诸如404这类的错误的时候，若也显示浏览器里面的那种错误提示页面就显得很丑陋了，
     *      那么这个时候我们的app就需要加载一个本地的错误提示页面，即webview如何加载一个本地的页面
     *
     * */
    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.v(TAG,"errorCode:"+errorCode);
        switch(errorCode){
//            case :
//                view.loadUrl("file:///android_assets/error_handle.html");
//                break;
        }
        view.loadUrl(String.valueOf(R.string.error_page_pro));
        Log.i(TAG, "onReceivedError:"+failingUrl+","+description);
    }

    /**webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
     * */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        handler.proceed();    //表示等待证书响应
        // handler.cancel();      //表示挂起连接，为默认方式
        // handler.handleMessage(null);    //可做其他处理
    }

    /**
     * shouldInterceptRequest()：页面每一次请求资源之前都会调用这个方法（非UI线程调用）。
     *
     * onLoadResource()：页面加载资源时调用，每加载一个资源（比如图片）就调用一次。
     *
     * onReceivedError()：加载页面的服务器出现错误（比如404）时回调。
     *
     * onReceivedSslError()：重写此方法可以让webview处理https请求。
     *
     * doUpdateVisitedHistory()：更新历史记录。
     *
     * onFormResubmission()：应用程序重新请求网页数据。
     *
     * onReceivedHttpAuthRequest()：获取返回信息授权请求。
     *
     * onScaleChanged()：WebView发生缩放改变时调用。
     *
     * onUnhandledKeyEvent()：Key事件未被加载时调用。
     *
     * 作者：TokyoZ
     * 链接：https://www.jianshu.com/p/3e0136c9e748
     * 来源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。*/
}
