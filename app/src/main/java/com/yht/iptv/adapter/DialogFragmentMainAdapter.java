package com.yht.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.TitleBean;

import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */

public class DialogFragmentMainAdapter extends BaseAdapter {


    private final LayoutInflater inflater;
    private List<TitleBean> titleList;

    public DialogFragmentMainAdapter(Context context, List<TitleBean> titleList) {

        inflater = LayoutInflater.from(context);

        this.titleList = titleList;

    }

    @Override
    public int getCount() {
        return titleList.size();
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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_dialog_fragment_listview,parent,false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }else{
             holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(titleList.get(position).getName());
        return convertView;
    }

    private class ViewHolder{
        TextView tv_title;
    }

}
