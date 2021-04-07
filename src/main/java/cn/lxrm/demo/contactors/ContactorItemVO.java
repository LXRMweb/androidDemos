package cn.lxrm.demo.contactors;

import androidx.annotation.NonNull;

/** README: 联系人实体类
 * @author created by ChenMeiYu at 2021-1-25 10:24, v1.0
 *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 *          [TODO-修改内容概述]
 */
public class ContactorItemVO {
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        String rst = "姓名：" + this.name + ", 电话：" + this.phone;
        return rst;
    }
}
