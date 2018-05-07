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

public class DialogFragmentListFoodOrderAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private boolean isFirst;
    private List<FoodOrderInfo> foodOrderInfos;

    public DialogFragmentListFoodOrderAdapter(Context context, List<FoodOrderInfo> foodOrderInfos) {
        inflater = LayoutInflater.from(context);
        isFirst = true;
        this.foodOrderInfos = foodOrderInfos;
    }

    @Override
    public int getCount() {
        return foodOrderInfos.size();
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
            convertView = inflater.inflate(R.layout.listview_message_push, parent, false);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText("款项："+foodOrderInfos.get(position).getOrderNum());
        holder.tv_time.setText(foodOrderInfos.get(position).getCreateTime());
        if(isFirst && position == 0){
            isFirst = false;
            holder.tv_title.setTextColor(Color.parseColor("#ffffff"));
            holder.iv_select.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    private class ViewHolder{
        ImageView iv_select;
        TextView tv_title;
        TextView tv_time;
    }


}
