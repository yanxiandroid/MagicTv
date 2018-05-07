package com.yht.iptv.view.mall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lzy.okgo.OkGo;
import com.open.androidtvwidget.view.MainUpView;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MallLoginInfo;
import com.yht.iptv.model.MallShopCarInfo;
import com.yht.iptv.model.MallShopCarListInfo;
import com.yht.iptv.model.MallStatusInfo;
import com.yht.iptv.presenter.MallGoodsLoginOutPresenter;
import com.yht.iptv.presenter.MallGoodsLoginStatusPresenter;
import com.yht.iptv.presenter.MallOrderApplyPresenter;
import com.yht.iptv.tools.ScanCodeAttentionDialog;
import com.yht.iptv.tools.ScanCodeLoginDialog;
import com.yht.iptv.tools.TvRecyclerViewBridge;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.Convert;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.QrCodeCreate;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MallGoodsShopCarActivity extends BaseActivity implements View.OnClickListener, TvRecyclerView.OnItemListener, View.OnFocusChangeListener, IPresenterMallBase<MallLoginInfo> {
    private TextView room_id_text, hotel_name_text, price_num_text, all_price_text;
    private Button[] buttons;
    private TvRecyclerView mRecyclerView;
    private LinearLayout car_empty_ll;
    private String room_id = null;
    private GiftBuyCarRecyclerAdapter adapter;
    private View oldView;
    private TvRecyclerViewBridge mRecyclerViewBridge;


    private ScanCodeLoginDialog lDialog;//扫码登录

    private RelativeLayout specifications_rl;
    private List<MallShopCarInfo> mallShopCarInfos;
    private MainPageInfo info;
    private float allPrice;
    private int allNum;
    private String message;
    private MyHandler handler;

    private static final int LOGIN_STATUS = 1;
    private MallGoodsLoginStatusPresenter presenter;
    private ProgressDialog progressDialog;
    private ScanCodeAttentionDialog aDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_buy_car_new_view);
        MallLoginInfo mallLoginInfo = DBUtils.find(this, MallLoginInfo.class);
        if(mallLoginInfo != null) {
            MallGoodsLoginOutPresenter presenter = new MallGoodsLoginOutPresenter(this);
            presenter.request(this,mallLoginInfo.getOpenid());
            DBUtils.delete(this, MallLoginInfo.class);
        }
        //获取购物车信息
        mallShopCarInfos = DBUtils.findAll(this, MallShopCarInfo.class);

        if (mallShopCarInfos == null) {
            mallShopCarInfos = new ArrayList<>();
        }
        initView();
        initData();
    }


    private void initView() {
        buttons = new Button[2];
        price_num_text = (TextView) findViewById(R.id.car_price_num_text);
        all_price_text = (TextView) findViewById(R.id.car_all_price_text);
        car_empty_ll = (LinearLayout) findViewById(R.id.car_empty_ll);
        mRecyclerView = (TvRecyclerView) findViewById(R.id.car_recyclerView);
        buttons[0] = (Button) findViewById(R.id.car_clear_btn);
        buttons[1] = (Button) findViewById(R.id.car_apply_btn);

        ImageView iv_bg = (ImageView) findViewById(R.id.iv_bg);
        ShowImageUtils.showImageView(this,R.drawable.activity_gift_buy_bg_new,iv_bg);

        initViewMove();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setInterceptKeyEvent(true);
        mRecyclerView.setSelectedItemAtCentered(true);
        mRecyclerView.setOnItemListener(this);
        adapter = new GiftBuyCarRecyclerAdapter(this, mallShopCarInfos);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setSelection(0);
            }
        }, 10);

        if (mallShopCarInfos.size() == 0) {
            //没有商品
            buttons[0].setFocusable(false);
            buttons[1].setFocusable(false);
            car_empty_ll.setVisibility(View.VISIBLE);
        } else {
            car_empty_ll.setVisibility(View.GONE);
        }

        for (int i = 0; i < 2; i++) {
            buttons[i].setOnClickListener(this);
            buttons[i].setOnFocusChangeListener(this);
        }

//        if (list.size() <= 4) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
        layoutParams.leftMargin = getDimension(R.dimen.w_204);
        mRecyclerView.setLayoutParams(layoutParams);
//        } else {
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
//            layoutParams.leftMargin = getDimension(R.dimen.w_2);
//            mRecyclerView.setLayoutParams(layoutParams);
//        }


    }

    private void initData() {
        hotel_name_text = (TextView) findViewById(R.id.car_hotel_name_text);
        room_id_text = (TextView) findViewById(R.id.car_room_id_text);
        info = Constants.mainPageInfo;
        if (info != null) {
            hotel_name_text.setText("酒店商城");
        }
        //获取房间号
        room_id = (String) SPUtils.get(this, Constants.ROOM_ID, "");
        presenter = new MallGoodsLoginStatusPresenter(this, this);
        room_id_text.setText("房间号: " + room_id);
        handler = new MyHandler(this);
        message = "&hotelID=" + info.getId() + "&roomNum=" + room_id + "&userID=" + info.getCustomerId() + "&pmsStatus=" + Constants.mainPageInfo.getPaymentSetting().getPayWithRoomfee();
        if (mallShopCarInfos.size() > 0) {
            allPrice = 0;
            allNum = 0;
            for (MallShopCarInfo infos : mallShopCarInfos) {
                if (infos.getGoodsPrice().matches("^(-?\\d+)(\\.\\d+)?$") && infos.getTotal().matches("^(-?\\d+)(\\.\\d+)?$")) {
                    int goodsNum = Integer.parseInt(infos.getTotal());
                    allNum += goodsNum;
                    float goodsPrice = Float.parseFloat(infos.getGoodsPrice());
                    allPrice += goodsPrice * goodsNum;
                }
            }

            price_num_text.setText("已选" + allNum + "件商品");
            all_price_text.setText("总计：" + numberFormat(allPrice) + "元");
        }
    }

    private void initViewMove() {
        MainUpView mainUpView = (MainUpView) findViewById(R.id.mainUpView);
        mainUpView.setEffectBridge(new TvRecyclerViewBridge());
        mRecyclerViewBridge = (TvRecyclerViewBridge) mainUpView.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.gift_frame_new);
        mRecyclerViewBridge.setTranDurAnimTime(0);

    }

    private String numberFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_apply_btn:
                MallLoginInfo mallLoginInfo = DBUtils.find(this, MallLoginInfo.class);
                if (mallLoginInfo == null) {
                    //登录
                    try {
                        Bitmap qrCode = QrCodeCreate.createQRCode(HttpConstants.MALL_GOODS_LOGIN_INFO + message, getDimension(R.dimen.w_488), getDimension(R.dimen.h_10));
                        lDialog = new ScanCodeLoginDialog(this, qrCode, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                handler.removeMessages(LOGIN_STATUS);
                                OkGo.getInstance().cancelTag(MallGoodsShopCarActivity.this);
                            }
                        });
                        DialogUtils.showDialog(lDialog);
                        handler.sendEmptyMessageDelayed(LOGIN_STATUS, 1000);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    MallShopCarListInfo infos = new MallShopCarListInfo();
                    infos.setRoomId(room_id);
                    infos.setHotelId(String.valueOf(info.getId()));
                    infos.setGoodsAllNum(allNum);
                    infos.setUserId(info.getCustomerId());
                    infos.setMallShopCarInfos(mallShopCarInfos);
                    String orderJson = Convert.toJson(infos);
                    initDialog(this, "提交购物车中...");
                    DialogUtils.showDialog(progressDialog);
                    MallOrderApplyPresenter presenter = new MallOrderApplyPresenter(progressDialog, this, new IPresenterMallBase<MallStatusInfo>() {
                        @Override
                        public void onSuccess(BaseMallModel<MallStatusInfo> dataList) {
                            ToastUtils.showShort(dataList.getMsg());
                            DBUtils.delete(MallGoodsShopCarActivity.this, MallShopCarInfo.class);
                            mallShopCarInfos.clear();
                            adapter.notifyDataSetChanged();
                            car_empty_ll.setVisibility(View.VISIBLE);
                            price_num_text.setText("已选" + 0 + "件商品");
                            all_price_text.setText("总计：" + 0 + "元");
                            //取消焦点控制
                            buttons[0].setFocusable(false);
                            buttons[1].setFocusable(false);
                            mRecyclerViewBridge.setVisibleWidget(true);
                            DialogUtils.dismissDialog(progressDialog);
                            showDialog();
                        }

                        @Override
                        public void onError() {
                            ToastUtils.showShort("服务器出错");
                            DialogUtils.dismissDialog(progressDialog);
                        }
                    });
                    presenter.request(orderJson, mallLoginInfo.getOpenid());
                }

                break;
            case R.id.car_clear_btn:
                DBUtils.delete(this, MallShopCarInfo.class);
                mallShopCarInfos.clear();
                adapter.notifyDataSetChanged();
                car_empty_ll.setVisibility(View.VISIBLE);
                price_num_text.setText("已选" + 0 + "件商品");
                all_price_text.setText("总计：" + 0 + "元");
                //取消焦点控制
                buttons[0].setFocusable(false);
                buttons[1].setFocusable(false);
                mRecyclerViewBridge.setVisibleWidget(true);
                break;
            default:
                break;
        }
    }


    //TvRecyclerViewBridge
    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        specifications_rl = (RelativeLayout) itemView.findViewById(R.id.car_rec_item_specifications_rl);
        specifications_rl.setVisibility(View.INVISIBLE);
        mRecyclerViewBridge.setUnFocusView(itemView);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        specifications_rl = (RelativeLayout) itemView.findViewById(R.id.car_rec_item_specifications_rl);
        specifications_rl.setVisibility(View.VISIBLE);
        mRecyclerViewBridge.setDrawUpRectPadding(new Rect(getDimension(R.dimen.w_21), getDimension(R.dimen.h_21), getDimension(R.dimen.w_21), getDimension(R.dimen.h_21)));
        mRecyclerViewBridge.setFocusView(itemView, oldView, 1.0f);
        oldView = itemView;
        mRecyclerViewBridge.setTranDurAnimTime(200);

    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {
        mRecyclerViewBridge.setFocusView(itemView, oldView, 1.0f);
        oldView = itemView;
    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getKeyCode() == KeyEvent.ACTION_DOWN) {

        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mRecyclerViewBridge.setDrawUpRectPadding(new Rect(getDimension(R.dimen.w_17), getDimension(R.dimen.h_17), getDimension(R.dimen.w_17), getDimension(R.dimen.h_17))); // 边框图片设置间距.
            mRecyclerViewBridge.setFocusView(v, oldView, 1.0f);
        }
    }

    @Override
    public void onSuccess(BaseMallModel<MallLoginInfo> dataList) {
        ToastUtils.showShort(dataList.getResult().getNickname() + "登录成功!");
        DialogUtils.dismissDialog(lDialog);
        DBUtils.save(this, dataList.getResult());

        MallShopCarListInfo infos = new MallShopCarListInfo();
        infos.setRoomId(room_id);
        infos.setHotelId(String.valueOf(info.getId()));
        infos.setGoodsAllNum(allNum);
        infos.setUserId(info.getCustomerId());
        infos.setMallShopCarInfos(mallShopCarInfos);
        String orderJson = Convert.toJson(infos);
        initDialog(this, "提交购物车中...");
        DialogUtils.showDialog(progressDialog);
        MallOrderApplyPresenter presenter = new MallOrderApplyPresenter(progressDialog, this, new IPresenterMallBase<MallStatusInfo>() {
            @Override
            public void onSuccess(BaseMallModel<MallStatusInfo> dataList) {
                ToastUtils.showShort(dataList.getMsg());
                DBUtils.delete(MallGoodsShopCarActivity.this, MallShopCarInfo.class);
                mallShopCarInfos.clear();
                adapter.notifyDataSetChanged();
                car_empty_ll.setVisibility(View.VISIBLE);
                price_num_text.setText("已选" + 0 + "件商品");
                all_price_text.setText("总计：" + 0 + "元");
                //取消焦点控制
                buttons[0].setFocusable(false);
                buttons[1].setFocusable(false);
                mRecyclerViewBridge.setVisibleWidget(true);
                DialogUtils.dismissDialog(progressDialog);
                showDialog();
            }

            @Override
            public void onError() {
                DialogUtils.dismissDialog(progressDialog);
                ToastUtils.showShort("服务器出错!");
            }
        });
        presenter.request(orderJson, dataList.getResult().getOpenid());
    }

    @Override
    public void onError() {
        handler.sendEmptyMessageDelayed(LOGIN_STATUS, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(LOGIN_STATUS);
        OkGo.getInstance().cancelTag(this);
    }

    private static class MyHandler extends Handler {

        private WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MallGoodsShopCarActivity activity = (MallGoodsShopCarActivity) weakReference.get();
            switch (msg.what) {
                case LOGIN_STATUS:
                    activity.requestLoginMsg();
                    break;
            }

        }
    }
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            //要做的事情
//            if (lDialog != null) {
//                DialogUtils.dismissDialog(lDialog);
//            }
//
//            try {
//                Bitmap qrCodeAliPay = QrCodeCreate.createQRCode("支付宝支付测试", getDimension(R.dimen.w_348), getDimension(R.dimen.h_5));
//                Bitmap qrCodeWeiXin = QrCodeCreate.createQRCode("微信支付测试", getDimension(R.dimen.w_348), getDimension(R.dimen.h_5));
//                pDialog = new ScanCodePayDialog(MallGoodsShopCarActivity.this, qrCodeAliPay, qrCodeWeiXin);
//                DialogUtils.showDialog(pDialog);
//                handler2.postDelayed(runnable2, 5000);//每两秒执行一次runnable.
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//        }
//    };


//    Handler handler2 = new Handler();
//    Runnable runnable2 = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            //要做的事情
//            if (pDialog != null) {
//                DialogUtils.dismissDialog(pDialog);
//            }
//
//
//            sDialog = new PaySuccessDialog(MallGoodsShopCarActivity.this, new IDialogListener() {
//                @Override
//                public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
//                    DialogUtils.dismissDialog(sDialog);
//                    showDialog();
//
//                }
//            }, "1298");
//            DialogUtils.showDialog(sDialog);
//
//        }
//    };


    private void showDialog() {
        aDialog = new ScanCodeAttentionDialog(MallGoodsShopCarActivity.this);
        DialogUtils.showDialog(aDialog);
    }

    private void requestLoginMsg() {
        presenter.request(this, String.valueOf(info.getId()), room_id, info.getCustomerId());
//        presenter.request(this,"1001","202","202");
    }


    private void initDialog(Activity activity, String msg) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(msg);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            MallLoginInfo mallLoginInfo = DBUtils.find(this, MallLoginInfo.class);
            if(mallLoginInfo != null) {
                MallGoodsLoginOutPresenter presenter = new MallGoodsLoginOutPresenter(this);
                presenter.request(this,mallLoginInfo.getOpenid());
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
