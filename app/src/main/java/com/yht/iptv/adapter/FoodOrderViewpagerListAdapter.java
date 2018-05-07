package com.yht.iptv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.FoodOrderInfo;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class FoodOrderViewpagerListAdapter extends BaseAdapter {

    private List<FoodOrderInfo.DinesListBean> dinesListBeen;
    private final LayoutInflater inflater;

    public FoodOrderViewpagerListAdapter(Context context, List<FoodOrderInfo.DinesListBean> dinesListBeen) {
        this.dinesListBeen = dinesListBeen;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dinesListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.food_order_viewpager_list_item, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(dinesListBeen.get(position).getName());
        holder.tv_num.setText(dinesListBeen.get(position).getPrice() + "元" + "  *  " + dinesListBeen.get(position).getCount());

        return convertView;
    }

    private class ViewHolder{
        TextView tv_title;
        TextView tv_num;
    }
}
