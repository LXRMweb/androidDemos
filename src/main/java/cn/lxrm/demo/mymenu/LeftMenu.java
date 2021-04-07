package cn.lxrm.demo.mymenu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appdemo01.R;

/**
 * README: 左菜单
 *
 * @author created by ChenMeiYu at 2021-3-1 17:27, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class LeftMenu extends Fragment{
    public final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View leftMenu = inflater.inflate(R.layout.mymenu_leftmenu, container, false);
        leftMenu.findViewById(R.id.leftmenu_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"左菜单：点击了左菜单的按钮");
            }
        });
        return leftMenu;
    }
}
