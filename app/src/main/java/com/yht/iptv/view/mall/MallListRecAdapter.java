package com.yht.iptv.view.mall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MallTitleInfo;

import java.util.List;

/**
 * Created by admin on 2017/3/28.
 */
public class MallListRecAdapter extends RecyclerView.Adapter<MallListRecAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private List<MallTitleInfo> mallTitleInfos;

    public MallListRecAdapter(Context mContext, List<MallTitleInfo> mallTitleInfos) {
        inflater = LayoutInflater.from(mContext);
        this.mallTitleInfos = mallTitleInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.adapter_recyclerview_gift_tab,parent,false);
        View view = inflater.inflate(R.layout.new_adapter_recyclerview_gift_tab,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.type_text = (TextView) view.findViewById(R.id.type_text);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.type_text.setText(mallTitleInfos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mallTitleInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView type_text;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
