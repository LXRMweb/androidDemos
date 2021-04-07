package cn.lxrm.demo.crazycat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * README: 布局（等价于布局文件layout）
 *
 * @author created by ChenMeiYu at 2021-1-26 10:56, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class Playground extends SurfaceView implements View.OnTouchListener {
    private String TAG = this.getClass().getSimpleName();
    // 画布宽度（水平方向总点数）
    private static final int COL_NUM = 25;
    // 画布高度（垂直方向总点数）
    private static int ROW_NUM = 13;
    // 点阵中每个点的宽度(单位：像素)
    private static int CELL_LEN = 100;
    // 阻碍块总块数
    private static int BLOCK_NUM = 15;
    // 画布点阵
    private DotVO[][] dotsArr;
    // 猫
    private DotVO cat;


    public Playground(Context context) {
        super(context);
        // 为当前SurfaceView添加回调函数
        getHolder().addCallback(callback);
        // 游戏初始化
        initGame();
        // 为当前对象实例注册touch事件监听器
        setOnTouchListener(this);
    }

    /** Description: 重新绘制界面
     * @author created by ChenMeiYu at 2021-1-26 10:58, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param
     * @return void
     * @throws
     */
    private void reDraw(){
        // 锁住画布
        Canvas canvas = getHolder().lockCanvas();
        // 背景色:CYAN-青色；LTGRAY-浅灰
//        canvas.drawColor(Color.CYAN);
        canvas.drawColor(Color.LTGRAY);

        // 绘制点阵、猫、障碍块
        Paint paint = new Paint();
        // 设置画笔的Flag,使得绘制出的效果更好
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        for(int col=0;col<COL_NUM;col++){
            for(int row=0;row<ROW_NUM;row++){
                DotVO dot = getDot(col, row);
                switch (dot.getStatus()){
                    case IN:
                        paint.setColor(0xFFFF0000);
                        break;
                    case ON:
                        paint.setColor(0XFFEEEEEE);
                        break;
                    case OFF:
                        paint.setColor(0xFFFFAA00);
                        break;
                    default:
                        break;
                }
                // Log.v(TAG,dot.toString()+": color = "+paint.getColor(),null);
                float offset = getOffset(row);
                canvas.drawOval(new RectF(dot.getCol()*CELL_LEN + offset,dot.getRow()*CELL_LEN,(dot.getCol()+1)*CELL_LEN + offset,(dot.getRow()+1)*CELL_LEN),paint);
            }
        }
        // 取消锁定并绘制相应内容
        getHolder().unlockCanvasAndPost(canvas);
    }

    Callback callback = new Callback(){
        /** Description: 创建SurfaceView时，执行该函数
         */
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            reDraw();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            // 适配不同的设备屏幕尺寸
            CELL_LEN = width / (COL_NUM + 1);
            ROW_NUM = (int) (height / CELL_LEN) - 2;
            BLOCK_NUM =(int) (ROW_NUM * COL_NUM * 0.1);
            initGame();
            reDraw();
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    };

    /** Description: 游戏初始化（初始化点阵、猫、障碍块）
     * @author created by ChenMeiYu at 2021-1-26 11:34, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param
     * @return void
     * @throws
     */
    private void initGame(){
        // 初始化点阵
        dotsArr = new DotVO[COL_NUM][ROW_NUM];
        for(int col=0;col<COL_NUM;col++){
            for(int row=0;row<ROW_NUM;row++){
                dotsArr[col][row] = new DotVO(col,row);
                // Log.v(TAG,"点阵："+dotsArr[col][row].toString(),null);
            }
        }

        // 初始化猫的位置
        cat = getDot((int)(COL_NUM/2),(int)(ROW_NUM/2));
        cat.setStatus(DotStatusEnum.IN);
        // Log.v(TAG,"猫："+cat.toString(),null);


        // 初始化路障
        for(int i=0;i<BLOCK_NUM;){
            DotVO dotRandom = getDotRandom();
            if(dotRandom.getStatus() == DotStatusEnum.ON){
                dotRandom.setStatus(DotStatusEnum.OFF);
                i++;
                // Log.v(TAG,"路障："+dotRandom.toString(),null);
            }
        }
    }

    /** Description: 获取点阵中的点
     * @author created by ChenMeiYu at 2021-1-26 13:57, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param col: int 点所在的列数
     * @param row：int 点所在的行数
     * @return cn.lxrm.demo.crazycat.DotVO 点阵中的点(如果给定的坐标落在点阵之外，则返回null)
     * @throws
     */
    private DotVO getDot(int col,int row){
        if(col<0 || row<0 || col > COL_NUM - 1 || row > ROW_NUM - 1) {
            // 坐标在地图边界之外，返回null
            return null;
        }
        return dotsArr[col][row];
    }

    /** Description: 获取点阵中的随机点
     * @author created by ChenMeiYu at 2021-1-26 14:04, v1.0
     */
    private DotVO getDotRandom(){
//        int x0 = new Random(COL_NUM).nextInt();
//        int y0 = new Random(ROW_NUM).nextInt();
        int x0 =(int) (Math.random()*1000%COL_NUM);
        int y0 =(int) (Math.random()*1000%ROW_NUM);
        return getDot(x0,y0);
    }

    /** Description: 响应屏幕触摸时间
     * @author created by ChenMeiYu at 2021-1-27 10:18, v1.0
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // MotionEvent.ACTION_UP：抬起，手离开屏幕
        if(event.getAction() == MotionEvent.ACTION_UP){
            // Toast.makeText(getContext(),event.getX()()+","+event.getY()(),Toast.LENGTH_SHORT).show();
            // 将屏幕坐标（像素坐标）转化为游戏坐标（(col,row)）
            // 游戏坐标：当前行
            int currRow = (int) (event.getY() / CELL_LEN);
            // 游戏坐标：当前列
            int currCol = (int) ((event.getX() - getOffset(currRow)) / CELL_LEN);
            // Toast.makeText(getContext(),"当前坐标："+currCol+","+currRow,Toast.LENGTH_SHORT).show();
            if(isOutOfBoundary(currCol,currRow)){
//                 Toast.makeText(getContext(),"当前坐标："+currCol+","+currRow+"；处于边界之外。",Toast.LENGTH_SHORT).show();
                // 边界之外
                // 游戏界面重置
                initGame();
                reDraw();

//                testGetNeighbor();
//                testGetDistance();
            } else {
                // 地图内部的点，将用户点击的点设置为障碍
                DotVO dot = getDot(currCol, currRow);
                if(dot.getStatus() == DotStatusEnum.ON){
                    dot.setStatus(DotStatusEnum.OFF);
                    // 猫移动
                    catMove();
                }
                // 更新游戏界面
                reDraw();
            }
        }
        return true;
    }

    /** Description: 猫移动
     * @author created by ChenMeiYu at 2021-1-27 9:06, v1.0
     */
    private void catMove() {
        moveToNext();
        updateGameRst();
    }

    /** Description: 更新游戏状态
     * @author created by ChenMeiYu at 2021-1-27 9:13, v1.0
     */
    private void updateGameRst() {
        if(isAtEdge(cat)){
            showGameOver();
        } else if(isSurrounded(cat)){
            showWin();
        }
    }

    /** Description: 判断猫有没有被围住
     * @author created by ChenMeiYu at 2021-1-27 9:14, v1.0
     * @param cat：DotVO 猫所在的点
     * @return boolean 猫有没有被围住，true表示完全围住
     * @throws
     */
    private boolean isSurrounded(DotVO cat) {
        // 如果猫在边界，说明没有被包围，返回false
        if(isAtEdge(cat)){
            return false;
        }
        // 猫不在边界，就要看六个方向上可移动距离
        for (DirectionEnum dir: DirectionEnum.values()) {
            int distance = getDistance(cat,dir);
            if(distance!=0){
                return false;
            }
        }
        return true;
    }

    /** Description: 展示-闯关成功
     * @author created by ChenMeiYu at 2021-1-27 9:10, v1.0
     */
    private void showWin() {
        Toast.makeText(getContext(),"恭喜，闯关成功！",Toast.LENGTH_SHORT).show();
    }

    /** Description: 展示-闯关失败
     * @author created by ChenMeiYu at 2021-1-27 9:08, v1.0
     */
    private void showGameOver() {
        Toast.makeText(getContext(),"很遗憾，闯关失败。再来一次吧！",Toast.LENGTH_SHORT).show();
        initGame();
        reDraw();
    }

    /** Description: 判断当前点是不是边界点[TODO-功能描述]
     * @author created by ChenMeiYu at 2021-1-26 17:27, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param dot：DotVO 猫所在的点
     * @return boolean
     * @throws
     */
    private boolean isAtEdge(DotVO dot) {
        int currCol = dot.getCol();
        int currRow = dot.getRow();
        return currCol == 0 || currRow == 0 || currCol == COL_NUM - 1 || currRow == ROW_NUM - 1;
    }

    /** Description: 获取节点在特定方向的临点对象
     * @author created by ChenMeiYu at 2021-1-27 10:06, v1.0
     * @param dot：DotVO 点
     * @param direction：DirectionNum 方向
     * @return cn.lxrm.demo.crazycat.DotVO 邻点（如果点前点在边界上，那么返回的邻点可能为null）
     * @throws
     */
    private DotVO getNeighbor(DotVO dot, DirectionEnum direction){
        switch (direction){
            case LEFT:
                return getDot(dot.getCol()-1,dot.getRow());
            case LEFT_UP:
                if(dot.getRow() %2 == 0){
                    // 奇数行
                    return getDot(dot.getCol()-1,dot.getRow()-1);
                }else{
                    // 偶数行
                    return getDot(dot.getCol(),dot.getRow()-1);
                }
            case RIGHT_UP:
                if(dot.getRow() %2 == 0){
                    // 奇数行
                    return getDot(dot.getCol(),dot.getRow()-1);
                }else{
                    // 偶数行
                    return getDot(dot.getCol()+1,dot.getRow()-1);
                }
            case RIGHT:
                return getDot(dot.getCol()+1,dot.getRow());
            case RIGHT_DOWN:
                if(dot.getRow() %2 == 0){
                    // 奇数行
                    return getDot(dot.getCol(),dot.getRow()+1);
                }else{
                    // 偶数行
                    return getDot(dot.getCol()+1,dot.getRow()+1);
                }
            case LRFT_DOWN:
                if(dot.getRow() %2 == 0){
                    // 奇数行
                    return getDot(dot.getCol()-1,dot.getRow()+1);
                }else{
                    // 偶数行
                    return getDot(dot.getCol(),dot.getRow()+1);
                }
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    /** Description: 返回猫在某个方向上的可移动距离
     *      当该方向前面有路障时，可移动距离为负
     *      该方向前面无路障，可以直达地图边缘时，可移动距离为正
     * @author created by ChenMeiYu at 2021-1-27 11:13, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param cat：DotVO 猫当前所在位置
     * @param direction：DirectionEnum 移动方向
     * @return int 猫在相应方向上的可移动距离
     * @throws
     */
    private int getDistance(DotVO cat, DirectionEnum direction){
        int distance = 0;
        DotVO currDot = cat;
        while(true){
//            Log.v(TAG,"DIS="+distance,null);
            DotVO nextDot = getNeighbor(currDot, direction);
            // 猫本身就在边界，相应方向上不再有邻点
            if(nextDot == null){
                return 0;
            }
            // 猫在相应方向上的邻点在地图内
            if(nextDot.getStatus() == DotStatusEnum.ON){
                ++distance;
                currDot = nextDot;
//                currDot.setStatus(DotStatusEnum.IN);
//                reDraw();
            }else if(nextDot.getStatus() == DotStatusEnum.OFF){
                // 邻点是障碍块
                return distance*-1;
            }
            // 猫在相应方向上的邻点恰好在边界上
            if(isAtEdge(nextDot)){
                return distance;
            }
        }
    }

    /** Description: 猫移动到下一个位置
     * @author created by ChenMeiYu at 2021-1-27 15:14, v1.0
     */
    private void moveToNext() {
//        // 随机选取可移动方向
//        moveToNextRandom();
//
//        // 最优路线(第一版)
//        moveToNextOptimizedV1();
        // 最优路线(第二版)
        moveToNextOptimizedV2();
    }

    /** Description: 猫移动到下一个点（随机选取可移动方向）
     * @author created by ChenMeiYu at 2021-1-27 15:05, v1.0
     */
    private void moveToNextRandom() {
        Vector<DirectionEnum> vector = new Vector<>();
        for (DirectionEnum direction: DirectionEnum.values()) {
            int distance = getDistance(cat, direction);
            if(distance!=0){
                vector.add(direction);
            }
        }
        if(vector.size() == 1){
            moveOneStep(vector.get(0));
        }else if(vector.size()>1){
            int random = (int) ((Math.random() * 1000) % vector.size());
            moveOneStep(vector.get(random));
        }
    }
    /** Description: 猫移动到下一个位置（最优算法-第一版）
     * @author created by ChenMeiYu at 2021-1-27 15:37, v1.0
     */
    private void moveToNextOptimizedV1() {
        // 直达边界的路径
        DirectionEnum directionPositiveToMove = null;
        int minDistancePositive = 999;
        // 如果有直达边界的路径，则该标志位被设为true，否则该标志位为false
        boolean flagPositive = false;
        // 障碍物路径
        DirectionEnum directionNegativeToMove = null;
        int maxDistanceNegative = 0;
        for (DirectionEnum direction: DirectionEnum.values()) {
            int distance = getDistance(cat, direction);
            if(distance>0){
                flagPositive = true;
                if(distance<minDistancePositive){
                    minDistancePositive = distance;
                    directionPositiveToMove = direction;
                }
            }else if(distance<0){
                if(distance<maxDistanceNegative){
                    maxDistanceNegative = distance;
                    directionNegativeToMove = direction;
                }
            }
        }
        if(flagPositive){
            // 有直达边界的路径
            moveOneStep(directionPositiveToMove);
        }else if(directionNegativeToMove!=null){
            moveOneStep(directionNegativeToMove);
        }
    }

    /** Description: 猫移动到下一个位置（最优算法-第一版）
     * @author created by ChenMeiYu at 2021-1-27 15:37, v1.0
     */
    private void moveToNextOptimizedV2() {
        HashMap<DirectionEnum, Integer> map = new HashMap<>();
        for (DirectionEnum direction: DirectionEnum.values()) {
            int distance = getDistance(cat, direction);
            if(distance!=0){
                map.put(direction,distance);
            }
        }
        Log.v(TAG,"排序前："+map.toString(),null);
        Set<Map.Entry<DirectionEnum, Integer>> entries = map.entrySet();
        ArrayList<Map.Entry<DirectionEnum, Integer>> list = new ArrayList<>(entries);
        Collections.sort(list, new Comparator<Map.Entry<DirectionEnum, Integer>>() {
            @Override
            public int compare(Map.Entry<DirectionEnum, Integer> o1, Map.Entry<DirectionEnum, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Log.v(TAG,"排序后："+list.toString(),null);
        if(list.size()>0){
            if(list.get(0).getValue()>0){
                // 有可以直达边界的路径，则选用距离边界最短的路径逃跑
                int idx = 1;
                while(idx<list.size()){
                    if(list.get(idx).getValue()<0){
                       break;
                    }
                    ++idx;
                }
                moveOneStep(list.get(idx-1).getKey());
                Log.v(TAG,"最佳逃跑方向："+list.get(idx-1).getKey()+","+list.get(idx-1).getValue());
            }else{
                // 没有可以直达边界的路径，所有方向上都有路障，选用距离路障最远的方向逃跑
                moveOneStep(list.get(list.size()-1).getKey());
                Log.v(TAG,"最佳逃跑方向："+list.get(list.size()-1).getKey()+","+list.get(list.size()-1).getValue());
            }
        }
    }

    /** Description: 猫按照指定方向移动一步
     * @author created by ChenMeiYu at 2021-1-27 14:19, v1.0
     * @param directionPositiveToMove：DirectionEnum 指定方向
     * @return void
     * @throws
     */
    private void moveOneStep(DirectionEnum directionPositiveToMove) {
        cat.setStatus(DotStatusEnum.ON);
        cat = getNeighbor(cat,directionPositiveToMove);
        cat.setStatus(DotStatusEnum.IN);
    }

    /** Description: 获取当前行点阵的水平偏移量（单位：像素）
     * @author created by ChenMeiYu at 2021-1-26 16:50, v1.0
     */
    private float getOffset(int currRow){
        float offset = 0;
        if(currRow % 2 != 0){
            offset = CELL_LEN / 2;
        }
        return offset;
    }

    /** Description: 判断当前坐标是否处于边界之外
     * @author created by ChenMeiYu at 2021-1-26 16:27, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param currCol：int 当前坐标所在的列
 * @param currRow：int 当前坐标所在的行
     * @return boolean 是否处于边界之外，true表示改点处于边界之外
     * @throws
     */
    private boolean isOutOfBoundary(int currCol,int currRow) {
        return currCol >= COL_NUM || currRow >= ROW_NUM;
    }

    // ----------------------- 测试 start ---------------------
    // 测试getNeighbor()接口
    private void testGetNeighbor(){
        for(int i=0;i<10;i++){
            DotVO dotRandom = getDotRandom();
            if(dotRandom!=null){
                dotRandom.setStatus(DotStatusEnum.IN);
                for (DirectionEnum direc: DirectionEnum.values()) {
                    DotVO neighbor = getNeighbor(dotRandom, direc);
                    if(neighbor!=null){
                        neighbor.setStatus(DotStatusEnum.OFF);
                    }
                }
            }
        }
        reDraw();
    }
    // 测试getDistance()接口
    private void testGetDistance(){
        for (DirectionEnum direc: DirectionEnum.values()) {
            int distance = getDistance(cat, direc);
//            Log.v(TAG,direc+":"+distance,null);
        }
        initGame();
        reDraw();
    }
}
