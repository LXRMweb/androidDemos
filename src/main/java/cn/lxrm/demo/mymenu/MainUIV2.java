package cn.lxrm.demo.mymenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

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
public class MainUIV2 extends RelativeLayout {
    private FrameLayout leftMenu;
    private FrameLayout content;
    private FrameLayout rightMenu;
    private FrameLayout middleMask;
    public static final int LEFT_MENU_ID = 0X000001;
    public static final int MIDDLE_CONTENT_ID = 0X001000;
    public static final int RIGHT_MENU_ID = 0X100000;
    private Context context;
    private Scroller mScroller;
    private final String TAG = this.getClass().getSimpleName();

    public MainUIV2(Context context) {
        super(context);
        initView(context);
    }

    public MainUIV2(Context context, AttributeSet attrs) {
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
        this.mScroller = new Scroller(context,new DecelerateInterpolator());

        // 初始化菜单
        leftMenu = new FrameLayout(context);
        content = new FrameLayout(context);
        rightMenu = new FrameLayout(context);
        middleMask = new FrameLayout(context);
        leftMenu.setId(LEFT_MENU_ID);
        content.setId(MIDDLE_CONTENT_ID);
        rightMenu.setId(RIGHT_MENU_ID);

        // 设置背景色
        leftMenu.setBackgroundColor(Color.BLACK);
        content.setBackgroundColor(Color.GREEN);
        rightMenu.setBackgroundColor(Color.DKGRAY);
        middleMask.setBackgroundColor(0x88000000);

        // 设置透明度（蒙板儿初始透明度是0-完全透明）
        middleMask.setAlpha(0);

        // 将菜单填充至父容器（extends RelativeLayout）
        addView(leftMenu);
        addView(content);
        addView(rightMenu);
        addView(middleMask);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        int currX = Math.abs(getScrollX());
        // 蒙板儿透明度随着滑动距离增加而减弱
        float scale = currX / (float) Math.min(leftMenu.getMeasuredWidth(),rightMenu.getMeasuredWidth());
        middleMask.setAlpha(scale);
        System.out.println("透明度："+middleMask.getAlpha());
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
        middleMask.measure(widthMeasureSpec,heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        int menuWidthMeasureLeft = MeasureSpec.makeMeasureSpec((int) (realWidth * 0.4f), MeasureSpec.EXACTLY);
        int menuWidthMeasureRight = MeasureSpec.makeMeasureSpec((int) (realWidth >> 2), MeasureSpec.EXACTLY);
        // 左菜单：设为屏幕宽度的60%，和屏幕高度相同
        leftMenu.measure(menuWidthMeasureLeft,heightMeasureSpec);
        // 右菜单：宽高
        rightMenu.measure(menuWidthMeasureRight,heightMeasureSpec);
    }

    /** Description: 设定菜单布局（在屏幕中的位置）
     * @author created by ChenMeiYu at 2021-1-28 14:40, v1.0
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        content.layout(l, t, r, b);
        middleMask.layout(l, t, r, b);
        leftMenu.layout(l-leftMenu.getMeasuredWidth(),t,l,b);
        rightMenu.layout(r,t,r+rightMenu.getMeasuredWidth(),b);
    }

    /** isOneTouchCompletedd:boolean 一次屏幕触摸事件是否结束
     */
    private boolean isOneTouchCompleted = false;
    /** isLeftRightEvent: 是否是左右滑动事件：true-是；false-否
     */
    private boolean isLeftRightEvent;

    /** Description: 处理屏幕触摸事件：左右滑动展示左菜单/右菜单
     * @author created by ChenMeiYu at 2021-3-1 15:51, v1.0
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(!this.isOneTouchCompleted){
            getEventType(ev);
            return true;
        }
        if(isLeftRightEvent){
            // 处理左右滑动事件
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_MOVE:
                    showMenu(ev);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    menuScrollAnim();
                    this.isOneTouchCompleted = false;
                    this.isLeftRightEvent = false;
                default:
                    break;
            }
        }else{ // 非左右滑动事件
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_UP:
                    this.isLeftRightEvent = false;
                    this.isOneTouchCompleted = false;
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);

    }

    /** Description: 左/右滑动展示左/右菜单动画效果
     *      左/右滑动距离超过左/右菜单宽度的二分之一时，手指抬起离开屏幕时，自动展示完整的左/右菜单
     *      滑动距离不足菜单宽度的二分之一时，恢复初始画面，展示内容隐藏左/右菜单
     * @author created by ChenMeiYu at 2021-3-1 16:48, v1.0
     */
    private void menuScrollAnim() {
        int scrollX = getScrollX();
        if(scrollX < 0 && (Math.abs(scrollX) > leftMenu.getMeasuredWidth() >> 1)){
            // 右滑距离大于左菜单宽度的一半
            // view右滑展示完整的左菜单
            mScroller.startScroll(scrollX,0,-leftMenu.getMeasuredWidth()-scrollX,0,100);
        } else if(scrollX > 0 && (Math.abs(scrollX) > rightMenu.getMeasuredWidth() >> 1)){
            // 左滑距离大于右菜单宽度的一半
            // view左滑展示完整的右菜单
            mScroller.startScroll(scrollX,0,rightMenu.getMeasuredWidth() - scrollX,0,100);
        } else {
            // 滑动距离小于菜单宽度的一半
            // view展示content部分，隐藏左右菜单
            mScroller.startScroll(scrollX,0,-scrollX,0,100);
        }
        // view重绘，刷新页面
        invalidate();
    }
//    private void menuScrollAnimV1() {
//        int scrollX = getScrollX();
//        if(Math.abs(scrollX) > leftMenu.getMeasuredWidth() >> 1){
//            // 滑动距离大于菜单宽度的一半
//            if(scrollX < 0){
//                // view右滑展示完整的左菜单
//                mScroller.startScroll(scrollX,0,-leftMenu.getMeasuredWidth()-scrollX,0,100);
//            } else {
//                // view左滑展示完整的右菜单
//                mScroller.startScroll(scrollX,0,leftMenu.getMeasuredWidth() - scrollX,0,100);
//            }
//        } else {
//            // 滑动距离小于菜单宽度的一半
//            // view展示content部分，隐藏左右菜单
//            mScroller.startScroll(scrollX,0,-scrollX,0,100);
//        }
//        // view重绘，刷新页面
//        invalidate();
//    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(!mScroller.computeScrollOffset()){
            return;
        }
        int currX = mScroller.getCurrX();
        scrollTo(currX,0);
    }

    /** Description: 滑动view, 展示左/右菜单
     * @author created by ChenMeiYu at 2021-3-1 15:57, v1.0
     */
    private void showMenu(MotionEvent ev) {
        int currScrollX = getScrollX();
        int dis_x = (int) (ev.getX() - startPoint.x);
        int expectX = -dis_x + currScrollX;
        int finalX = 0;
        if(expectX<0){
            finalX = Math.max(expectX,-leftMenu.getMeasuredWidth());
        }else{
            finalX = Math.min(expectX,rightMenu.getMeasuredWidth());
        }
        scrollTo(finalX,0);
        startPoint.x = (int) ev.getX();
    }

    private Point startPoint = new Point();
    public static final int MIN_MOVE_DIS = 20;
    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                setPoint(ev);
                // 其他事件交给系统去处理(如“单击事件”)
                super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                isLeftRight(ev);
                setPoint(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 其他事件交给系统去处理(如“单击事件”)
                super.dispatchTouchEvent(ev);
                isLeftRightEvent = false;
                isOneTouchCompleted = false;
                break;
            default:
                break;
        }
    }

    private void isLeftRight(MotionEvent ev) {
        int dX = Math.abs((int) ev.getX() - startPoint.x);
        int dY  =Math.abs((int) ev.getY() - startPoint.y);
        if(dX > MIN_MOVE_DIS && dX > dY){
            // 左右滑动
            this.isLeftRightEvent = true;
            isOneTouchCompleted = true;
        } else if(dY > MIN_MOVE_DIS && dY >dX){
            this.isLeftRightEvent = false;
            this.isOneTouchCompleted = true;
        } else {
            this.isLeftRightEvent = false;
        }
    }

    private void setPoint(MotionEvent ev) {
        startPoint.x = (int) ev.getX();
        startPoint.y = (int) ev.getY();
    }
} // end class
