package com.yht.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.HotelPhoneInfo;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class HotelPhoneViewpagerListAdapter extends BaseAdapter {

    private List<HotelPhoneInfo> hotelPhoneInfos;
    private final LayoutInflater inflater;

    public HotelPhoneViewpagerListAdapter(Context context, List<HotelPhoneInfo> dinesListBeen) {
        this.hotelPhoneInfos = dinesListBeen;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return hotelPhoneInfos.size();
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
            convertView = inflater.inflate(R.layout.hotel_phone_viewpager_list_item, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(hotelPhoneInfos.get(position).getName());
        holder.tv_title.setSelected(true);
        holder.tv_phone.setText(hotelPhoneInfos.get(position).getPhoneNum());

        return convertView;
    }

    private class ViewHolder{
        TextView tv_title;
        TextView tv_phone;
    }
}
