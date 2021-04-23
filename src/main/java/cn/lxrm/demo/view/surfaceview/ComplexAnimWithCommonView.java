package cn.lxrm.demo.view.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.appdemo01.R;

/**
 * README: 使用普通view绘制复杂动画(不推荐)
 *      不推荐使用普通view绘制复杂动画
 *      因为普通view的onDraw()函数执行过程是在UI线程中进行的
 *      普通view绘制图形的相关操作都放在onDraw函数中，onDraw又在UI线程中执行，所以onDraw耗时较长时就会阻塞UI线程，用户就会感觉到画面卡顿
 *      因为绘制复杂动画时，耗时较长，将会导致屏幕卡顿，卡顿过程中Activity中其他view将无法响应点击、触摸事件
 *
 * @author created by Meiyu Chen at 2021-4-23 9:09, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class ComplexAnimWithCommonView extends View {
    private final String TAG = this.getClass().getSimpleName();
    // 设备屏幕宽、高
    private float mWidth, mHeight;
    // 画笔
    private Paint paint;
    // 画笔可选颜色集合
    private int[] colors = {R.color.white, R.color.red,R.color.green,R.color.yellow, R.color.red,  R.color.red,  R.color.red,  R.color.red, R.color.gray_game2048};
    // 触摸点坐标（之后将以此点为中心，绘制同心圆）
    private float centerX = 0f, centerY = 0f;
    // 同心圆个数：绘制多少个同心圆
    private final int TOTAL_NUM = 10000;

    public ComplexAnimWithCommonView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    public ComplexAnimWithCommonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ComplexAnimWithCommonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ComplexAnimWithCommonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        // 告诉屏幕当前view已经失效，下一次刷新屏幕时记得更新该view的相关属性的值
        // 如本例中，就是需要告诉OS，当前view的 mHeight && mWidth 属性已经更新了
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        centerX = event.getX();
        centerY = event.getY();
        // 告诉屏幕当前view已经失效，下一次刷新屏幕时记得更新该view的相关属性的值
        // 如本例中，就是需要告诉OS，当前view的centerX、centerY属性已经更新了
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int idx = 0;
        for (int i = 0; i < TOTAL_NUM; i++) {
            idx = (int) (Math.random() * colors.length);
            paint.setColor(colors[idx]);
            float radius = i % (Math.min(mHeight, mWidth) / 2);
            canvas.drawCircle(centerX, centerY, radius, paint);
        }
    }
}
