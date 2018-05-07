package com.yht.iptv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yht.iptv.R;
import com.yht.iptv.model.MediaDetailBean;
import com.yht.iptv.utils.ShowImageUtils;

import java.util.List;

/**
 * Created by Q on 2017/10/16.
 */

public class MediaRecyclerViewAdapter extends RecyclerView.Adapter<MediaRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<MediaDetailBean> beanList;
    private LayoutInflater inflater;


    public MediaRecyclerViewAdapter(Context mContext, List<MediaDetailBean> beanList) {
        this.mContext = mContext;
        this.beanList = beanList;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.media_recycler_item_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.media_item_img = (ImageView) view.findViewById(R.id.media_item_img);
        holder.rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (beanList.size() > 0 && beanList.get(0) != null) {
            int pos = position % beanList.size();
//            if(pos < 3){
//                if(position < 3) {
//                    holder.rl_main.setVisibility(View.INVISIBLE);
//                }else{
//                    pos += 3;
//                    holder.rl_main.setVisibility(View.VISIBLE);
//                    if (beanList.get(position % beanList.size()).getFileUpload() != null) {
//                        ShowImageUtils.showImageView(mContext, beanList.get(pos).getFileUpload().getPath(), holder.media_item_img);
//                    }
//                }
//            }else{
//                holder.rl_main.setVisibility(View.VISIBLE);
//                if (beanList.get(position % beanList.size()).getFileUpload() != null) {
//                    ShowImageUtils.showImageView(mContext, beanList.get(pos).getFileUpload().getPath(), holder.media_item_img);
//                }
//            }

            if (beanList.get(position % beanList.size()).getFileUpload() != null) {
                ShowImageUtils.showImageView(mContext, beanList.get(pos).getFileUpload().getPath(), holder.media_item_img);
            }
//            if(beanList.get(position % beanList.size()).getName() == null){
//                holder.rl_main.setVisibility(View.INVISIBLE);
//            }else {
//                holder.rl_main.setVisibility(View.VISIBLE);
//                if (beanList.get(position % beanList.size()).getFileUpload() != null) {
//                    ShowImageUtils.showImageView(mContext, beanList.get(position % beanList.size()).getFileUpload().getPath(), holder.media_item_img);
//                }
//            }
        }

    }

    @Override
    public int getItemCount() {
        if (beanList.size() == 1 || beanList.size() == 0) {
            return beanList.size();
        }
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView media_item_img;
        RelativeLayout rl_main;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
