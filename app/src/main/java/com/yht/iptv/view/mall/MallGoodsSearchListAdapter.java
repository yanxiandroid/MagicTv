package com.yht.iptv.view.mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MallGoodsListInfo;
import com.yht.iptv.utils.ShowImageUtils;

import java.util.List;

/**
 * Created by admin on 2017/7/24.
 */

public class MallGoodsSearchListAdapter extends BaseAdapter {


    private Context context;
    List<MallGoodsListInfo.GoodsListBean> items;
    private final LayoutInflater inflater;

    private int currentPos;

    public MallGoodsSearchListAdapter(List<MallGoodsListInfo.GoodsListBean> items, Context context) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
        currentPos = -1;
    }

    @Override
    public int getCount() {
        return items.size();
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
            convertView = inflater.inflate(R.layout.adapter_mall_goods_search_list, null);
            holder.rl_title_type1 = (RelativeLayout) convertView.findViewById(R.id.rl_title_type1);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);

            holder.tv_title_type2 = (TextView) convertView.findViewById(R.id.tv_title_type2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (currentPos != -1) {
            if (currentPos == position) {
                showHideType(holder.rl_title_type1, holder.tv_title_type2);
                holder.tv_name.setText(items.get(position).getTitle());
                holder.tv_price.setText("价格：" + items.get(position).getMarketprice());
                holder.tv_description.setText("简介：" + items.get(position).getDescription());

                ShowImageUtils.showImageView(context,items.get(position).getThumb(),holder.iv_photo);

            } else {
                showHideType(holder.tv_title_type2, holder.rl_title_type1);
                holder.tv_title_type2.setText(items.get(position).getTitle());
            }
        } else {
            showHideType(holder.tv_title_type2, holder.rl_title_type1);
            holder.tv_title_type2.setText(items.get(position).getTitle());
        }

        return convertView;
    }


    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int pos) {
        currentPos = pos;
    }


    private class ViewHolder {
        private RelativeLayout rl_title_type1;
        private ImageView iv_photo;
        private TextView tv_name;
        private TextView tv_price;
        private TextView tv_description;
        private TextView tv_title_type2;
    }

    private void showHideType(View view1, View view2) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
    }
}
