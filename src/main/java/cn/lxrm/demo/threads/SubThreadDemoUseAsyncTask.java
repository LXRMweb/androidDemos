package cn.lxrm.demo.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * README: 创建并使用子线程（使用进一步封装的接口：AsyncTask）
 *      AsyncTask是对底层线程相关API（Thread/Runnable...的进一步封装）
 * 编程步骤：
 *      step1，编写一个继承AsyncTask的子类
 *      step2，继承AsyncTask时，可以为AsyncTask指定三个泛型参数：
 *          Params: 在执行AsyncTask时需要传入的参数，可用于在后台任务中使用
 *              如，本例中为Params赋值为Void,说明执行该子线程任务时不需要传入任何参数
 *          Progress: 后台任务执行时，如果需要在界面上显示当前的进度，则使用这里指定的泛型作为进度单位
 *              如：本例中为Progress赋值为Integer，说明你需要使用Integer类型的变量表示进度
 *          Result: 当任务执行完毕后，如果需要对结果进行返回，则使用这里指定的泛型作为返回值类型
 *              如：本例中指定返回值类型为Boolean型
*       step3, 实现AsyncTask中定义的一些列抽象方法
 *          onPreExecute、doInBackground、onProgressUpdate...
 *      step4, new SubThreadDemoUseAsyncTask(...).execute()来执行该任务
 * @author created by Meiyu Chen at 2021-4-21 8:48, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class SubThreadDemoUseAsyncTask extends AsyncTask<Void, Integer, Boolean> {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private ProgressDialog progressDialog;

    public SubThreadDemoUseAsyncTask(Context context) {
        this.context = context;
    }

    /** Description: onPreExecute会在后台任务开始执行之前调用，用于进行一些界面上的初始化操作，比如显示一个进度条对话框等
     * @author created by Meiyu Chen at 2021-4-21 9:02, v1.0
     */
    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        // UI操作：比如显示一个进度条对话框
        progressDialog.show();
//        ProgressDialog.show(context,"进度条对话框标题","进度条对话框内容");
    }

    /** Description: doInBackground中的所有代码都会在子线程中运行，我们应该在这里去处理所有的耗时任务。
     * 子线程中执行耗时任务，不会阻塞主线程，不用阻塞UI
     * 任务一旦完成就可以通过return语句来将任务的执行结果返回，当然，如果AsyncTask的第三个泛型参数指定的是Void,就可以不返回任务执行结果
     * 注意，在这个方法中是不可以进行UI操作的，如果需要更新UI元素，比如说反馈当前任务的执行进度，可以调用publishProgress(Progress)方法来完成
     * @author created by Meiyu Chen at 2021-4-21 9:03, v1.0
     * @return AsyncTask<,,Result> 通过return返回耗时任务执行结果，通知线程调度器调用onPostExecute接收该结果，并更新UI
     *      执行结果的类型是在定义class时通过AsyncTask的泛型参数Result指定的
     *      如，本例中就指定了Result的类型是Boolean, 实际开发过程中，你也可以根据业务需要将Result类型指定为其他数据类型
     *      如，如果你需要在子线程执行HTTP请求之类的耗时操作时，你可以将Result定义为Object，用于存储HTTP应答报文，并作为耗时操作的结果返回给UI线程
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        try{
            // 模拟耗时操作
            timeConsumingOpr();
        }catch (Exception e) {
            // 耗时操作任务进行过程中出现异常，返回false作为任务处理结果
//            progressDialog.dismiss();
            return false;
        }
        // 耗时操作正常完成，返回true作为任务处理结果
        // 通过return返回耗时任务操作结果，通知线程调度器调用onPostExecute()接收该结果，更新UI
        return true;
    }

    /** Description: 模拟耗时操作（如网络请求/IO/复杂计算...）
     * @author created by Meiyu Chen at 2021-4-21 10:28, v1.0
     */
    private void timeConsumingOpr() {
        int progress = 0;
        while(true){
            // 模拟耗时操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.i(TAG, "doInBackground: 线程挂起中断。");
                e.printStackTrace();
            }
            // 进度更新
            progress += 20;
            // 调用publishProgress将耗时任务执行进度，或者其他参数发布出去，通知调度器调用onProgressUpdate来接收进度，并且更新UI进度条
            publishProgress(progress);
            // 任务结束条件
            if (progress>=100) {
                break;
            }
        }
    }

    /** Description: 当在后台任务中调用了publishProgress()之后，onProgressUpdate()方法就会很快被调用
     * 方法中携带的参数就是在后台任务中传递过来的。
     * 在onProgressUpdate()方法中可以对UI进行操作，利用参数中的数值就可以对界面元素进行相应的更新
     * @author created by Meiyu Chen at 2021-4-21 9:03, v1.0
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
        // 接收publishProgress发过来的进度，更新到UI上
        progressDialog.setMessage("耗时任务执行进度：" + values[0] + "%");
    }

    /** Description: 当后台任务执行完毕并通过return语句进行返回时(也就是doInBackground执行了return之后)，onPostExecute方法就很快会被调用。
     * 返回的数据会作为参数传递到onPostExecute中，可以利用返回的数据来进行一些UI操作，
     * 比如说提醒任务执行的结果，以及关闭掉进度条对话框等
     * @author created by Meiyu Chen at 2021-4-21 10:04, v1.0
     */
    @Override
    protected void onPostExecute(Boolean taskConsumingTaskOperateRst) {
//        super.onPostExecute(aBoolean);
        // 子线程结束之后（也即doInBackground执行了return之后），开始执行onPostExecute函数
        // 可以在onPostExecute中进行一些UI操作
        // 如：关闭进度条
        progressDialog.dismiss();
        // 如：提示耗时任务结束/异常退出
        if (taskConsumingTaskOperateRst) {
            Toast.makeText(context,"耗时任务成功完成",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"耗时任务失败，执行过程中遇到异常情况",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }
}
