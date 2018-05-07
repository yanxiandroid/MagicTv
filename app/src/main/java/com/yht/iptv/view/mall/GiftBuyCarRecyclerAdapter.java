package com.yht.iptv.view.mall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MallShopCarInfo;
import com.yht.iptv.utils.ShowImageUtils;

import java.util.List;


public class GiftBuyCarRecyclerAdapter extends RecyclerView.Adapter<GiftBuyCarRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<MallShopCarInfo> dataList;
    private final LayoutInflater mInflater;

    public GiftBuyCarRecyclerAdapter(Context context, List<MallShopCarInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public GiftBuyCarRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.new_gift_car_rec_item_view, parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.car_rec_item_img = (ImageView) view.findViewById(R.id.car_rec_item_img);
        holder.specifications_text = (TextView) view.findViewById(R.id.car_rec_item_specifications_text);
        holder.name_text = (TextView) view.findViewById(R.id.car_rec_item_name_text);
        holder.price_text = (TextView) view.findViewById(R.id.car_rec_item_price_text);
        holder.num_text = (TextView) view.findViewById(R.id.car_rec_item_num_text);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GiftBuyCarRecyclerAdapter.ViewHolder holder, int position) {
        if (dataList.get(position) != null) {
            holder.name_text.setText(dataList.get(position).getGoodsTitle());
            ShowImageUtils.showImageView(context, dataList.get(position).getThumb(), holder.car_rec_item_img);
            holder.price_text.setText("￥" + dataList.get(position).getGoodsPrice());
            holder.num_text.setText("×" + dataList.get(position).getTotal());
            if(dataList.get(position).getTypeName1() != null && dataList.get(position).getTypeName2() != null) {
                holder.specifications_text.setText("规格：" + dataList.get(position).getTypeName1() + dataList.get(position).getTypeName2());
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView car_rec_item_img;
        TextView specifications_text;
        TextView name_text;
        TextView price_text;
        TextView num_text;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }


}
