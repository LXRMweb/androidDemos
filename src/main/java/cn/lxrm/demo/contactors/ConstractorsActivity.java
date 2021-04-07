package cn.lxrm.demo.contactors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appdemo01.R;

import java.util.ArrayList;
import java.util.List;

import cn.lxrm.demo.checkpermission.CheckPermission;

public class ConstractorsActivity extends AppCompatActivity {
    ListView contactorsListView;
    List<ContactorItemVO> contractors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constractors);
        contactorsListView = findViewById(R.id.contactorsListView);
        checkPermission();
        // 读取通讯录，获取联系人列表
        contractors = new GetContractors().getContractors(this);
        /** TODO: ChenMeiYu, 2021-1-26 8:57
         * 预计处理时间:
         * description: 测试数据
         */
        contractors = test(contractors);
        contactorsListView.setAdapter(new ContactorItemAdapter(contractors,this));
        // 为ListView添加点击事件
        contactorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactorItemVO contactorItemVO = contractors.get(position);
                Toast.makeText(ConstractorsActivity.this,
                        contactorItemVO.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /** TODO: ChenMeiYu, 2021-1-26 8:58
     * 预计处理时间:
     * description: 测试数据
     */
    private List<ContactorItemVO> test(List<ContactorItemVO> contractors) {
        List<ContactorItemVO> rst = new ArrayList<>();
        for (int i=0;i<100;i++) {
            rst.addAll(contractors);
        }
        return rst;
    }


    // ------------ 动态获取权限 start -------------------
    public void checkPermission(){
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            //获取应用的Target版本
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        String[] PermissionString= {Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Build.VERSION.SDK_INT是获取当前手机版本 Build.VERSION_CODES.M为6.0系统
            //如果系统>=6.0
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                //第 1 步: 检查是否有相应的权限
                boolean isAllGranted = checkPermissionAllGranted(PermissionString);
                if (isAllGranted) {
                    //Log.e("err","所有权限已经授权！");
                    return;
                }
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(this,
                        PermissionString, 1);
            }
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                //Log.e("err","权限"+permission+"没有授权");
                return false;
            }
        }
        return true;
    }

    // 申请权限结果返回处理
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                // 所有的权限都授予了
                Log.e("err","权限都授权了");
                // 读取通讯录，获取联系人列表
                List<ContactorItemVO> contractors = new GetContractors().getContractors(this);
                contactorsListView.setAdapter(new ContactorItemAdapter(contractors,this));
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                //容易判断错
                //MyDialog("提示", "某些权限未开启,请手动开启", 1) ;
                String[] PermissionString= {Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS};
                new CheckPermission(this, (Activity) this).showDialog(this);
            }
        }
    }
    // ------------ 动态获取权限 end -------------------
}