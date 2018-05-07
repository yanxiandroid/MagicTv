package com.yht.iptv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.FoodCarInfo;
import com.yht.iptv.utils.ShowImageUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Q on 2017/10/25.
 */

public class HotelFoodCarRecyclerViewAdapter extends RecyclerView.Adapter<HotelFoodCarRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<FoodCarInfo> dataList;

    public HotelFoodCarRecyclerViewAdapter(Context context, List<FoodCarInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_car_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.car_item_img = (ImageView) view.findViewById(R.id.car_item_img);
        holder.rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
        holder.car_item_price = (TextView) view.findViewById(R.id.car_item_price);
        holder.car_item_num = (TextView) view.findViewById(R.id.car_item_num);
        holder.car_item_name = (TextView) view.findViewById(R.id.car_item_name);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(dataList.get(position).getFoodId() == null){
            holder.rl_main.setVisibility(View.INVISIBLE);
        }else {
            holder.rl_main.setVisibility(View.VISIBLE);
            if (dataList.size() > 0 && dataList.get(position) != null) {
                if (dataList.get(position).getImage() != null) {
                    ShowImageUtils.showImageView(context, dataList.get(position).getImage(), holder.car_item_img);
//                Glide.with(context).load(dataList.get(position).getImage()).into(holder.car_item_img);
                }
                float price = Float.parseFloat(dataList.get(position).getPrice());
                holder.car_item_price.setText("￥:" + numberFormat(price));
                holder.car_item_num.setText("*" + dataList.get(position).getNum());
                holder.car_item_name.setText(dataList.get(position).getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView car_item_img;
        private TextView car_item_price;
        private TextView car_item_num;
        private TextView car_item_name;
        private RelativeLayout rl_main;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    private String numberFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }
}
