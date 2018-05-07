package com.yht.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.BusTimetableBean;
import com.yht.iptv.utils.LanguageUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class BusViewpagerListAdapter extends BaseAdapter {

    private List<BusTimetableBean> busTimetableBeen;
    private final LayoutInflater inflater;
    private Context context;

    public BusViewpagerListAdapter(Context context, List<BusTimetableBean> dinesListBeen) {
        this.busTimetableBeen = dinesListBeen;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return busTimetableBeen.size();
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.bus_timetable_viewpager_list_item, parent, false);
            holder.tv_station = (TextView) convertView.findViewById(R.id.tv_station);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.tv_station.setText(busTimetableBeen.get(position).getStartStation() + "-" + busTimetableBeen.get(position).getEndStation());
            holder.tv_station.setSelected(true);
            holder.tv_time.setText(busTimetableBeen.get(position).getStartTime() + "/" + busTimetableBeen.get(position).getDuration());
            holder.tv_name.setText(busTimetableBeen.get(position).getNumber());
            holder.tv_name.setSelected(true);
            if (LanguageUtils.getInstance().getLanguage(context).equals("en")) {
                holder.tv_price.setText("￥ "+numberFormat(Float.parseFloat(busTimetableBeen.get(position).getPrice())));
            }else {
                holder.tv_price.setText(numberFormat(Float.parseFloat(busTimetableBeen.get(position).getPrice())) + "元");
            }

        } catch (Exception e) {

        }


        return convertView;
    }

    private class ViewHolder {
        TextView tv_station;
        TextView tv_time;
        TextView tv_name;
        TextView tv_price;
    }

    private String numberFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }
}
