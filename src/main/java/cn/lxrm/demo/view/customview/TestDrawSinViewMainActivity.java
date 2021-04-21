package cn.lxrm.demo.view.customview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.appdemo01.R;

/** Description: 测试自定义view（DrawSinView-绘制正弦函数动画)
 * @author created by Meiyu Chen at 2021-4-19 16:12, v1.0
 *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 *          [TODO-修改内容概述]
 */
public class TestDrawSinViewMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_draw_sin_view_main);
        DrawSinView drawSinView = (DrawSinView) findViewById(R.id.drawSinView);
        // 为自定义视图添加activity生命周期监听器，将自定义视图和activity生命周期绑定在一起
        getLifecycle().addObserver(drawSinView);
    }
}