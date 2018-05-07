package com.yht.iptv.view.mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.iptv.R;

import java.util.List;

/**
 * Created by admin on 2016/5/19.
 */
public class SearchGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private final LayoutInflater inflater;

    public SearchGridViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView =inflater.inflate(R.layout.gridview_search_item,null);
            holder.mKb = (TextView) convertView.findViewById(R.id.tv_kb);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mKb.setText(list.get(position));

        return convertView;
    }

    private class ViewHolder{
        TextView mKb;
    }

}
