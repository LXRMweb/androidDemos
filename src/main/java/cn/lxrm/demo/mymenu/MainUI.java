package cn.lxrm.demo.mymenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * README: 自定义菜单（不使用任何三方框架，完全使用原生API实现左菜单+右菜单功能界面）
 *              - 不使用任何三方框架，实现菜单功能
 *              - 左菜单 + 右菜单
 *              - 菜单滑动动画：右滑操作，滑动距离小于屏幕宽度的二分之一时，菜单自动归位
 *                      滑动距离大于屏幕宽度的二分之一时，展示完整的左菜单
 *              - 遮罩效果：滑动操作拉出菜单+的过程中，内容部分显示遮罩，且遮罩透明度逐渐变化
 * @author created by ChenMeiYu at 2021-1-28 11:00, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class MainUI extends RelativeLayout {
    private FrameLayout leftMenu;
    private FrameLayout content;
    private FrameLayout rightMenu;
    private Context context;
    private final String TAG = this.getClass().getSimpleName();

    public MainUI(Context context) {
        super(context);
        initView(context);
    }

    public MainUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /** Description: 初始化页面
     * @author created by ChenMeiYu at 2021-1-28 11:09, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param context:Context
     * @return void
     * @throws
     */
    private void initView(Context context) {
        this.context = context;

        // 初始化菜单
        leftMenu = new FrameLayout(context);
        content = new FrameLayout(context);
        rightMenu = new FrameLayout(context);

        // 设置背景色
        leftMenu.setBackgroundColor(Color.BLACK);
        content.setBackgroundColor(Color.LTGRAY);
        rightMenu.setBackgroundColor(Color.DKGRAY);

        // 将菜单填充至父容器（extends RelativeLayout）
        addView(leftMenu);
        addView(content);
        addView(rightMenu);

        //
    }

    /** Description: 获取屏幕宽高
     *      设置菜单的尺寸（宽度，高度）
     * @author created by ChenMeiYu at 2021-1-28 11:17, v1.0
     * @param widthMeasureSpec：int 设备，屏幕宽度
     * @param heightMeasureSpec: int 设备，屏幕高度
     * @return void
     * @throws
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置菜单的宽、高、放置位置
        // 内容部分，设为和屏幕尺寸相同
        content.measure(widthMeasureSpec,heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        int menuWidthMeasure = MeasureSpec.makeMeasureSpec((int) (realWidth * 0.6f), MeasureSpec.EXACTLY);
        // 左菜单：设为屏幕宽度的60%，和屏幕高度相同
        leftMenu.measure(menuWidthMeasure,heightMeasureSpec);
        // 右菜单：宽高
        rightMenu.measure(menuWidthMeasure,heightMeasureSpec);
    }

    /** Description: 设定菜单布局（在屏幕中的位置）
     * @author created by ChenMeiYu at 2021-1-28 14:40, v1.0
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        content.layout(l, t, r, b);
        leftMenu.layout(l-leftMenu.getMeasuredWidth(),t,l,b);
        rightMenu.layout(r,t,r+rightMenu.getMeasuredWidth(),b);
    }

    /** lastPoint: 上次屏幕事件的位置
     */
    private Point lastPoint = new Point();
    /** MIN_MOVE_DISTANCE: 判定为滑动事件的最短距离（只有滑动距离大于该值时，才可判定为滑动事件）
     */
    private static final int MIN_MOVE_DISTANCE = 20;
    private boolean isTestCompleted;
    private boolean isLeftEightEventFlag;

    /** Description: 事件处理(事件分发)
     *      处理左右滑动、上下滑动、点击事件，左滑一定距离展示右菜单，右滑一定距离展示左菜单
     * @author created by ChenMeiYu at 2021-1-28 15:45, v1.0
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isTestCompleted){
            getEventType(ev);
            return true;
        }
        showToast("事件分发：" + ev.getActionMasked());
        if(isLeftEightEventFlag){
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    showToast("ACTION_MOVE:::lastPoint="+lastPoint);
                    if (isLeftRightEvent(ev)) {
                        // 展示左菜单/右菜单
                        showMenu(ev);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    this.isTestCompleted = false;
                    this.isLeftEightEventFlag = false;
                    break;
                default:
                    break;
            }
        }else{
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_UP:
                    this.isLeftEightEventFlag = false;
                    this.isTestCompleted = false;
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                setPoint(ev,lastPoint);
                break;
            case MotionEvent.ACTION_MOVE:
                this.isLeftEightEventFlag = isLeftRightEvent(ev);
                this.isTestCompleted = true;
                setPoint(ev,lastPoint);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
            default:
                break;
        }
    }

    private void showToast(String s) {
        Log.v(TAG,s);
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    /** Description: 左/右滑动展示左/右菜单
     * @author created by ChenMeiYu at 2021-3-1 11:16, v1.0
     */
    private void showMenu(MotionEvent ev) {
        // 获取当前view左上角距离坐标系原点的x轴坐标
        int curr_x = getScrollX();
        // 获取x轴滑动距离（可正可负）
        int move_dis_x = (int) (ev.getX() - lastPoint.x);
        int expectX = -move_dis_x + curr_x;
        showToast("菜单："+curr_x+","+move_dis_x+","+expectX);
        int finalX = 0;
        if(expectX<0){
            // 右滑
            finalX = Math.max(expectX, -leftMenu.getMeasuredWidth());
        } else {
            finalX = Math.min(expectX, rightMenu.getMeasuredWidth());
        }
        lastPoint.x = (int) ev.getX();
        showToast("展示菜单：" + finalX);
        scrollTo(finalX,0);
    }

    /** Description: 判断是否是左右滑动事件
     * @author created by ChenMeiYu at 2021-3-1 10:38, v1.0
     * @param ev:MotionEvent 屏幕操作事件
     * @return boolean true-表示是左右滑动事件；false-表示不是左右滑动事件
     */
    private boolean isLeftRightEvent(MotionEvent ev) {
        boolean isLeftRightEvent = false;
        // 手指滑动距离
        int dx = Math.abs((int) ev.getX()- lastPoint.x);
        int dy = Math.abs((int) ev.getY()- lastPoint.y);
        if (dx > MIN_MOVE_DISTANCE && dx > dy) {
            // 滑动距离大于最小值，是滑动事件，且横向移动距离大于纵向移动距离，判定是左右滑动事件
            isLeftRightEvent = true;
        }
        showToast("是否左右滑动：" + isLeftRightEvent);
        return isLeftRightEvent;
    }

    /** Description: 记录当前事件的屏幕位置
     * @author created by ChenMeiYu at 2021-3-1 10:05, v1.0
     */
    private void setPoint(MotionEvent ev,Point point) {
        point.x = (int) ev.getX();
        point.y = (int) ev.getY();
        showToast("lastPoint:::"+lastPoint);
    }

} // end class
