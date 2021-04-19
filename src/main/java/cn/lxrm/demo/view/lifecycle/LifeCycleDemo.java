package cn.lxrm.demo.view.lifecycle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

/**
 * README: 展示view生命周期函数用法
 *
 * @author created by Meiyu Chen at 2021-4-19 15:21, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class LifeCycleDemo extends View implements LifecycleObserver {
    private final String TAG = this.getClass().getSimpleName();

    public LifeCycleDemo(Context context) {
        super(context);
        Log.d(TAG, "LifeCycleDemo: ");
    }

    public LifeCycleDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "LifeCycleDemo: ");
    }

    public LifeCycleDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "LifeCycleDemo: ");
    }

    public LifeCycleDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.d(TAG, "LifeCycleDemo: ");
    }

    /**
     * Description: XxxView被new之后，调用完该view的构造函数之后，就会调用该view的onAttachedToWindow()生命周期函数
     *
     * @author created by Meiyu Chen at 2021-4-19 15:24, v1.0
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
    }

    /**
     * Description: 该view所属*-layout.xml加载完该view之后，会调用onFinishInflate()生命周期函数
     *
     * @author created by Meiyu Chen at 2021-4-19 16:21, v1.0
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate: ");
    }

    /**
     * Description: 调用完onAttachedToWindow()生命周期函数后，就去调用该view的measure()函数，测量该view的尺寸
     * 尺寸测量完成之后会调用onMeasure()这个回调函数，通过这个回调函数将该view的尺寸传给父容器
     *
     * @author created by Meiyu Chen at 2021-4-19 15:26, v1.0
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }

    /**
     * Description: 该view的尺寸最终确定之后调用onSizeChanged()生命周期函数
     *      调用完onMeasure()之后会调用onSizeChanged()
     *
     * @author created by Meiyu Chen at 2021-4-19 16:34, v1.0
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: ");
    }

    /**
     * Description: 调用完onSizeChanged生命周期函数之后会调用layout()，获取该view在父容器中/屏幕中的位置
     * 获取到在父容器中、屏幕中的位置之后，调用onLayout()回调函数，将该view在父容器中的位置告知父容器
     *
     * @author created by Meiyu Chen at 2021-4-19 15:29, v1.0
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: ");
    }

//    /** Description: 调用完onLayout生命周期函数之后，并不是马上就可以在屏幕上绘制该view
//     *      而是要将该view的绘制任务添加到队列中，等下一帧开始的时钟信号到来时，才能调用draw/onDraw()将该view绘制到屏幕上
//     * @author created by Meiyu Chen at 2021-4-19 15:35, v1.0
//     */
//    @Override
//    protected void dispatchToDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//    }

    /**
     * Description: 自定义的绘图过程一般是放在onDraw()生命周期函数中
     *
     * @author created by Meiyu Chen at 2021-4-19 16:49, v1.0
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
    }

    /**
     * Description: 没讲如何使用
     * todo- 生命周期函数：draw() vs onDraw()
     *      调用onDraw之后才会调用draw()
     * @author created by Meiyu Chen at 2021-4-19 15:40, v1.0
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d(TAG, "draw: ");
    }

    /**
     * Description: [TODO-功能描述]
     *
     * @author created by Meiyu Chen at 2021-4-19 16:31, v1.0
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow: ");
    }

    /**
     * Description: 如果下一帧画面到来时，想要刷新该view，就调用invalidate()
     * 调用invalidate()之后，会跳转至该view的dispatchToDraw()函数
     * 通知系统当前view已过时，下一帧画面开始时需要重新绘制该view
     *
     * @author created by Meiyu Chen at 2021-4-19 16:01, v1.0
     */
    @Override
    public void invalidate(Rect dirty) {
        super.invalidate(dirty);
        Log.d(TAG, "invalidate: ");
    }

    /**
     * Description: 如果调用了requestLayout()，就会跳转到measure()，
     * 重新测量该view的尺寸，重新获取该view在屏幕中的位置，重新将该view的绘制任务放到资源调度队列，
     * 重新绘制该view...
     *
     * @author created by Meiyu Chen at 2021-4-19 16:04, v1.0
     */
    @Override
    public void requestLayout() {
        super.requestLayout();
        Log.d(TAG, "requestLayout: ");
    }
}
