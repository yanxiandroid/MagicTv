//package com.yht.iptv.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.jude.rollviewpager.RollPagerView;
//import com.jude.rollviewpager.adapter.LoopPagerAdapter;
//import com.yht.iptv.R;
//import com.yht.iptv.model.PictureAdInfo;
//import com.yht.iptv.utils.ShowImageUtils;
//
//import java.util.List;
//
//
///**
// * Created by Q on 2018/1/5.
// */
//
//public class LoopAdapter extends LoopPagerAdapter {
//
//    private Context context;
//    private List<PictureAdInfo> pictureAdInfos;
//
//
//    public LoopAdapter(RollPagerView viewPager, Context context, List<PictureAdInfo> pictureAdInfos) {
//        super(viewPager);
//        this.context = context;
//        this.pictureAdInfos = pictureAdInfos;
//    }
//
//
//    @Override
//    public View getView(ViewGroup container, int position) {
//        ImageView view = new ImageView(container.getContext());
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        try {
//            ShowImageUtils.showImageView(context, pictureAdInfos.get(position % pictureAdInfos.size()).getFileUpload().getPath(), view);
//        } catch (Exception e) {
//            ShowImageUtils.showImageView(context, R.drawable.main_bg, view);
//        }
//
//        return view;
//    }
//
//    @Override
//    public int getRealCount() {
//        return pictureAdInfos.size();
//    }
//}
