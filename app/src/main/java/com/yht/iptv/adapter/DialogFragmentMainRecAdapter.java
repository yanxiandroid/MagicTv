package com.yht.iptv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.TitleBean;

import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */

public class DialogFragmentMainRecAdapter extends RecyclerView.Adapter<DialogFragmentMainRecAdapter.ViewHolder> {


    private final LayoutInflater inflater;
    private List<TitleBean> titleList;
    private View view;
    private boolean isFirst;
    private Context context;

    public DialogFragmentMainRecAdapter(Context context, List<TitleBean> titleList) {

        this.context = context;

        inflater = LayoutInflater.from(context);

        isFirst = true;

        this.titleList = titleList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.adapter_dialog_fragment_listview,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isFirst && position == 2){
            isFirst = false;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) context.getResources().getDimension(R.dimen.h_140);
            view.setLayoutParams(params);
        }
        holder.tv_title.setText(titleList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
