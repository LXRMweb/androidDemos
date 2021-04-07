package cn.lxrm.demo.mymenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

public class TestMyMenuActivity extends FragmentActivity {
    LeftMenu leftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainUI mainUI = new MainUI(this);
        MainUIV2 mainUI = new MainUIV2(this);
        setContentView(mainUI);
        // 添加左菜单页面
        leftMenu = new LeftMenu();
        getSupportFragmentManager().beginTransaction().add(MainUIV2.LEFT_MENU_ID,leftMenu).commit();
    }
}
