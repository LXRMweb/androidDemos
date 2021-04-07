package cn.lxrm.demo.contactors;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/** Description: 读取通讯录，获取联系人列表
 * @author created by ChenMeiYu at 2021-1-25 10:33, v1.0
 *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 *          [TODO-修改内容概述]
 */
public class GetContractors {
    private final String TAG = this.getClass().getSimpleName();
    private List<ContactorItemVO> contactors = new ArrayList<ContactorItemVO>();
    public List<ContactorItemVO> getContractors(Context context){
         Log.v(TAG,"获取联系人信息：开始.......");
         // 安卓OS提供了通讯录访问接口，通讯录是存在数据库中的，访问数据库的接口返回的都是Cursor
         Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
         while (cursor.moveToNext()){
             ContactorItemVO itemVO = new ContactorItemVO();
             itemVO.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
             itemVO.setPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
             contactors.add(itemVO);
             Log.v(TAG,itemVO.toString());
         }
         return contactors;
     }
}
