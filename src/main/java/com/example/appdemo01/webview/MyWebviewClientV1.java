package com.example.appdemo01.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

public class MyWebviewClientV1 extends WebViewClient {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private AnimatedCircleLoadingView animatedCircleLoadingView;

    public MyWebviewClientV1(Context context){
        this.context = context;
    }
    public MyWebviewClientV1(Context context, AnimatedCircleLoadingView animatedCircleLoadingView){
        this.context = context;
        this.animatedCircleLoadingView = animatedCircleLoadingView;
        adaptLoadingDialogDimen();
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
        startLoading();
//        startPercentMockThread();
    }

    /**在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
     * */
    @Override
    public void onPageFinished(WebView view, String url) {
        //设定加载结束的操作
        stopOk();
        stopLoading();
    }

    //    ----------------- loading 动画 start -----------------
    // 改变dialog动画尺寸，使其匹配设备屏幕尺寸
    private void adaptLoadingDialogDimen(){
        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // 屏幕宽度（像素）
        int height = metrics.heightPixels;
        float density = metrics.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metrics.densityDpi; // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width/density); // 屏幕宽度(dp)
        int screenHeight = (int)(height/density); // 屏幕高度(dp)

        //定义布局参数
//        AnimatedCircleLoadingView.LayoutParams layoutParams = new AnimatedCircleLoadingView.LayoutParams(AnimatedCircleLoadingView.LayoutParams.WRAP_CONTENT, AnimatedCircleLoadingView.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams layoutParams = animatedCircleLoadingView.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = screenHeight;
//        layoutParams.width = width;
//        layoutParams.height = height;
//        layoutParams.width = (int) (width * 0.42);
//        layoutParams.height = (int) (height * 0.65);
//        layoutParams.leftMargin = (int) (width * 0.1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setLayoutParams(layoutParams);
            }
        });
        String msg = "设置参数|尺寸："+width+","+height+";dp-"+screenWidth+","+screenHeight;
        String msg2 = "实际|尺寸："+animatedCircleLoadingView.getWidth()+","+animatedCircleLoadingView.getHeight();
        Log.v(TAG,msg);
        Log.v(TAG,msg2);
        animatedCircleLoadingView.requestLayout();
    }
    // 展示loading动画
    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }
    private void startIndeterminate() {
        animatedCircleLoadingView.startIndeterminate();
    }
    private void stopLoading() {
    }

    // 模拟：更新加载进度
//    private void startPercentMockThread() {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1500);
//                    for (int i = 0; i <= 100; i++) {
//                        Thread.sleep(65);
//                        changePercent(i);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }

    // 更新加载进度
    private void changePercent(final int percent) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    public void resetLoading() {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }

    public void stopOk() {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.stopOk();
            }
        });
    }

    public void stopFailure() {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.stopFailure();
            }
        });
    }

    //    ----------------- loading 动画 end -----------------
}
