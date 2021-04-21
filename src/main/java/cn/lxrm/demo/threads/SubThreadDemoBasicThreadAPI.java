package cn.lxrm.demo.threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * README: 安卓创建并使用子线程demo-使用最底层的线程API（Thread && Runnable）
 *
 * @author created by Meiyu Chen at 2021-4-20 16:48, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class SubThreadDemoBasicThreadAPI {
    public static final int UPDATE_TEXT = 1;
    private final String TAG = this.getClass().getSimpleName();
    private Handler handler;

    /** Description: 接收UI线程传过来的textView对象，用于在子线程中更新它（不能直接更新，必须借助handler来更新）
     * @author created by Meiyu Chen at 2021-4-20 17:44, v1.0
     * @param textView
     */
    public SubThreadDemoBasicThreadAPI(TextView textView) {
        this.handler = new Handler(){
            @Override
            // handleMessage是在主线程中运行的
            public void handleMessage(@NonNull Message msg) {
                // 接收消息，更新UI
                if(msg.what == UPDATE_TEXT){
                    Bundle data = msg.getData();
                    textView.setText(data.getString("textViewContent"));
                }
            }
        };
    }

    /** Description: 使用java最原始的线程相关API创建子线程，并在子线程中执行耗时操作
     * @author created by Meiyu Chen at 2021-4-20 16:53, v1.0
     */
    public void subThreadWithprimitiveAPI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "subThreadWithprimitiveAPI: 耗时任务完成");
                updateUITextView("subThreadWithprimitiveAPI；子线程调用成功");
            }
        }).start();
    }

    /** Description: 子线程中更新UI
     *      android中，子线程不能直接修改UI，view只能被创它们的线程（即主线程）修改
     *      所以子线程中要借助Handler来实现
     * @author created by Meiyu Chen at 2021-4-20 17:18, v1.0
     */
    private void updateUITextView(String s) {
        /* 不能直接这么调用，不能在子线程中直接修改UI，否则会出现运行时错误
        *   Process: com.example.appdemo01, PID: 29188
            android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
            * */
//        this.textView.setText(s);

        // 子线程中借助于Handler实现UI操作，更新UI
        Message message = new Message();
        message.what = UPDATE_TEXT;
        Bundle bundle = new Bundle();
        bundle.putString("textViewContent",s);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
