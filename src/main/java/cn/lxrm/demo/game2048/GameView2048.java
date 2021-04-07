package cn.lxrm.demo.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * README: 2048网格游戏view
 *
 * @author created by ChenMeiYu at 2021-3-5 10:19, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class GameView2048 extends GridLayout {

    private Context context;
    private final String TAG = this.getClass().getSimpleName();
    // 卡片宽度
    int cardWidth = 0;
    // 卡片阵列大小：CARD_ARRAY_SIZE*CARD_ARRAY_SIZE
    private final int CARD_ARRAY_SIZE = 4;
    // 初始随机数个数
    private final int INIT_RANDOM_NUM = 2;
    // 卡片（CARD_ARRAY_SIZE*CARD_ARRAY_SIZE的数组）
    private Card[][] cards = new Card[CARD_ARRAY_SIZE][CARD_ARRAY_SIZE];
    // 空点数组（没有数字的卡片坐标数组）
    private List<Point> emptyCards = new ArrayList<>();

    public GameView2048(Context context) {
        super(context);
        initGameView(context);
    }

    public GameView2048(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView(context);
    }

    public GameView2048(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView(context);
    }

    private void initGameView(Context context) {
        this.context = context;

        // 设置GridView列数：CARD_ARRAY_SIZE列
        setColumnCount(CARD_ARRAY_SIZE);

        handleTouchEvent();
    }

    /**
     * Description: 适配不同尺寸的硬件设备
     *
     * @author created by ChenMeiYu at 2021-4-6 14:15, v1.0
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*获取屏幕尺寸的较小者，使得横屏、竖屏状态下卡片尺寸一致
          -10，表示希望卡片有一定的外边距，使其不要充满整个屏幕
          /4 得到每个卡片的宽度
         */
        this.cardWidth = (Math.min(w, h) - 10) / CARD_ARRAY_SIZE;
        redraw();
    }

    /**
     * Description: 添加卡片类
     *
     * @author created by ChenMeiYu at 2021-4-6 14:07, v1.0
     */
    private void addCards() {
        // 初始化卡片数组
        Card card;
        for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                card = new Card(getContext());
                // 将卡片组价添加至本组件（GameView048组件）中
                addView(card, this.cardWidth, this.cardWidth);
                cards[x][y] = card;
            }
        }

        // 卡片数组中添加两个随机数字
        addRandomNum();
        System.out.println("初始卡片阵列");
        printCardsArray();
    }

    /**
     * Description: 在卡片阵列中随机选取一张空卡片，添加一个随机数字（2/4）
     * 其中，添加的数值为2的概率是0.9，为4的概率是0.1
     *
     * @author created by ChenMeiYu at 2021-4-6 16:01, v1.0
     */
    private void addRandomNumOnOneRandomEmptyCard() {
        // 清空历史数据
        emptyCards.clear();
        // 遍历卡片数组，找出所有的空卡片（将已经含有数字的卡片排除在外，防止两次生成的随机数添加在同一张卡片中）
        for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                // 判断是否空卡片
                if (cards[x][y].getNum() <= 0) {
                    emptyCards.add(new Point(x, y));
                }
            }
        }
        if (emptyCards.size()<1){
            gameOver();
            return;
        }
        // 从空卡片中随机取出一张
        Point randomPoint = emptyCards.remove((int) (Math.random() * emptyCards.size()));
        // 给上面随机选出的一张空卡片赋值（2/4）, 其中为4的概率是0.1，为2的概率是0.9
        cards[randomPoint.x][randomPoint.y].setNum(Math.random() > 0.9 ? 4 : 2);
    }

    /** Description: 游戏结束
     * @author created by ChenMeiYu at 2021-4-7 15:05, v1.0
     */
    private void gameOver() {
//        Toast.makeText(getContext(),"很遗憾，闯关失败！", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(getContext())
                .setTitle("游戏结束")
                .setMessage("最终得分：" + MainGame2048Activity.getMainGame2048Activity().getScore())
                .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                }).show();
    }

    private void redraw() {
        addCards();
    }

    /** Description: 清空卡片阵列
     * @author created by ChenMeiYu at 2021-4-7 15:14, v1.0
     */
    private void resetGame() {
        // 清空积分
        MainGame2048Activity.getMainGame2048Activity().clearScore();
        // 清空卡片阵列数字
        for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                cards[x][y].setNum(0);
            }
        }
        // 添加初始随机数
        addRandomNum();
    }

    private void addRandomNum() {
        // 卡片数组中添加两个随机数字
        for (int i = 0; i < INIT_RANDOM_NUM; i++) {
            addRandomNumOnOneRandomEmptyCard();
        }
    }
    // ---------- start 处理屏幕触摸事件（上/下/左/右滑动事件）----------

    /**
     * Description: 处理屏幕触摸事件（上、下、左、右滑动事件）
     *
     * @author created by ChenMeiYu at 2021-3-5 10:34, v1.0
     */
    private void handleTouchEvent() {
        setOnTouchListener(new OnTouchListener() {
            /**
             * 手指落下的位置
             */
            private float X0, Y0;
            /**
             * 手指滑动位移
             */
            private float dX, dY;
            /**
             * 最小滑动距离（只要滑动距离大于MIN_OFFSET时，才被视作有效滑动事件）
             */
            private final int MIN_OFFSET = 20;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        X0 = event.getX();
                        Y0 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        dX = event.getX() - X0;
                        dY = event.getY() - Y0;
                        if (Math.abs(dX) > Math.abs(dY)) {
                            // 水平滑动
                            if (dX < -MIN_OFFSET) {
                                Log.d(TAG, "to left");
                                handleLeftSwipEvent();
                            } else if (dX > MIN_OFFSET) {
                                Log.d(TAG, "to right");
                                handleRightSwipEvent();
                            }
                        } else {
                            // 垂直滑动
                            if (dY < -MIN_OFFSET) {
                                Log.d(TAG, "to up");
                                handleUPSwipEvent();
                            } else if (dY > MIN_OFFSET) {
                                Log.d(TAG, "to down");
                                handleDownSwiptEvent();
                            }
                        }
                        // 生成一个新的随机数
                        addRandomNumOnOneRandomEmptyCard();
                        break;
                    default:
                        // 返回false，告诉OS自己没有“消费”掉该事件
                        return false;
                }
                // 返回true的目的是，告诉OS自己“消费”了该事件
                return true;
            }
        });
    }

    /**
     * Description: 处理“向下滑动事件”
     *
     * @author created by ChenMeiYu at 2021-4-6 11:13, v1.0
     */
    private void handleDownSwiptEvent() {
        /*处理向下滑动事件，规则：
         *   1. 遍历每一列,针对每一列做相同的处理
         *   2. 针对某一列卡片从下往上遍历：
         *           “相邻”卡片上数字相同时，合并两张卡片
         *           上方数字卡片填充下方空卡片
         * */
        // 针对每一列做相同的操作
        for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
            Card[] arr = new Card[CARD_ARRAY_SIZE];
            for (int x = CARD_ARRAY_SIZE - 1; x >= 0; x--) {
                arr[CARD_ARRAY_SIZE - 1 - x] = cards[x][y];
            }
            handleArray(arr);
            for (int x = 0; x < arr.length; x++) {
                cards[CARD_ARRAY_SIZE-1-x][y] = arr[x];
            }
        }
    }

    /** Description: 处理数组，从头至尾遍历数组，将两个“弱相邻”的相同的卡片“合并”，并将“合并”后的数组左对齐
     * @author created by ChenMeiYu at 2021-4-7 11:18, v1.0
     */
    private void handleArray(Card[] cardArr) {
        Log.v(TAG,"处理前：" + Arrays.toString(cardArr));
        // 遍历数组，两个“弱相邻”的相同的卡片“合并”
        for (int i = 0; i < cardArr.length; i++) {
            if (cardArr[i].getNum() <= 0) continue;
            // 某行/列遇到第一张非空卡片，遍历该非空卡片右/上/左/下方剩余的卡片
            for (int j = i + 1; j < cardArr.length; j++) {
                if (cardArr[j].getNum() <= 0) continue;
                // 该非空卡片右/上/左/下方剩余的卡片中找到一张非空卡片
                if (cardArr[i].equals(cardArr[j])) {
                    cardArr[i].setNum(cardArr[i].getNum() * 2);
                    cardArr[j].setNum(0);
                    // 增加积分
                    MainGame2048Activity.getMainGame2048Activity().addScore(cardArr[i].getNum());
                } else {
                    i = j - 1;
                    break;
                }
            }
        }
        Log.v(TAG,"弱相邻合并：" + Arrays.toString(cardArr));
        // 遍历数组，开头对齐
        for (int i = 0; i < cardArr.length; i++) {
            if (cardArr[i].getNum()>0) {
                continue;
            }
            // 从左至右遍历数组，直到遇到第一张空卡片
            for (int j = i+1; j < cardArr.length; j++) {
                if (cardArr[j].getNum()<=0) {
                    continue;
                }
                // 遍历空卡片之后的数组，直到遇到第一张非空卡片
                cardArr[i].setNum(cardArr[j].getNum());
                cardArr[j].setNum(0);
                break;
            }
        }
        Log.v(TAG,"左对齐：" + Arrays.toString(cardArr));
    }

    /**
     * Description: 处理“向上滑动事件”
     *
     * @author created by ChenMeiYu at 2021-4-6 11:13, v1.0
     */
    private void handleUPSwipEvent() {
        for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
            Card[] arr = new Card[CARD_ARRAY_SIZE];
            for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
                arr[x] = cards[x][y];
            }
            handleArray(arr);
            for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
                cards[x][y] = arr[x];
            }
        }
    }

    /**
     * Description: 处理“向右滑动事件”
     *
     * @author created by ChenMeiYu at 2021-4-6 11:13, v1.0
     */
    private void handleRightSwipEvent() {
        for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
            Card[] arr = new Card[CARD_ARRAY_SIZE];
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                arr[y] = cards[x][CARD_ARRAY_SIZE-1-y];
            }
            handleArray(arr);
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                cards[x][y] = arr[CARD_ARRAY_SIZE-1-y];
            }
        }
    }

    /**
     * Description: 处理“向左滑动事件”
     *
     * @author created by ChenMeiYu at 2021-4-6 11:13, v1.0
     */
    private void handleLeftSwipEvent() {
        printCardsArray();
        for (int x = 0; x < CARD_ARRAY_SIZE; x++) {
            Card[] arr = new Card[CARD_ARRAY_SIZE];
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                arr[y] = cards[x][y];
            }
            handleArray(arr);
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                cards[x][y] = arr[y];
            }
        }
        printCardsArray();
    }

    /** Description: 输出卡片阵列
     * @author created by ChenMeiYu at 2021-4-7 14:28, v1.0
     */
    private void printCardsArray() {
        for (int  x= 0; x < CARD_ARRAY_SIZE; x++) {
            for (int y = 0; y < CARD_ARRAY_SIZE; y++) {
                System.out.print(cards[x][y].getNum()+"\t");
            }
            System.out.println("");
        }
    }


}
