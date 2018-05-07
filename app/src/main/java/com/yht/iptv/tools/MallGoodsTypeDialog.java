package com.yht.iptv.tools;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.open.androidtvwidget.view.MainUpView;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.model.MallGoodsDetailInfo;
import com.yht.iptv.view.mall.MallGoodsTypeAdapter;

import java.util.List;

/**
 * Created by admin on 2017/7/20.
 */

public class MallGoodsTypeDialog extends AppCompatDialog implements TvRecyclerView.OnItemListener {

    private Context context;
    private TvRecyclerView tr_type1;
    private TvRecyclerView tr_type2;
    private MainUpView mainUpView;
    private TvRecyclerViewBridge mRecyclerViewBridge;
    private View oldView;

    private OnCancelListener listener;
    private MallGoodsTypeAdapter type1Adapter;
    private MallGoodsTypeAdapter type2Adapter;
    private List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> item1;
    private List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> item2;
    private OnRecyclerViewClick onRecyclerViewClick;
    private int pos1;
    private int pos2;

    public interface OnRecyclerViewClick{
        void onItemClick(int tag, int position);
    }


    public MallGoodsTypeDialog(Context context) {
        super(context, R.style.menuDialogDefine);
        this.context = context;
    }

    public MallGoodsTypeDialog(Context context, int theme) {
        super(context, theme);
    }

    public MallGoodsTypeDialog(Context context, OnCancelListener listener, OnRecyclerViewClick onRecyclerViewClick, List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> item1, List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> item2, int pos1, int pos2) {
        super(context, R.style.menuDialogDefine);
        this.context = context;
        this.listener = listener;
        this.item1 = item1;
        this.item2 = item2;
        this.onRecyclerViewClick = onRecyclerViewClick;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_mall_goods_type);
        window.setGravity(Gravity.START | Gravity.TOP);

        this.setOnCancelListener(listener);

        tr_type1 = (TvRecyclerView) window.findViewById(R.id.tr_type1);
        tr_type2 = (TvRecyclerView) window.findViewById(R.id.tr_type2);
        TextView tv_title1 = (TextView) window.findViewById(R.id.tv_title1);
        TextView tv_title2 = (TextView) window.findViewById(R.id.tv_title2);
        mainUpView = (MainUpView) window.findViewById(R.id.mainUpView);
        type1Adapter = new MallGoodsTypeAdapter(context,item1,pos1);
        type2Adapter = new MallGoodsTypeAdapter(context,item2,pos2);
        tr_type1.setHasFixedSize(true);
        tr_type1.setInterceptKeyEvent(true);
        tr_type1.setSelectedItemAtCentered(true);
        tr_type1.setOnItemListener(this);

        tr_type2.setHasFixedSize(true);
        tr_type2.setInterceptKeyEvent(true);
        tr_type2.setSelectedItemAtCentered(true);
        tr_type2.setOnItemListener(this);

        tr_type1.setAdapter(type1Adapter);
        tr_type2.setAdapter(type2Adapter);

        initViewMove();

        showPosition(window);

        tv_title1.setText(item1.get(0).getSpec_name());

        if(item2 != null){
            tv_title2.setText(item2.get(0).getSpec_name());
        }

        tr_type1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tr_type1.setSelection(0);
            }
        },300);

        if(item2 == null){
            tr_type2.setVisibility(View.GONE);
        }

    }

    private void initViewMove() {

        mainUpView.setEffectBridge(new TvRecyclerViewBridge());
        mRecyclerViewBridge = (TvRecyclerViewBridge) mainUpView.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.gift_frame_new);
        mainUpView.setDrawUpRectPadding(new Rect(getDimension(R.dimen.w_18), getDimension(R.dimen.h_18), getDimension(R.dimen.w_18), getDimension(R.dimen.h_18)));
        mRecyclerViewBridge.setTranDurAnimTime(0);
    }

    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        mRecyclerViewBridge.setUnFocusView(itemView);
        mRecyclerViewBridge.setVisibleWidget(false);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        mRecyclerViewBridge.setFocusView(itemView,oldView,1.0f);
        oldView = itemView;
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {
        mRecyclerViewBridge.setFocusView(itemView,oldView,1.0f);
        oldView = itemView;
    }

    @Override
    public void onItemClick(TvRecyclerView parent, final View itemView, final int position) {
        if(parent.getId() == R.id.tr_type1){
            Log.e("TvRecyclerView","type1-----");
            int lastPosition = type1Adapter.getChoosePosition();
            if(lastPosition != position) {
                type1Adapter.setChoosePosition(position);
                type1Adapter.notifyItemChanged(lastPosition);
                type1Adapter.notifyItemChanged(position);
                tr_type1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tr_type1.setSelection(position);
                    }
                }, 100);
                onRecyclerViewClick.onItemClick(parent.getId(), position);
            }
        }else if(parent.getId() == R.id.tr_type2){
            Log.e("TvRecyclerView","type2-----");
            int lastPosition = type2Adapter.getChoosePosition();
            if(lastPosition != position) {
                type2Adapter.setChoosePosition(position);
                type2Adapter.notifyItemChanged(lastPosition);
                type2Adapter.notifyItemChanged(position);
                tr_type2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tr_type2.setSelection(position);
                    }
                },100);
                onRecyclerViewClick.onItemClick(parent.getId(),position);
            }
        }
    }

    public int getDimension(int id) {
        return (int) context.getResources().getDimension(id);
    }

    /**
     * 设置显示位置
     */
    private void showPosition(Window window) {
        WindowManager.LayoutParams wl = window.getAttributes();
        //设置X轴最右边Y轴最上面
        wl.x = getDimension(R.dimen.w_835);
        wl.y = getDimension(R.dimen.h_708);
        // 以下这两句是为了保证按钮可以垂直满屏
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        this.onWindowAttributesChanged(wl);
    }

}
