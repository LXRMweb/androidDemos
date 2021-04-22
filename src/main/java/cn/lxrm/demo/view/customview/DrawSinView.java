package cn.lxrm.demo.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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
 *
 * 参考资料：
 *      【视频教程】（https://www.bilibili.com/video/BV1Xv411k756）
 * 调试工具： 设置 - 开发者选项 - monitoring - Profile HWUI rendering
 *              设置 - 开发者选项 - 监控 - GPU呈现模式分析 - 在屏幕上显示为条形图
 *      通过上述工具查看当前view的绘图性能表现
 *      解析： 绿色的线表示每一帧视图加载时间，如60FPS（frame per seconds）的设备,一帧视图的加载时间约为1000 / 60 ~ 16ms
 *          下面的直方图表示实际每一帧视图加载时间，重点关注绿色部分，
 *          如果绿色部分高出上面绿色的线条，就说明你的当前activity需要改进，你需要降低相应视图中计算复杂度、图形绘制复杂度等，从而降低整个activity加载时间
 *          防止实际加载时间超出上限（上方的绿色线条），出现“掉帧”现象
 *
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
    // 正弦路径
    private Path sinWavePath = new Path();
    // 余弦路径
    private Path cosWavePath = new Path();
    // 采样频率（采样点数）
    public static final int SAMPLES_COUNT = 100;


    // 画笔
    // 实线画笔
    private Paint solidLinePaint;
    // 实线画笔(红色)
    private Paint solidRedLinePaint;
    // 实线画笔(蓝色)
    private Paint solidBlueLinePaint;
    // 虚线画笔（黄色）
    private Paint dashedLinePaint;
    // 虚线画笔(白色)
    private Paint dashedWhiteLinePaint;
    // 矢量画笔
    private Paint vectorLinePaint;
    // 填充画笔
    private Paint filledCirclePaint;
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

        // 实线画笔
        solidRedLinePaint = new Paint();
        // 画笔样式：STROKE-无填充；FILL-有填充
        solidRedLinePaint.setStyle(Paint.Style.STROKE);
        // 设置线条宽度
        solidRedLinePaint.setStrokeWidth(5f);
        // 设置画笔颜色
        solidRedLinePaint.setColor(getResources().getColor(R.color.red));

        // 实线画笔
        solidBlueLinePaint = new Paint();
        // 画笔样式：STROKE-无填充；FILL-有填充
        solidBlueLinePaint.setStyle(Paint.Style.STROKE);
        // 设置线条宽度
        solidBlueLinePaint.setStrokeWidth(5f);
        // 设置画笔颜色
        solidBlueLinePaint.setColor(getResources().getColor(R.color.blue));

        // 矢量实线画笔
        vectorLinePaint = new Paint();
        // 画笔样式：STROKE-无填充；FILL-有填充
        vectorLinePaint.setStyle(Paint.Style.STROKE);
        // 设置线条宽度
        vectorLinePaint.setStrokeWidth(5f);
        // 设置画笔颜色
        vectorLinePaint.setColor(getResources().getColor(R.color.green));

        // 实心填充画笔
        filledCirclePaint = new Paint();
        // 画笔样式：FILL-有填充
        filledCirclePaint.setStyle(Paint.Style.FILL);
        // 画笔颜色
        filledCirclePaint.setColor(getResources().getColor(R.color.white));

        // 虚线画笔
        dashedLinePaint = new Paint();
        dashedLinePaint.setColor(getResources().getColor(R.color.yellow));
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setStrokeWidth(5f);
        // 虚线
        float[] arr = {10f, 10f};
        dashedLinePaint.setPathEffect(new DashPathEffect(arr, 0f));

        // 虚线画笔(白素)
        dashedWhiteLinePaint = new Paint();
        dashedWhiteLinePaint.setColor(getResources().getColor(R.color.white));
        dashedWhiteLinePaint.setStyle(Paint.Style.STROKE);
        dashedWhiteLinePaint.setStrokeWidth(5f);
        dashedWhiteLinePaint.setPathEffect(new DashPathEffect(arr, 0f));

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
//        // Log.d(TAG, "onSizeChanged: w=" + w + ",h=" + h + ",oldW=" + oldw + ",oldH=" + oldh);

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
        drawProjections(canvas);
        drawSinWave(canvas);
        drawCosWave(canvas);
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
        // 画布重置（将画布恢复到原始位置）
        canvas.restore();
    }

    /** Description: 绘制“矢量投影”
     * @author created by Meiyu Chen at 2021-4-22 9:57, v1.0
     */
    private void drawProjections(Canvas canvas) {
        // Log.d(TAG, "drawProjections: ");
        /*  当前角度的余弦值、正弦值
        *       注意: degree 和 radians 角度和弧度的相互转换
        *       注意：投影要乘以半径 */
        float cosValue = (float) Math.cos(Math.toRadians(currAngle));
        float sinValue = (float) Math.sin(Math.toRadians(currAngle));
        // 矢量在x轴，y轴的投影坐标值
        float vectorDx = radius * cosValue;
        float vectorDy = radius * sinValue;
        // Log.d(TAG, "drawProjections: vectorDx = " + vectorDx + ",vectorDy = " + vectorDy);

        drawWithTranslation(canvas,mWidth/2,mHeight/2);
        // 在x轴上的投影（圆点）
        canvas.drawCircle(vectorDx,0f,10f,filledCirclePaint);
        canvas.restore();

        // 在矢量圆的中轴线上的投影（圆点）
        drawWithTranslation(canvas,mWidth/2,mHeight/4*3);
        canvas.drawCircle(vectorDx,0f,10f,filledCirclePaint);
        canvas.restore();

        // 绘制投影线
        // 将坐标系原点平移到投影线和矢量圆的交点处，之后再绘制投影线就相当于在y轴上绘制直线了
        drawWithTranslation(canvas,mWidth/2+ vectorDx,mHeight/4*3- vectorDy);
        canvas.drawLine(0f,0f,0f,-mHeight/4+ vectorDy,dashedWhiteLinePaint);
        // 绘制当前角度下，矢量圆半径在y轴方向上的投影（radius*sin(currAngle)）
        canvas.drawLine(0f,0f,0f,vectorDy,solidBlueLinePaint);
        canvas.restore();
        // 绘制当前角度下，矢量圆半径在x轴方向上的投影（radius*cos(currAngle)）
        drawWithTranslation(canvas,mWidth/2,mHeight*3/4);
        canvas.drawLine(0f,0f,vectorDx,0,solidRedLinePaint);
        canvas.restore();
    }

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
                        // Log.d(TAG, "doInBackground: currAngle = " + currAngle);
                        // Log.d(TAG, "doInBackground: currAngle = 直接在子线程中调用invalidate()函数");
//                        publishProgress();
                        invalidate();
                    } catch (InterruptedException e) {
                        // Log.d(TAG, "doInBackground: 矢量旋转线程被中断");
                    }
                }
            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                // invalidate():  重新执行onDraw()，重新绘制view，必须在UI线程中调用
//                invalidate();
//            }
        }.execute();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pauseRotating(){
        // Log.d(TAG, "pauseRotating: ");
        // 使用cancel函数并不能将子线程停止
//        asyncTask.cancel(false);
        // 有很多种方法可以实现立即停止子线程的功能，如：标志位法、异常法....
        if (asyncTask!=null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            asyncTask.cancel(true);
        }
    }

    /** Description: 绘制正弦函数
     *      用绘制路径的方法绘制正弦函数
     *      待绘制路径： path = radius * sin(ax+b) = radius * sin(ax+currAngle), 可以设置角速度a为任意常量, 设置矢量当前角度currAngle为初始相位
     *      使用采样的方法采样若干个点，然后将相邻的点用贝叶斯函数连起来，就形成了我们想要的路径
     *      绘制路径之前先将屏幕平移到坐标系原点，再逆时针旋转90度，绘制过程会更简单
     *
     * @author created by Meiyu Chen at 2021-4-22 14:16, v1.0
     */
    private void drawSinWave(Canvas canvas) {
//        Log.d(TAG, "drawSinWave: ");
        // 采样间隔
        float dx = mHeight / 2 / SAMPLES_COUNT;
        // 清空历史数据
        sinWavePath.reset();
        // 将路径移动到起始位置（正弦函数和y轴的交点）
        sinWavePath.moveTo(0f, (float) (radius*Math.sin(Math.toRadians(currAngle))));
        for (int i = 0; i < SAMPLES_COUNT; i++) {
            float currX = dx * i;
            float a = 0.15f;
            float currY =(float) (radius * Math.sin(a * i + Math.toRadians(currAngle)));
            sinWavePath.quadTo(currX,currY,currX,currY);
        }
        drawWithTranslation(canvas,mWidth/2,mHeight/2,-90f);
        canvas.drawPath(sinWavePath,solidBlueLinePaint);
        // 沿着路径绘制文字
        canvas.drawTextOnPath("sin",sinWavePath, 1000f,20f,textPaint);
        canvas.restore();
    }

    /** Description: 绘制余弦函数曲线
     *      画布先平移、旋转，绘制过程会更简单
     *      绘制路径时，使用采样方式，先获取路径上的若干个采样点（x,y）,然后将这些采样点连起来绘制成路径
     *      采样频率将影响路径
     *      路径 = radius * cos(ax + currAngle), 其中，radius是震荡幅度，currAngle是初始相位
     * @author created by Meiyu Chen at 2021-4-22 15:41, v1.0
     */
    private void drawCosWave(Canvas canvas) {
//        Log.d(TAG, "drawCosWave: ");
        // 采样间隔
        float dx = mHeight / 2 / SAMPLES_COUNT;
        // 清空历史数据
        sinWavePath.reset();
        // 将路径移动到起始位置（正弦函数和y轴的交点）
        sinWavePath.moveTo(0f, (float) (radius*Math.cos(Math.toRadians(currAngle))));
        for (int i = 0; i < SAMPLES_COUNT; i++) {
            float currX = dx * i;
            float a = 0.15f;
            float currY =(float) (radius * Math.cos(a * i + Math.toRadians(currAngle)));
            sinWavePath.quadTo(currX,currY,currX,currY);
        }
        drawWithTranslation(canvas,mWidth/2,mHeight/2,-90);
        canvas.drawPath(sinWavePath,solidRedLinePaint);
        // 沿着路径绘制文字
        canvas.drawTextOnPath("cos",sinWavePath, 1450f,20f,textPaint);
        canvas.restore();
    }
} // end class
