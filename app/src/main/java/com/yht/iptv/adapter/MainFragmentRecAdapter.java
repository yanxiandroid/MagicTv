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
import com.yht.iptv.model.MainUIBean;

import java.util.List;

/**
 * Created by admin on 2017/10/13.
 * yanxi
 */

public class MainFragmentRecAdapter extends RecyclerView.Adapter<MainFragmentRecAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<MainUIBean> mainUIBeenList;

//    public MainFragmentRecAdapter(Context context, String[] titles, int[] imageResources) {
//
//        inflater = LayoutInflater.from(context);
//        this.titles = titles;
//        this.imageResources = imageResources;
//
//    }

    public MainFragmentRecAdapter(Context context, List<MainUIBean> mainUIBeenList) {
        inflater = LayoutInflater.from(context);
        this.mainUIBeenList = mainUIBeenList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_rec_main_fragment,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        holder.rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
        holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mainUIBeenList.get(position).getName() == null){
            holder.rl_main.setVisibility(View.INVISIBLE);
        }else{
            holder.rl_main.setVisibility(View.VISIBLE);
            holder.tv_title.setText(mainUIBeenList.get(position).getName());
            holder.iv_icon.setImageResource(mainUIBeenList.get(position).getIcon());
        }

    }

    @Override
    public int getItemCount() {
        return mainUIBeenList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView iv_icon;
        RelativeLayout rl_main;

        ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
