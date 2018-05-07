package com.yht.iptv.view.mall;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MallGoodsDetailInfo;

import java.util.List;

/**
 * Created by admin on 2017/3/28.
 */
public class MallGoodsTypeAdapter extends RecyclerView.Adapter<MallGoodsTypeAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> itemList;
    private int choosePosition;

    public MallGoodsTypeAdapter(Context mContext, List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> itemList, int choosePosition) {
        inflater = LayoutInflater.from(mContext);
        this.itemList = itemList;
        this.choosePosition = choosePosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_recyclerview_mall_goods_type,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        holder.ll_name = (LinearLayout) view.findViewById(R.id.ll_name);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(choosePosition == position){
            holder.ll_name.setBackgroundResource(R.drawable.mall_goods_detail_frame);
        }else {
            holder.ll_name.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.tv_name.setText(itemList.get(position).getItem());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private LinearLayout ll_name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void setChoosePosition(int position){
        choosePosition = position;
    }

    public int getChoosePosition(){
        return choosePosition;
    }
}
