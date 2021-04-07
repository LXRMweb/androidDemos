package cn.lxrm.demo.contactors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appdemo01.R;

import java.util.ArrayList;
import java.util.List;

public class ContactorItemAdapter extends BaseAdapter {
    List<ContactorItemVO> contactorList = new ArrayList<>();
    Context context;

    public ContactorItemAdapter(List<ContactorItemVO> contactorList) {
        this.contactorList = contactorList;
    }
    public ContactorItemAdapter(List<ContactorItemVO> contactorList,Context context) {
        this(contactorList);
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.contactorList.size();
    }

    @Override
    public ContactorItemVO getItem(int position) {
        return this.contactorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** Description: [TODO-功能描述]
     * @author created by ChenMeiYu at 2021-1-25 17:32, v1.0
     *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
     *          [TODO-修改内容概述]
     * @param position
     * @param convertView: View 这个参数用于将之前加载好的布局进行缓存，以便于之后可以进行重用。借助于这个参数，可以实现ListView的性能优化哦
     * @param parent
     * @return android.view.View
     * @throws
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.adapter_contractor_item,null);
//        TextView phonetv = layout.findViewById(R.id.phoneContactor);
//        TextView nametv = layout.findViewById(R.id.nameContactor);
//        phonetv.setText(getItem(position).getPhone());
//        nametv.setText(getItem(position).getName());
        ViewHolder holder;
        if(convertView == null){
            // 使用LayoutInflater去加载布局
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_contractor_item, null);
            holder = new ViewHolder();
            holder.nametv = convertView.findViewById(R.id.nameContactor);
            holder.phonetv = convertView.findViewById(R.id.phoneContactor);
            convertView.setTag(holder);
        } else {
            // 如果之前加载过该布局，则使用缓存的该布局实例
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nametv.setText(getItem(position).getName());
        holder.phonetv.setText(getItem(position).getPhone());
        return convertView;
//        return layout;
    }

    private static class ViewHolder {
        TextView phonetv;
        TextView nametv;
    }

}
