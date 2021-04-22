package cn.lxrm.demo.view.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.appdemo01.R;

import java.util.Objects;

/**
 * README: 自定义view（自定义EditText）
 * 可编辑输入框尾部添加“清除”图标，
 * 聚焦状态 && 录入内容不为空时展示“清除”图标
 * 失焦状态 || 聚焦状态但是录入内容为空时不展示“清除”图标
 * 点击“清除”图标，清空已录入内容
 * <p>
 * <p>
 * 题外话：sdk自带的输入框EditText没有一键清除所录数据的功能，如果想要实现该功能，需要：
 * 1. 设置 android:drawableEnd="@drawable/ic_baseline_clear_24"，在尾部添加图标
 * 2. 为相应EditText实例添加addTextChangedListener, 在监听器中实现“聚焦展示，失焦隐藏” && 点击图标清空录入内容的功能
 * 总结：如果使用EditText实现这种需求，那么每次你想要使用这种录入框的时候，你都需要按照上述步骤编写一遍代码
 * 这种方案有明显的缺点，代码重复度高 && 不利于后期维护
 * 所以，还有其他更好的方法来实现上述需求么
 * 答案很明显：有，使用自定义view就可以实现上述需求，且自定义view将会更好，因为自定义view有更好的封装性和可复用性
 * 题外话：可以为自定义视图添加专属属性
 * step1. 在res/values/attrs.xml中声明专属属性
 * 如，为EditTextWithClear2组件声明一个名称为clearIcon的属性
 * step2. 在layout.xml中编辑自定义组件，为其专属属性赋值
 * 如，编辑EditTextWithClear2组件的clearIcon属性，为其赋值：
 * app:clearIcon="@drawable/ic_baseline_clear_24"
 * step3，在自定义视图class中获取其专属属性
 * 在构造函数中调用获取专属的方法，获取专属属性
 * 如：getCustomedViewAttrs(context,attrs);
 *
 * * 参考资料：
 *  *      【视频教程】（https://www.bilibili.com/video/BV1NK411A72N/?spm_id_from=333.788.recommend_more_video.2）
 * @author created by ChenMeiYu at 2021-4-14 19:54, v1.0
 */
public class EditTextWithClear2 extends androidx.appcompat.widget.AppCompatEditText {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * 清除图标（放在EditText的尾部）
     */
    private Drawable drawableClearIcon = null;

    public EditTextWithClear2(Context context) {
        super(context);
//        initView(context);
    }

    public EditTextWithClear2(Context context, AttributeSet attrs) {
        super(context, attrs);
        getCustomedViewAttrs(context, attrs);
//        initView(context);
    }

    public EditTextWithClear2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getCustomedViewAttrs(context, attrs);
//        initView(context);
    }

    /**
     * Description: 自定义视图的专属属性-step3，在自定义视图class中获取其专属属性
     * 通过 XML 布局(即layout.xml)创建视图时，XML 标记中的所有属性都会从资源包读取，并作为 AttributeSet 传递到视图的构造函数中。
     * 虽然可以直接从 AttributeSet 读取值，但这样做有一些弊端：
     * 不解析属性值中的资源引用
     * 不应用样式
     * 为了避免上述弊端，请改为将 AttributeSet 传递给 obtainStyledAttributes()。此方法会传回一个 TypedArray 数组，其中包含已解除引用并设置了样式的值
     *
     * @author created by Meiyu Chen at 2021-4-16 16:32, v1.0
     */
    private void getCustomedViewAttrs(Context context, AttributeSet attrs) {
        /* layout.xml中使用了自定义组件EditTextWithClear2，并且为该组件的某些属性设定了值，
         * layout.xml加载完成之后，这些属性会被传给EditTextWithClear2的构造函数,赋值给构造函数的 AttributeSet attrs属性
         */
        TypedArray customedViewAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditTextWithClear2,
                0, 0);

        try {
            // 获取layout.xml中为自定义组件EditTextWithClear2设置的clearIcon属性的值，并且在没有获取到相应值时为赋值为-1
            int resourceId = customedViewAttrs.getResourceId(R.styleable.EditTextWithClear2_clearIcon, -1);
            Log.d(TAG, "clearIcon::ResourceId=" + resourceId);
            if (resourceId != -1) {
                /* layout.xml中为自定义组件设置了clearIcon属性时,获取属性值，加载相应的资源（icon图标）并赋值给drawableClearIcon
                        如果layout.xml中没有为自定义组件配置clearIcon属性，则不加载“清除”图标
                 */
                drawableClearIcon = ContextCompat.getDrawable(context, resourceId);
            }
//            mShowText = customedViewAttrs.getBoolean(R.styleable.PieChart_showText, false);
//            textPos = customedViewAttrs.getInteger(R.styleable.PieChart_labelPosition, 0);
        } finally {
            // 请注意，TypedArray 对象是共享资源，必须在使用后回收。
            customedViewAttrs.recycle();
        }
    }

//    private void initView(Context context) {
//        drawableClearIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24);
//    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        // 录入框中录入的内容改变时，展示/隐藏“清除”图标
        showClearIcon(text);
    }

    /**
     * Description: 展示/隐藏 “清除”图标
     *
     * @author created by Meiyu Chen at 2021-4-14 20:49, v1.0
     */
    private void showClearIcon(CharSequence text) {
        // 输入框聚焦状态 && 录入的内容不为空时，展示“clear图标”，失焦状态 or 聚焦但录入内容为空时不展示
        Drawable icon = isFocused() && !("".equals(text.toString())) ? drawableClearIcon : null;
        // 给EditText添加图标（在上下左右四个方向上添加图标；WithIntrinsicBounds表示使用图标自定义的大小）
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        // 失焦状态隐藏图标；聚焦状态下判断内容是否为空，非空时展示图标
        showClearIcon(getText());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return toggleClearIcon(event);
    }

    /**
     * Description: 点击“清除”图标，清空已录入内容
     *
     * @return boolean 触摸事件是否已被消费：true-已消费，false-未被消费
     * @author created by Meiyu Chen at 2021-4-14 20:55, v1.0
     */
    private boolean toggleClearIcon(MotionEvent event) {
        // 触摸屏幕，手指抬起之后才去做相应操作
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (drawableClearIcon != null) {
                /* 如何判断是否点击了“清除”图标？
                 *    屏幕触摸位置在“图标”上或图标附近时，即认定是点击了“清除”图标
                 *    getWidth获取的是当前EditText组件的宽度
                 *    drawableClearIcon.getIntrinsicWidth() 是图标的固有宽度
                 *    +/-20 是为了提升用户使用体验，只要点击了图标附近，都认为是点击了图标
                 * */
                if (event.getX() > getWidth() - drawableClearIcon.getIntrinsicWidth() - 20
                        && event.getX() < getWidth() + 20
                        && event.getY() > getHeight() / 2 - drawableClearIcon.getIntrinsicHeight() / 2 - 20
                        && event.getY() < getHeight() / 2 + drawableClearIcon.getIntrinsicHeight() / 2 + 20) {
                    Objects.requireNonNull(getText()).clear();
                }
            }
        }
        super.performClick();
        return super.onTouchEvent(event);
    }
}
