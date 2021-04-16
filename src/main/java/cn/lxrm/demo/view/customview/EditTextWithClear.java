package cn.lxrm.demo.view.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.appdemo01.R;

/**
 * README: 自定义view（自定义EditText）
 *          可编辑输入框尾部添加“清除”图标，
 *          录入内容不为空时展示“清除”图标
 *          录入内容为空时不展示“清除”图标
 *          点击“清除”图标，清空已录入内容
 * <p>
 * <p>
 * 题外话：sdk自带的输入框EditText没有一键清除所录数据的功能，如果想要实现该功能，需要：
 *          1. 设置 android:drawableEnd="@drawable/ic_baseline_clear_24"，在尾部添加图标
 *          2. 为相应EditText实例添加addTextChangedListener, 在监听器中实现“聚焦展示，失焦隐藏” && 点击图标清空录入内容的功能
 *      总结：如果使用EditText实现这种需求，那么每次你想要使用这种录入框的时候，你都需要按照上述步骤编写一遍代码
 *          这种方案有明显的缺点，代码重复度高 && 不利于后期维护
 *          所以，还有其他更好的方法来实现上述需求么
 *          答案很明显：有，使用自定义view就可以实现上述需求，且自定义view将会更好，因为自定义view有更好的封装性和可复用性
 * 附录：
 *      1. 为自定义视图添加专属属性
 *          https://developer.android.google.cn/training/custom-views/create-view#java
 *      2.
 * @author created by ChenMeiYu at 2021-4-14 19:54, v1.0
 */
public class EditTextWithClear extends androidx.appcompat.widget.AppCompatEditText {
//    private Drawable drawableClearIcon;
//    private Context context;

    public EditTextWithClear(Context context) {
        super(context);
//        initView(context);
    }

//    private void initView(Context context) {
//        this.context = context;
//        drawableClearIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return toggleClearIcon(event)?true:super.onTouchEvent(event);
//    }

//    @Override
//    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
//        super.onTextChanged(text, start, lengthBefore, lengthAfter);
//        showClearIcon(text);
//
//    }
//
//    /** Description: 展示/隐藏 “清除”图标
//     * @author created by Meiyu Chen at 2021-4-14 20:49, v1.0
//     */
//    private void showClearIcon(CharSequence text) {
//        // 输入框中录入的内容不为空时，展示“clear图标”，为空时不展示
//        Drawable icon = text.equals("") ? null : drawableClearIcon;
//        // 给EditText添加图标（在上下左右四个方向上添加图标；WithIntrinsicBounds表示使用图标自定义的大小）
//        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null);
//    }
//
//    /** Description: 点击“清除”图标，清空已录入内容
//     * @author created by Meiyu Chen at 2021-4-14 20:55, v1.0
//     * @return boolean 触摸事件是否已被消费：true-已消费，false-未被消费
//     */
//    private boolean toggleClearIcon(MotionEvent event) {
//        String text = getText().toString();
//        if (text.equals("")) {
//            // 录入内容为空时，不展示“清除‘图标，不用处理该图标的触摸事件
//            return true;
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                if (event.getX() > getWidth() - drawableClearIcon.getIntrinsicWidth() - 20
//                    && event.getX() < getWidth() + 20
//                    && event.getY() > getHeight() / 2 - drawableClearIcon.getIntrinsicHeight() / 2 -20
//                    && event.getY() < getHeight() / 2 + drawableClearIcon.getIntrinsicHeight() / 2 + 20) {
//                    setText("");
//                }
//                return true;
//            default:
//                return false;
//        }
//    }
}
