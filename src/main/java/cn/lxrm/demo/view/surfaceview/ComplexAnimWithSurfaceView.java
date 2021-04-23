package cn.lxrm.demo.view.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.appdemo01.R;

/**
 * README: 使用surfaceView绘制复杂动画（推荐）
 *      推荐使用SurfaceView绘制复杂动画
 *      因为SurfaceView是对View的进一步封装，性能更好，尤其是SurfaceView的绘图都是在自己的独立的视图中进行的，不占用 UI线程和UI界面
 *      所以SurfaceView绘制复杂图形并不会阻塞主线程
 * @author created by Meiyu Chen at 2021-4-23 11:00, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class ComplexAnimWithSurfaceView extends SurfaceView {
    private float centerX=0f,centerY=0f;
    private Paint paint;
//    private int[] colors = {R.color.red,R.color.white,R.color.yellow,R.color.green,R.color.gray_game2048};
    private int[] colors = {R.color.blue, R.color.white, R.color.red,R.color.green,R.color.yellow, R.color.red,  R.color.red,  R.color.red,  R.color.red, R.color.gray_game2048};
    private final int TOTAL_NUM = 5000;
    private float mWidth;
    private float mHeight;
    private final String TAG = this.getClass().getSimpleName();

    public ComplexAnimWithSurfaceView(Context context) {
        super(context);
        initView();
    }

    public ComplexAnimWithSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ComplexAnimWithSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ComplexAnimWithSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        Log.d(TAG, "initView: ");
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        centerX = event.getX();
        centerY = event.getY();

        // 复杂图形绘制（耗时操作）：绘制几千个同心圆
        // surfaceView中通过以下方式获取画布canvas
        Canvas canvas = getHolder().lockCanvas();
        // 下次绘制之前先清空之前绘制的东西
        canvas.drawColor(getResources().getColor(R.color.black));
        int idx = 0;
        for (int i = 0; i < TOTAL_NUM; i++) {
            idx = (int) (Math.random() * colors.length);
            paint.setColor(colors[idx]);
            float radius = i % (Math.min(mHeight, mWidth) / 2);
            canvas.drawCircle(centerX, centerY, radius, paint);
        }
        // 一定要释放canvas（否则就会出现运行时错误了）
        getHolder().unlockCanvasAndPost(canvas);
        return super.onTouchEvent(event);
    }
}
