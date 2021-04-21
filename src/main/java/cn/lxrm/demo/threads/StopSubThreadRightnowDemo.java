package cn.lxrm.demo.threads;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * README: 本用例演示如何立即停止一个子线程
 * 使用AsyncTask.cancel()是无法直接停止一个子线程的，执行完cancel()之后，子线程仍然会继续执行
 * 那么该如何立即停止一个子线程呢？
 * 答：有许多中方法可以实现立即停止一个子线程的需求，如：标记法、异常法...
 * 本例展示如何使用标记法停止子线程任务，编程步骤如下：
 * step1，标记线程销毁
 *       if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
 *           asyncTask.cancel(true);
 *       }
 *       详情参见本例中的“step1-code实例”标签处的代码
 * step2，执行耗时任务的过程中时常检查线程销毁标志，一旦线程销毁标志置成了true，立即退出耗时任务（return退出AsnycTask#doInBackground()即可终止耗时任务）
 *      见本例中的“step2-code实例”标签处的代码
 *
 * @author created by Meiyu Chen at 2021-4-21 16:23, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class StopSubThreadRightnowDemo {
    private static int progress = 0;
    private static final String TAG = "StopSubThreadDemo";
    private static AsyncTask<Void, Void, Void> asyncTask;

    public static void main(String[] args) {
        // 创建一个新的子线程实例
        asyncTask = creatOneNewThread();
        byte[] input = new byte[100];
        while (true) {
            System.out.println("请输入对应的命令：1-开启线程；2-结束线程");
            try {
                System.in.read(input);
                switch (new Integer(input.toString())) {
                    case 1:
                        if (asyncTask == null) {
                            Log.d(TAG, "main: 未获取到子线程实例");
                            break;
                        } else if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                            Log.d(TAG, "main: 该子线程已经开启了，请不要重复开启呦");
                            break;
                        } else {
                            // 开启该线程
                            asyncTask.execute();
                        }
                        break;
                    case 2:
                        // step1-code实例
                        if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                            /* step1, 先将该子线程标记为销毁
                                    (注意，只是标记为销毁并不能立即停止子线程哦，
                                    你还需要在子线程asyncTask的doInBackground()方法中读取线程销毁标记，并在合适的时机根据这个 线程销毁标记结束asyncTask的doInBackground()
                                    你可以使用return语句结束掉asyncTask的doInBackground()
                                    结束掉asyncTask的doInBackground()，才算结束掉该子线程)*/
                            asyncTask.cancel(true);
                        }
                        break;
                    default:
                        Log.d(TAG, "main: 暂不支持该命令");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: 创建一个新的子线程
     *
     * @author created by Meiyu Chen at 2021-4-21 16:44, v1.0
     */
    private static AsyncTask<Void, Void, Void> creatOneNewThread() {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    // step2-code实例: 子线程耗时任务执行过程中，读取子线程销毁标志，一旦该子线程被销毁，则停止耗时任务的执行，退出doInBackground()函数，才算真正结束掉该子线程
                    if (asyncTask.isCancelled()) {
                        return null;
                    }
                    try {
                        // 模拟耗时操作
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress += 1;
                    Log.d(TAG, "doInBackground: progress = " + progress);
                }
            }
        };
    }
}
