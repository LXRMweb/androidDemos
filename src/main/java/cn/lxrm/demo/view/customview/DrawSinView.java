package cn.lxrm.demo.view.customview;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.appdemo01.R;

import static cn.lxrm.demo.view.utils.DrawPicUtils.*;

/**
 * README: 绘制正弦函数
 *
 * 自定义视图和所属Activity生命周期绑定：
 *      定义：所谓生命周期绑定，就是说要让你的自定义view和其所属activity生命周期一致，activity进入暂停态时，你的自定义view也进入暂停态
 *            activity激活时，view也跟着激活，如：本例中的矢量旋转函数，和activity生命周期绑定到一起了，那activity暂停时，暂停矢量旋转，activity恢复可见状态时，矢量旋转动作也恢复
 *      代码实现步骤：
 *          step1，自定义view要实现LifecycleObserver接口，将这个自定义视图和其所属Activity的生命周期绑定在一起
 *          step2，为自定义view的相应method添加注解
 *              如，本例中为startRotating()函数添加注解 @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
 *                  使得该自定义view的矢量旋转动作跟activity的生命周期绑定在一起，activity进入暂停态时，矢量旋转暂停，activity恢复活跃态时，矢量旋转恢复
 *          step3, 在相应的activity中为自定义view添加obsever
 *              如：在TestDrawSinViewMainActivity中编写
 *                   DrawSinView drawSinView = (DrawSinView) findViewById(R.id.drawSinView);
 *                   // 为自定义视图添加activity生命周期监听器，将自定义视图和activity生命周期绑定在一起
 *                   getLifecycle().addObserver(drawSinView);
 * @author created by Meiyu Chen at 2021-4-19 14:55, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class DrawSinView extends View implements LifecycleObserver {
    private final String TAG = this.getClass().getSimpleName();

    // 当前view组件的宽度和高度
    private float mWidth = 0f;
    private float mHeight = 0f;
    // 圆-半径
    private float radius;
    // 矢量当前角度
    private float currAngle = 10f;

    // 画笔
    // 实线画笔
    private Paint solidLinePaint;
    // 虚线画笔
    private Paint dashedLinePaint;
    // 矢量画笔
    private Paint vectorLinePaint;
    // 文字画笔
    private Paint textPaint;

    // 子线程（矢量旋转线程）
    private AsyncTask<Void, Void, Void> asyncTask;

    public DrawSinView(Context context) {
        super(context);
        initView();
    }

    public DrawSinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DrawSinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DrawSinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * Description: 初始化view
     * 初始化一些对象(如：画笔...)
     *
     * @author created by Meiyu Chen at 2021-4-19 17:16, v1.0
     */
    private void initView() {
        initPaints();
    }

    /**
     * Description: 初始化画笔（s)
     *
     * @author created by Meiyu Chen at 2021-4-19 17:15, v1.0
     */
    private void initPaints() {
        // 实线画笔
        solidLinePaint = new Paint();
        // 画笔样式：STROKE-无填充；FILL-有填充
        solidLinePaint.setStyle(Paint.Style.STROKE);
        // 设置线条宽度
        solidLinePaint.setStrokeWidth(5f);
        // 设置画笔颜色
        solidLinePaint.setColor(getResources().getColor(R.color.white));

        // 矢量实线画笔
        vectorLinePaint = new Paint();
        // 画笔样式：STROKE-无填充；FILL-有填充
        vectorLinePaint.setStyle(Paint.Style.STROKE);
        // 设置线条宽度
        vectorLinePaint.setStrokeWidth(5f);
        // 设置画笔颜色
        vectorLinePaint.setColor(getResources().getColor(R.color.green));

        // 虚线画笔
        dashedLinePaint = new Paint();
        dashedLinePaint.setColor(getResources().getColor(R.color.yellow));
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setStrokeWidth(5f);
        // 虚线
        float[] arr = {10f, 10f};
        dashedLinePaint.setPathEffect(new DashPathEffect(arr, 0f));

        // 文字画笔
        textPaint = new Paint();
        // 设置文字大小
        textPaint.setTextSize(40f);
        // 设置文字样式
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置文字颜色
        textPaint.setColor(getResources().getColor(R.color.white));
    }

    /**
     * Description: 该view的尺寸最终确定之后调用onSizeChanged()生命周期函数
     * 调用完onMeasure()之后会调用onSizeChanged()
     * 横屏和竖屏转换时，会调用onSizeChanged，会重新获取该view的宽和高
     *
     * @author created by Meiyu Chen at 2021-4-19 16:34, v1.0
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w=" + w + ",h=" + h + ",oldW=" + oldw + ",oldH=" + oldh);

        // 更新当前view的宽度和高度
        mWidth = w;
        mHeight = h;
        // 更新圆的半径
        radius = Math.min(mWidth / 2, mHeight / 4) - 40;
    }

    /**
     * Description: 自定义的绘图过程一般是放在onDraw()生命周期函数中
     * 不要在onDraw中new创建对象，因为每帧画面都会重新执行onDraw
     * 创建对象是非常耗用资源的, 不要在每帧画面中重建对象,否则会造成资源浪费，降低性能
     * 坐标系：
     * 绘图过程中常用两种方式确定坐标位置
     * 方式一，绝对坐标
     * 屏幕左上角是（0,0）
     * 往右是x轴正轴
     * 往下是y轴正轴
     * 方式二，旋转画布
     * 先将画布平移、旋转至待绘制图像中心位置
     * 以坐标系原点开始绘制待绘制图形
     * 保存
     * 重置画布
     * 进行下一轮循环
     *
     * @author created by Meiyu Chen at 2021-4-19 16:49, v1.0
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLable(canvas);
        drawAxises(canvas);
        drawCricle(canvas);
        drawVector(canvas);
    }

    /**
     * Description: 绘制标签
     *
     * @author created by Meiyu Chen at 2021-4-20 10:13, v1.0
     */
    private void drawLable(Canvas canvas) {
        // 画布平移
        drawWithTranslation(canvas, 30f, 30f);
        // 绘制标签外围的矩形
        canvas.drawRect(5f, 20f, 670f, 100f, solidLinePaint);
//        canvas.drawArc(100f,100f,500f,300f,5f,5f,false,solidLinePaint);
        // 绘制标签
        canvas.drawText("自定义视图：绘制正弦函数矢量动画", 10f, 75f, textPaint);
        // 画布重置（将画布恢复到原始位置）
        canvas.restore();
    }

    /**
     * Description: 绘制坐标系（两条横线，一条竖线）
     *
     * @author created by Meiyu Chen at 2021-4-19 17:36, v1.0
     */
    private void drawAxises(Canvas canvas) {
        // 平移画布，使得我们可以以坐标系原点为起点绘制目标图案
        drawWithTranslation(canvas, mWidth / 2, mHeight / 2);
        // 绘制中心X轴
        canvas.drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint);
        // 绘制中心Y轴
        canvas.drawLine(0f, -mHeight / 2, 0f, mHeight / 2, solidLinePaint);
        // 画布重置（将画布恢复到原始位置）
        canvas.restore();
    }

    /**
     * Description: 绘制圆上的x轴、圆、矢量
     *
     * @author created by Meiyu Chen at 2021-4-20 10:11, v1.0
     */
    private void drawCricle(Canvas canvas) {
        // 平移画布，使得我们可以以坐标系原点为起点绘制目标图案
        drawWithTranslation(canvas, mWidth / 2, mHeight / 4 * 3);
        // 绘制下方x轴（横穿圆的x轴）
        canvas.drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint);
        // 绘制圆
        canvas.drawCircle(0f, 0f, radius, dashedLinePaint);
        // 画布重置（将画布恢复到原始位置）
        canvas.restore();
    }

    /**
     * Description: 绘制矢量（圆里面的矢量）
     *
     * @author created by Meiyu Chen at 2021-4-20 10:49, v1.0
     */
    private void drawVector(Canvas canvas) {
        // 画布平移+旋转，使得待绘制的矢量原点和坐标系原点重合，方向和x轴垂直
        drawWithTranslation(canvas, mWidth / 2, mHeight / 4 * 3, -currAngle);
        canvas.drawLine(0, 0, radius, 0, vectorLinePaint);
    }

//    /** Description: 矢量旋转
//     * @author created by Meiyu Chen at 2021-4-20 14:56, v1.0
//     */
//    private void startRotating(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        // 每隔100ms, 矢量顺时针旋转5度
//                        Thread.sleep(100);
//                        currAngle += 5f;
//                        // 重新执行onDraw()，重新绘制view，必须在UI线程中调用
//                        invalidate();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }

    /** Description: 矢量旋转函数
     *      矢量匀速顺时针旋转，并且每次旋转之后刷新UI
     *      使用@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)注解将矢量旋转动作和activity生命周期的resume()绑定在一起
     *      使用@OnLifecycleEvent注解的函数不能是private的
     * @author created by Meiyu Chen at 2021-4-21 14:55, v1.0
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void startRotatingV2() {
        asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    if(asyncTask.isCancelled()){
                        asyncTask = null;
                        return null;
                    }
                    try {
                        // 每隔100ms, 矢量顺时针旋转5度
                        Thread.sleep(100);
                        currAngle += 5f;
                        currAngle %= 360;
                        Log.d(TAG, "doInBackground: currAngle = " + currAngle);
                        publishProgress();
                    } catch (InterruptedException e) {
                        Log.d(TAG, "doInBackground: 矢量旋转线程被中断");
                    }
                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                // invalidate():  重新执行onDraw()，重新绘制view，必须在UI线程中调用
                invalidate();
            }
        }.execute();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pauseRotating(){
        Log.d(TAG, "pauseRotating: ");
        // 使用cancel函数并不能将子线程停止
//        asyncTask.cancel(false);
        // 有很多种方法可以实现立即停止子线程的功能，如：标志位法、异常法....
        if (asyncTask!=null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            asyncTask.cancel(true);
        }
    }
} // end class
