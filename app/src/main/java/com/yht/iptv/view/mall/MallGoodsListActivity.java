package com.yht.iptv.view.mall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MallGoodsListInfo;
import com.yht.iptv.model.MallLoginInfo;
import com.yht.iptv.model.MallTitleInfo;
import com.yht.iptv.presenter.MallListPresenter;
import com.yht.iptv.presenter.MallTitlePresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.CustomGridViewTv;
import com.yht.iptv.tools.NoticeDialog;
import com.yht.iptv.tools.ScanCodeAttentionDialog;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2017/7/18.
 */

public class MallGoodsListActivity extends BaseActivity implements TvRecyclerView.OnItemListener, View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, ViewTreeObserver.OnGlobalFocusChangeListener, IPresenterMallBase<List<MallTitleInfo>> {
    private ImageView search_img, shopping_cart_img;
    private TextView hotel_name_text;
    private TvRecyclerView recyclerView;
    private CustomGridViewTv gridView;
    private MainUpView mainUpView;
    private TextView type_text;
    private ImageView iv_image_bg;
    private RelativeLayout relativeLayout;
    private EffectNoDrawBridge bridget;
    private View oldView;

    private final int REQUEST_DETAIL = 0;
    private int currentPos = -1;
    private boolean isRefresh;


    //    private List<GiftTypeBean> giftTypeBeanList;//商品类型
    private List<MallTitleInfo> mallTitleInfos;//商品类型
    private List<MallGoodsListInfo.GoodsListBean> mallGoodsListInfos;//商品类型
    private MallListRecAdapter mRecAdapter;
    //    private List<GiftDetailBean> giftDetailBeanList;//商品类型详情
//    private GiftDetailPresenter giftDetailPresenter;
    private MallListPresenter mallListPresenter;
    private NewGiftBuyGridAdapter mGriAdapter;

    private int currentPage = 1;
    private final int pageSize = 20;
    private int totalPage = 1;
    private boolean isLoading;
    private MainPageInfo info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        recordStart(Constants.SHOPPING);
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_START);
        bundle.putString("behaviour", Constants.SHOPPING);
        ServiceUtils.startService(PageRecordService.class, bundle);
        setContentView(R.layout.activity_gift_buy_new);
        initView();
        initData();
        initRecyclerView();
        initGridView();
        initBridge();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
            ServiceUtils.startService(PageRecordService.class, bundle);
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 60 * 1000);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, 60 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(runnable);
    }


    private void initView() {
        search_img = (ImageView) findViewById(R.id.activity_gift_buy_search_img);
        shopping_cart_img = (ImageView) findViewById(R.id.activity_gift_buy_shopping_cart_img);
        hotel_name_text = (TextView) findViewById(R.id.activity_gift_buy_hotel_name_text);
        recyclerView = (TvRecyclerView) findViewById(R.id.activity_gift_buy_recycler_view);
        gridView = (CustomGridViewTv) findViewById(R.id.activity_gift_buy_grid_view);
        mainUpView = (MainUpView) findViewById(R.id.mainUpView);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_gift_buy_rl);
        ImageView iv_bg = (ImageView) findViewById(R.id.iv_bg);
        search_img.setVisibility(View.GONE);
        shopping_cart_img.setVisibility(View.GONE);
        recyclerView.setNextFocusUpId(R.id.activity_gift_buy_shopping_cart_img);
        recyclerView.setInterceptKeyEvent(true);
        recyclerView.setSelectedItemAtCentered(true);
        recyclerView.setOnItemListener(this);
        relativeLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        search_img.setOnClickListener(this);
        shopping_cart_img.setOnClickListener(this);
        search_img.setOnFocusChangeListener(this);
        shopping_cart_img.setOnFocusChangeListener(this);

        mallTitleInfos = new ArrayList<>();
        mallGoodsListInfos = new ArrayList<>();

        ShowImageUtils.showImageView(this, R.drawable.activity_gift_buy_bg_new, iv_bg);

    }

    private void initData() {
        info = Constants.mainPageInfo;
        if (info == null) {
            ToastUtils.showShort("主页信息为空.请返回主页重新获取!");
            finish();
            return;
        }
//        if (info != null) {
//            hotel_name_text.setText(info.getZhName() + "商城");
//        }
//
//        GiftTypePresenter giftTypePresenter = new GiftTypePresenter(this, this);
//        giftDetailPresenter = new GiftDetailPresenter(this, new GiftDetailListener());
//        giftTypePresenter.request(this);

        MallTitlePresenter titlePresenter = new MallTitlePresenter(this, this);
        mallListPresenter = new MallListPresenter(this, new GiftDetailListener());
        titlePresenter.request();

    }


    private void initRecyclerView() {

        mRecAdapter = new MallListRecAdapter(this, mallTitleInfos);
        recyclerView.setAdapter(mRecAdapter);

    }

    private void initGridView() {
        mGriAdapter = new NewGiftBuyGridAdapter(this, mallGoodsListInfos);
        gridView.setSmoothScrollbarEnabled(true);
        gridView.setOnItemSelectedListener(this);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(mGriAdapter);
    }

    private void initBridge() {
        mainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
        mainUpView.setUpRectResource(R.drawable.gift_frame_new); // 设置移动边框的图片.
        mainUpView.setDrawUpRectPadding(new Rect(getDimension(R.dimen.w_21), getDimension(R.dimen.h_21), getDimension(R.dimen.w_21), -getDimension(R.dimen.h_21))); // 边框图片设置间距.
        bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
        bridget.setTranDurAnimTime(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_gift_buy_search_img:
//                Intent in1 = new Intent(MallGoodsListActivity.this, MallGoodsSearchActivity.class);
//                startActivity(in1);
                showDialog();
                break;
            case R.id.activity_gift_buy_shopping_cart_img:
                if (Constants.mainPageInfo.getPaymentSetting().getMall() == 1) {
                    Intent in = new Intent(MallGoodsListActivity.this, MallGoodsShopCarActivity.class);
                    Bundle bundle = new Bundle();
                    in.putExtras(bundle);
                    startActivity(in);
                } else {
                    NoticeDialog noticeDialog = new NoticeDialog(this, new IDialogListener() {
                        @Override
                        public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                            DialogUtils.dismissDialog(dialog);
                        }
                    });
                    DialogUtils.showDialog(noticeDialog);
                }
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        ScanCodeAttentionDialog aDialog = new ScanCodeAttentionDialog(this);
        DialogUtils.showDialog(aDialog);
    }

    //TvRecyclerView
    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        type_text = (TextView) itemView.findViewById(R.id.type_text);
        iv_image_bg = (ImageView) itemView.findViewById(R.id.iv_image_bg);
        iv_image_bg.setVisibility(View.INVISIBLE);
        type_text.setTextColor(Color.parseColor("#90ffffff"));
        type_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.w_36));
        isRefresh = true;
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        currentPage = 1;
        bridget.setVisibleWidget(true);
        mainUpView.setFocusView(itemView, oldView, 1.0f);
        currentPos = position;
        type_text = (TextView) itemView.findViewById(R.id.type_text);
        iv_image_bg = (ImageView) itemView.findViewById(R.id.iv_image_bg);
        iv_image_bg.setVisibility(View.VISIBLE);
        type_text.setTextColor(Color.parseColor("#ffd699"));
        type_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.w_42));
        //获取商品详情
        if (currentPos != -1 && isRefresh) {
            mHandler.removeMessages(REQUEST_DETAIL);
            mHandler.sendEmptyMessageDelayed(REQUEST_DETAIL, 300);
        }
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {

    }


    //gridView
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mainUpView.setFocusView(view, oldView, 1.0f);
        bridget.setVisibleWidget(false);
        oldView = view;
        bridget.setTranDurAnimTime(0);

        if (totalPage > 1 && !isLoading && parent.getLastVisiblePosition() == (parent.getCount() - 1)) {
            //到底了
            if (this.currentPage == totalPage) {
                //加载完成
                //已经加载完成
            } else if (parent.getLastVisiblePosition() == (parent.getCount() - 1)) {
                this.currentPage++;
                isLoading = true;
                OkGo.getInstance().cancelAll();
                mHandler.removeMessages(REQUEST_DETAIL);
                mHandler.sendEmptyMessageDelayed(REQUEST_DETAIL, 300);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MallGoodsDetailActivity.class);
        intent.putExtra("goodsId", mallGoodsListInfos.get(position).getId());
        startActivity(intent);
    }

    //relativeLayout
    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (oldFocus != null) {
            if (oldFocus instanceof GridView && newFocus.getParent() instanceof TvRecyclerView) {
                type_text.setTextColor(Color.parseColor("#ffd699"));
                type_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.w_42));
                iv_image_bg.setVisibility(View.VISIBLE);
                bridget.setVisibleWidget(true);
                bridget.setTranDurAnimTime(0);
                isRefresh = false;
                return;
            }
            if (oldFocus.getParent() instanceof RecyclerView && newFocus instanceof GridView) {
                if (type_text != null) {
                    type_text.setTextColor(Color.parseColor("#ffd699"));
                    type_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.w_36));
                    iv_image_bg.setVisibility(View.INVISIBLE);
                    View view = ((GridView) newFocus).getSelectedView();
                    if (view != null) {
                        mainUpView.setFocusView(view, oldView, 1.0f);
                        bridget.setTranDurAnimTime(0);
                        bridget.setVisibleWidget(false);
                    }
                }
            }

            if (oldFocus.getParent() instanceof RecyclerView && newFocus instanceof ImageView && newFocus.getId() == R.id.activity_gift_buy_shopping_cart_img) {
                type_text.setTextColor(Color.parseColor("#ffd699"));
                type_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.w_36));
                iv_image_bg.setVisibility(View.INVISIBLE);
                bridget.setVisibleWidget(true);
                bridget.setTranDurAnimTime(0);
                isRefresh = false;
            }

        }

    }

    @Override
    protected void onDestroy() {
//        recordEnd();
        super.onDestroy();
        DBUtils.delete(this, MallLoginInfo.class);
        OkHttpUtils.cancel();
        mallTitleInfos = null;
        mallGoodsListInfos = null;
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS, Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class, bundle);
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_DETAIL:
//                    OkGo.getInstance().cancelTag(this);
                    if (currentPos == -1) {
                        mallListPresenter.request(this, mallTitleInfos.get(0).getId(), info.getId(), String.valueOf(currentPage), String.valueOf(pageSize), "");
                    } else {
                        mallListPresenter.request(this, mallTitleInfos.get(currentPos).getId(), info.getId(), String.valueOf(currentPage), String.valueOf(pageSize), "");
                    }
                    break;
            }
        }

    };

    //search_img + shopping_cart_img
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.activity_gift_buy_search_img:
                if (hasFocus) {
                    search_img.setImageResource(R.drawable.activity_gift_buy_search_selected);
                } else {
                    search_img.setImageResource(R.drawable.activity_gift_buy_search);
                }
                break;
            case R.id.activity_gift_buy_shopping_cart_img:
                if (hasFocus) {
                    shopping_cart_img.setImageResource(R.drawable.activity_gift_buy_shopping_cart_selected);
                } else {
                    shopping_cart_img.setImageResource(R.drawable.activity_gift_buy_shopping_cart);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(BaseMallModel<List<MallTitleInfo>> dataList) {
        if (dataList.result.size() > 0 && dataList.result.get(0) != null) {
            List<MallTitleInfo> result = dataList.result;
            this.mallTitleInfos.clear();
            this.mallTitleInfos.addAll(result);
            mRecAdapter.notifyDataSetChanged();

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setSelection(0);
                }
            }, 10);
            search_img.setVisibility(View.VISIBLE);
            shopping_cart_img.setVisibility(View.VISIBLE);
            //获取礼品详情
            mHandler.removeMessages(REQUEST_DETAIL);
            mHandler.sendEmptyMessage(REQUEST_DETAIL);

        }
    }

    @Override
    public void onError() {

    }


//    /**
//     * 获取商品类型
//     *
//     * @param dataList 商品类型数据
//     */
//    @Override
//    public void onSuccess(BaseModel<List<GiftTypeBean>> dataList) {
//        if (dataList.data.size() > 0 && dataList.data.get(0) != null) {
//            List<GiftTypeBean> giftTypeBeanList = dataList.data;
//            this.giftTypeBeanList.clear();
//            this.giftTypeBeanList.addAll(giftTypeBeanList);
//            mRecAdapter.notifyDataSetChanged();
//
//            recyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerView.setSelection(0);
//                }
//            }, 10);
//            search_img.setVisibility(View.VISIBLE);
//            shopping_cart_img.setVisibility(View.VISIBLE);
//            //获取礼品详情
//            mHandler.removeMessages(REQUEST_DETAIL);
//            mHandler.sendEmptyMessage(REQUEST_DETAIL);
//
//        }
//    }
//
//    @Override
//    public void onError() {
//
//    }


    /**
     * 获取商品类型详情
     */
    private class GiftDetailListener implements IPresenterMallBase<MallGoodsListInfo> {

        @Override
        public void onSuccess(BaseMallModel<MallGoodsListInfo> dataList) {
            List<MallGoodsListInfo.GoodsListBean> goods_list = dataList.result.getGoods_list();
            totalPage = dataList.getResult().getTotal();
            if (currentPage <= 1) {
                mallGoodsListInfos.clear();
            }
            mallGoodsListInfos.addAll(goods_list);
            mGriAdapter.notifyDataSetChanged();
            isLoading = false;
        }

        @Override
        public void onError() {
            mallGoodsListInfos.clear();
            mGriAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getKeyCode() == KeyEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof GridView) {
                gridView = (CustomGridViewTv) getCurrentFocus();
                View view = gridView.getSelectedView();
                if (view != null) {
                    int itemPosition = gridView.getSelectedItemPosition();
                    if (itemPosition % 4 == 0) {
                        recyclerView.requestFocus();
                        return true;
                    }
                }
            }

            if (getCurrentFocus() != null && getCurrentFocus() instanceof ImageView && getCurrentFocus().getId() == R.id.activity_gift_buy_search_img) {
                return true;
            }
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof GridView) {
                gridView = (CustomGridViewTv) getCurrentFocus();
                View view = gridView.getSelectedView();
                if (view != null) {
                    int itemPosition = gridView.getSelectedItemPosition();
                    if (itemPosition < 4) {
                        return true;
                    }
                }
            }
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof ImageView && getCurrentFocus().getId() == R.id.activity_gift_buy_shopping_cart_img) {
                return true;
            }
        }


        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
