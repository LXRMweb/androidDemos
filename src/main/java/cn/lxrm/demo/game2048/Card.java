package cn.lxrm.demo.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.appdemo01.R;

import java.util.Objects;

/**
 * README: 2048游戏中的卡片类
 *
 * @author created by ChenMeiYu at 2021-4-6 13:54, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class Card extends FrameLayout {

    private int num;
    private TextView tv;

    public Card(@NonNull Context context) {
        super(context);

        initTextView(context);
        setNum(0);
    }

    private void initTextView(@NonNull Context context) {
        tv = new TextView(context);
        tv.setTextSize(32);
        // 将tv组件放在Card组件的中间
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(getResources().getColor(R.color.white_game2048));
        // 布局参数：参数都是-1，表示期望本组件充满整个父容器
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.setMargins(10,10,0,0);
        // 将文本组件添加到Card布局中
        addView(tv,layoutParams);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        tv.setText(num<=0?"":(num + ""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return num == card.num;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }

    @Override
    public String toString() {
        return "Card{" +
                "num=" + num +
                '}';
    }
}
