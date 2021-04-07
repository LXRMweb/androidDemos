package cn.lxrm.demo.utils.touchevent;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

/**
 * README: 判断触摸事件类型
 *
 * @author created by ChenMeiYu at 2021-4-6 10:11, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class MyTouchEventListener implements View.OnTouchListener {
    /** 起始位置：手指触摸屏幕的起始坐标, 一个touch事件的起始位置
     */
    private Point startPoint = new Point();
    /** 最小滑动距离：滑动距离大于该距离是才可以判定为滑动事件
     */
    private static final int MIN_MOVE_DIS = 20;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        getEventType(event);
        return false;
    }

    /** Description: 获取事件类型
     * @author created by ChenMeiYu at 2021-4-6 10:17, v1.0
     */
    private boolean getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                setPoint(ev);
                return true;
//                // 其他事件交给系统去处理(如“单击事件”)
//                super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                setPoint(ev);
                return isLeftRight(ev).equals(EventTypeEnum.UNDEFINED)?false:true;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
            default:
                return false;
        }
    }

    private EventTypeEnum isLeftRight(MotionEvent ev) {
        int dX = Math.abs((int) ev.getX() - startPoint.x);
        int dY  =Math.abs((int) ev.getY() - startPoint.y);
        if(dX > MIN_MOVE_DIS && dX > dY){
            return ((int) ev.getX() - startPoint.x) > 0 ? EventTypeEnum.LEFT_TO_RIGHT : EventTypeEnum.RIGHT_TO_LEFT;
        } else if(dY > MIN_MOVE_DIS && dY >dX){
            return ((int) ev.getY() - startPoint.y) > 0 ? EventTypeEnum.UP_TO_DOWN : EventTypeEnum.DOWN_TO_UP;
        }
        return EventTypeEnum.UNDEFINED;
    }

    private void setPoint(MotionEvent ev) {
        startPoint.x = (int) ev.getX();
        startPoint.y = (int) ev.getY();
    }

}
