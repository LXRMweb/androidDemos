package com.example.appdemo01.webview;

import android.content.Context;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**WebChromeClient作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
 * */
public class MyWebChromeClient extends WebChromeClient {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private ProgressBar progressBar;


    public MyWebChromeClient(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    public MyWebChromeClient(Context context) {
        this.context = context;
    }

    /**获取网页的加载进度并展示
     * */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress < 100) {
            if(progressBar != null){
                progressBar.setProgress(newProgress);
            }
        } else if(newProgress == 100){

        }
    }

    /**与JS进行交互，拦截JS的alert弹出框
     * */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

//        message:String 是js中alert弹出框展示的内容
//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle("JsAlert")
//                .setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                })
//                .setCancelable(false)
//                .show();

        return super.onJsAlert(view, url, message, result);
    }

    /**拦截JS的确认框*/
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle("JsConfirm")
//                .setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                })
//                .setCancelable(false)
//                .show();
        // 返回布尔值：判断点击时确认还是取消
        // true表示点击了确认；false表示点击了取消；
        return super.onJsConfirm(view, url, message, result);
    }

    /**拦截JS的输入框，支持用户输入内容*/
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//        final EditText et = new EditText(MainActivity.this);
//        et.setText(defaultValue);
//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle(message)
//                .setView(et)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm(et.getText().toString());
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                })
//                .setCancelable(false)
//                .show();
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
