package com.yht.iptv.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yht.iptv.R;
import com.yht.iptv.model.HotelFoodListBean;
import com.yht.iptv.utils.ShowImageUtils;

import java.util.List;

/**
 * Created by Q on 2017/10/23.
 */

public class HotelFoodViewPagerAdapter extends PagerAdapter {

    private Context context;
    private OnItemClickListener onItemClick;
    private List<HotelFoodListBean> dataList;
    private LayoutInflater inflater;
    private View mCurrentView;

    public HotelFoodViewPagerAdapter(Context context, List<HotelFoodListBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemCLickLitener(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getCount() {
        if (dataList.size()== 1) {
            return 1;
        }
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.hotel_food_view_pager_item, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.item_img);
        ImageView iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);

        if (dataList.get(position).getFileUpload() != null) {
            ShowImageUtils.showImageView(context,dataList.get(position).getFileUpload().getPath(),image);
//            Glide.with(context).load(dataList.get(position).getFileUpload().getPath()).into(image);
        } else {

        }
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //接口传递
                onItemClick.onItemClick(position);
            }
        });

//        ShowImageUtils.showImageView(context,R.drawable.hotel_food_mask_bg,iv_bg);

        container.addView(view);
        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }


}
