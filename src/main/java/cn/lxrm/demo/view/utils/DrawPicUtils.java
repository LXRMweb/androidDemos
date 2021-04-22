package cn.lxrm.demo.view.utils;

import android.graphics.Canvas;
import android.util.Log;

public class DrawPicUtils{
    private static final String TAG = "DrawPicUtils";

    /** Description: 画布平移、旋转（使得画布坐标系原点和待绘制图形中心点重合）
     * @author created by Meiyu Chen at 2021-4-20 8:54, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param canvas:Canvas 画布
     * @param dx：float 画布水平移动距离和方向
     * @param dy：float 画布垂直移动距离和方向
     * @param angle：float 画布旋转角度(顺时针旋转)
     */
    public static void drawWithTranslation(Canvas canvas, float dx, float dy, float angle) {
        // 画布上原有内容保存
        canvas.save();
//        // 画布重置（将画布恢复到原始位置）
//        canvas.restore();
        // 画布平移
        canvas.translate(dx,dy);
        // 画布旋转
        canvas.rotate(angle);
//        // 画布重置（防止之后绘制的图形不正确）
//        canvas.restore();
    }

    /** Description: 画布平移、旋转（使得画布坐标系原点和待绘制图形中心点重合）
     * @author created by Meiyu Chen at 2021-4-20 8:54, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param canvas:Canvas 画布
     * @param dx：float 画布水平移动距离和方向
     * @param dy：float 画布垂直移动距离和方向
     */
    public static void drawWithTranslation(Canvas canvas, float dx, float dy) {
        Log.d(TAG, "drawWithTranslation: dx="+dx+",dy="+dy);
        // 画布上原有内容保存
        canvas.save();
        // 画布平移
        canvas.translate(dx,dy);
//        // 画布重置（防止之后绘制的图形不正确）
//        canvas.restore();
    }
}
