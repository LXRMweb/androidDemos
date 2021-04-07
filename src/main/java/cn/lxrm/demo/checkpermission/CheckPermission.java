package cn.lxrm.demo.checkpermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/** Description: 动态请求权限
 * @author created by ChenMeiYu at 2021-1-25 14:23, v1.0
 *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 *          [TODO-修改内容概述]
 */
public class CheckPermission {
    private Context context;
    private Activity activity;

    public CheckPermission(Context context, Activity activity) {
        this.context = context;
    }

    public void checkPermission()
    {
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;//获取应用的Target版本
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
                ActivityCompat.requestPermissions(activity,
                        PermissionString, 1);
            }
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                //Log.e("err","权限"+permission+"没有授权");
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showDialog(Activity activity){
        String[] permissions= {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
        /**
         * 申请相关权限
         * @param activity          Activity对象
         * @param permissions       请求的权限组
         * @param requestCode       本次请求码
         */
        ActivityCompat.requestPermissions(activity, permissions, 1);
        //minSdkVersion >= 23 可以直接使用
        activity.requestPermissions(permissions, 1);
    }
}
