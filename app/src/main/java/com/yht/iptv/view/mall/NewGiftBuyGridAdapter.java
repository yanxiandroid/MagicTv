package com.yht.iptv.view.mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MallGoodsListInfo;
import com.yht.iptv.utils.ShowImageUtils;

import java.util.List;


public class NewGiftBuyGridAdapter extends BaseAdapter {
    private Context mContext;
    private  LayoutInflater mInflater;
    private List<MallGoodsListInfo.GoodsListBean> mallGoodsListInfos;

    public NewGiftBuyGridAdapter(Context mContext, List<MallGoodsListInfo.GoodsListBean> mallGoodsListInfos) {
        this.mContext = mContext;
        this.mallGoodsListInfos = mallGoodsListInfos;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mallGoodsListInfos.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.new_gift_buy_grid_item_view,null);
            viewHolder.gift_img = (ImageView) convertView.findViewById(R.id.gift_buy_grid_item_img);
            viewHolder.name_text = (TextView) convertView.findViewById(R.id.gift_buy_grid_item_name_text);
            viewHolder.price_text = (TextView) convertView.findViewById(R.id.gift_buy_grid_item_price_text);
            viewHolder.sales_volume_text = (TextView) convertView.findViewById(R.id.gift_buy_grid_item_sales_volume_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ShowImageUtils.showImageView(mContext,mallGoodsListInfos.get(position).getThumb(),viewHolder.gift_img);

        viewHolder.name_text.setText(mallGoodsListInfos.get(position).getTitle());
        viewHolder.price_text.setText("￥" + mallGoodsListInfos.get(position).getMarketprice());
        viewHolder.sales_volume_text.setText("月销" + mallGoodsListInfos.get(position).getSales() + "笔");
        return convertView;
    }

    private class ViewHolder {
        ImageView gift_img;//商品的图片
        TextView name_text;//商品名称
        TextView price_text;//商品价格
        TextView sales_volume_text;//商品销量
    }
}
