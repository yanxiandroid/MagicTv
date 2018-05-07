package com.yht.iptv.view.mall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MallGoodsDetailInfo;
import com.yht.iptv.model.MallShopCarInfo;
import com.yht.iptv.presenter.MallGoodsDetailPresenter;
import com.yht.iptv.tools.MallGoodsTypeDialog;
import com.yht.iptv.tools.NoticeDialog;
import com.yht.iptv.tools.ShopCarDialog;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.TextUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;

/**
 * Created by admin on 2017/7/19.
 */

public class MallGoodsDetailActivity extends BaseActivity implements ViewTreeObserver.OnGlobalFocusChangeListener, View.OnClickListener, View.OnFocusChangeListener, IPresenterMallBase<MallGoodsDetailInfo> {

    private ImageView iv_mall_goods_number_reduce;
    private ImageView iv_mall_goods_number_add;
    private TextView bt_more_type;
    private MainUpView mainUpView;
    private RelativeLayout rl_main;
    private EffectNoDrawBridge bridget;
    private View oldView;
    private TextView tv_mall_goods_number;
    private Button bt_shop_car;
    private Button bt_buy;
    //    private List<String> item1;
//    private List<String> item2;
    private TextView tv_goods_type;
    private WebView web_detail;
    private RelativeLayout rl_right;
    private ImageView iv_goods_image;
    private TextView tv_goods_name;
    private TextView tv_goods_price;
    private MallGoodsDetailInfo result;
    private List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> goodsSpecListBeen;
    private List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean> goodsSpecListBeen2;
    //    private List<List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean>> goodsSpecList;
    private ProgressDialog progressDialog;
    private int[] position;
    private int position1 = 0;//规格项1
    private int position2 = 0;//规格项2
    private ShopCarDialog shopCarDialog;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mall_goods_detail);

        String goodsId = getIntent().getStringExtra("goodsId");


        iv_mall_goods_number_reduce = (ImageView) findViewById(R.id.iv_mall_goods_number_reduce);
        iv_mall_goods_number_add = (ImageView) findViewById(R.id.iv_mall_goods_number_add);
        iv_goods_image = (ImageView) findViewById(R.id.iv_goods_image);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        bt_more_type = (TextView) findViewById(R.id.bt_more_type);
        mainUpView = (MainUpView) findViewById(R.id.mainUpView);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        tv_mall_goods_number = (TextView) findViewById(R.id.tv_mall_goods_number);
        tv_goods_type = (TextView) findViewById(R.id.tv_goods_type);
        bt_shop_car = (Button) findViewById(R.id.bt_shop_car);
        bt_buy = (Button) findViewById(R.id.bt_buy);
        web_detail = (WebView) findViewById(R.id.web_detail);
        rl_right = (RelativeLayout) findViewById(R.id.rl_right);

        tv_mall_goods_number.setText("1");

        rl_main.getViewTreeObserver().addOnGlobalFocusChangeListener(this);

        bt_more_type.setOnClickListener(this);
        bt_shop_car.setOnClickListener(this);
        bt_buy.setOnClickListener(this);
        iv_mall_goods_number_add.setOnClickListener(this);
        iv_mall_goods_number_reduce.setOnClickListener(this);

        bt_shop_car.setOnFocusChangeListener(this);
        bt_buy.setOnFocusChangeListener(this);

        bt_shop_car.setTextColor(Color.parseColor("#ffffff"));
        bt_shop_car.setBackgroundResource(R.drawable.mall_goods_shopcar_select_shape);
        bt_buy.setTextColor(Color.parseColor("#80575757"));
        bt_buy.setBackgroundResource(R.drawable.mall_goods_buy_shape);

//        //初始化dialog数据
//        item1 = getItem1();
//        item2 = getItem2();


        initViewMove();


//        web_detail.loadUrl("http://10.0.10.244/sample/htmlDemo/demo.html");
//        web_detail.getSettings().setNeedInitialFocus(false);
        //启用支持javascript
        web_detail.getSettings().setJavaScriptEnabled(true);

        web_detail.setBackgroundColor(Color.TRANSPARENT);

        iv_mall_goods_number_add.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_mall_goods_number_add.requestFocus();
            }
        }, 10);

        MallGoodsDetailPresenter presenter = new MallGoodsDetailPresenter(this, this);
        presenter.request(this, goodsId);

        initDialog(this, "详情信息加载中...");
        DialogUtils.showDialog(progressDialog);

    }

    private void initViewMove() {
        mainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
        mainUpView.setUpRectResource(R.drawable.gift_frame_new); // 设置移动边框的图片.
        mainUpView.setDrawUpRectPadding(new Rect(getDimension(R.dimen.w_18), getDimension(R.dimen.h_18), getDimension(R.dimen.w_18), getDimension(R.dimen.h_18))); // 边框图片设置间距.
        bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
        bridget.setTranDurAnimTime(0);
    }


    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (newFocus instanceof WebView) {
            mainUpView.setFocusView(rl_right, 1.0f);
        } else {
            mainUpView.setFocusView(newFocus, oldView, 1.0f);
        }
        oldView = newFocus;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_more_type:
                if (goodsSpecListBeen == null) {
                    ToastUtils.showShort("规格信息为空");
                    return;
                }
                //弹出对话框
                MallGoodsTypeDialog dialog = new MallGoodsTypeDialog(this, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //显示框
                        bridget.setVisibleWidget(false);
                    }
                }, new MallGoodsTypeDialog.OnRecyclerViewClick() {
                    @Override
                    public void onItemClick(int tag, int pos) {
                        String[] split = tv_goods_type.getText().toString().split(" ");
                        if (tag == R.id.tr_type1) {
                            position1 = pos;
                            if (goodsSpecListBeen2 != null) {
                                tv_goods_type.setText(split[0] + " " + goodsSpecListBeen.get(pos).getItem() + " " + split[2]);
                            } else {
                                tv_goods_type.setText(split[0] + " " + goodsSpecListBeen.get(pos).getItem());
                            }

                        } else if (tag == R.id.tr_type2) {
                            position2 = pos;
                            tv_goods_type.setText(split[0] + " " + split[1] + " " + goodsSpecListBeen2.get(pos).getItem());
                        }
                        if (goodsSpecListBeen2 != null) {
                            List<MallGoodsDetailInfo.SpecGoodsPriceBean> spec_goods_price = result.getSpec_goods_price();
                            if (spec_goods_price != null) {
                                for (MallGoodsDetailInfo.SpecGoodsPriceBean bean : spec_goods_price) {
                                    if (bean.getKey().equals(goodsSpecListBeen.get(position1).getItem_id() + "_" + goodsSpecListBeen2.get(position2).getItem_id())) {
                                        tv_goods_price.setText("价格：" + bean.getPrice());
                                        break;
                                    }
                                }
                            }
                        } else {
                            List<MallGoodsDetailInfo.SpecGoodsPriceBean> spec_goods_price = result.getSpec_goods_price();
                            if (spec_goods_price != null) {
                                for (MallGoodsDetailInfo.SpecGoodsPriceBean bean : spec_goods_price) {
                                    if (bean.getKey().equals(goodsSpecListBeen.get(position1).getItem_id())) {
                                        tv_goods_price.setText("价格：" + bean.getPrice());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }, goodsSpecListBeen, goodsSpecListBeen2, position1, position2);
                DialogUtils.showDialog(dialog);
                //隐藏框
                bridget.setVisibleWidget(true);
                break;
            case R.id.iv_mall_goods_number_add:
                textChanged(true);
                break;
            case R.id.iv_mall_goods_number_reduce:
                textChanged(false);
                break;
            case R.id.bt_shop_car:
                if (Constants.mainPageInfo.getPaymentSetting().getMall() == 1) {
                    //加入到数据库中
                    String name = result.getGoods().getGoods_name();
                    String content = "确定把 " + name + " 加入购物车?";
                    SpannableStringBuilder builder = new SpannableStringBuilder(content);
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#f69b4d"));
                    int start = content.indexOf(name);
                    builder.setSpan(span, start, name.length() + start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                    //确定按钮
                    shopCarDialog = new ShopCarDialog(MallGoodsDetailActivity.this, new IDialogListener() {
                        @Override
                        public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                            //确定按钮
                            if (tag.equals(Constants.CONFIRM)) {
                                addToShopCar();
                                DialogUtils.dismissDialog(shopCarDialog);
                            } else {
                                DialogUtils.dismissDialog(shopCarDialog);
                            }
                        }
                    }, builder);
                    DialogUtils.showDialog(shopCarDialog);
                } else {
                    NoticeDialog noticeDialog1 = new NoticeDialog(this, new IDialogListener() {
                        @Override
                        public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                            DialogUtils.dismissDialog(dialog);
                        }
                    });
                    DialogUtils.showDialog(noticeDialog1);
                }

                break;
            case R.id.bt_buy:
                if (Constants.mainPageInfo.getPaymentSetting().getMall() == 1) {
                    addToShopCar();
                    Intent intent = new Intent(this, MallGoodsShopCarActivity.class);
                    startActivity(intent);
                } else {
                    NoticeDialog noticeDialog2 = new NoticeDialog(this, new IDialogListener() {
                        @Override
                        public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                            DialogUtils.dismissDialog(dialog);
                        }
                    });
                    DialogUtils.showDialog(noticeDialog2);
                }
                break;
        }
    }

    private void textChanged(boolean isAdd) {
        int value = Integer.parseInt(tv_mall_goods_number.getText().toString());
        if (isAdd) {
            value++;
            if (value > 99) {
                value = 99;
            }
        } else {
            if (value == 1) {
                value = 1;
            } else {
                value--;
            }
        }
        tv_mall_goods_number.setText(String.valueOf(value));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.bt_buy) {
                bt_buy.setTextColor(Color.parseColor("#ffffff"));
                bt_buy.setBackgroundResource(R.drawable.mall_goods_buy_select_shape);
                bt_shop_car.setTextColor(Color.parseColor("#80575757"));
                bt_shop_car.setBackgroundResource(R.drawable.mall_goods_shopcar_shape);

            } else if (v.getId() == R.id.bt_shop_car) {
                bt_shop_car.setTextColor(Color.parseColor("#ffffff"));
                bt_shop_car.setBackgroundResource(R.drawable.mall_goods_shopcar_select_shape);
                bt_buy.setTextColor(Color.parseColor("#80575757"));
                bt_buy.setBackgroundResource(R.drawable.mall_goods_buy_shape);
            }
        }
//        else{
//            if (v.getId() == R.id.bt_buy) {
//                bt_buy.setTextColor(Color.parseColor("#80575757"));
//                bt_buy.setBackgroundResource(R.drawable.mall_goods_buy_shape);
//            } else if (v.getId() == R.id.bt_shop_car) {
//                bt_shop_car.setTextColor(Color.parseColor("#80575757"));
//                bt_shop_car.setBackgroundResource(R.drawable.mall_goods_shopcar_shape);
//            }
//        }
    }


//    private class MyRun implements Runnable{
//
//        private boolean isUp;
//
//        public MyRun(boolean isUp) {
//            this.isUp = isUp;
//        }
//
//        @Override
//        public void run() {
//            if(isUp){
//                web_detail.loadUrl("javascript:up()");
//            }else{
//                web_detail.loadUrl("javascript:down()");
//            }
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess(BaseMallModel<MallGoodsDetailInfo> dataList) {
        result = dataList.getResult();
        DialogUtils.dismissDialog(progressDialog);
        initData();
    }

    private void initData() {
        ShowImageUtils.showImageView(this, result.getGallery().get(0).getImage_url(), iv_goods_image);
        tv_goods_name.setText(result.getGoods().getGoods_name());
        tv_goods_price.setText("价格：" + result.getGoods().getShop_price());
        web_detail.loadDataWithBaseURL(null, htmlHead()[0] + result.getGoods().getGoods_content() + htmlHead()[1], "text/html", "utf-8", null);
        List<List<MallGoodsDetailInfo.GoodsBean.GoodsSpecListBean>> goods_spec_list = result.getGoods().getGoods_spec_list();
        if (goods_spec_list == null || goods_spec_list.size() == 0) {
            return;
        }
        //TODO
//        goodsSpecList = goods_spec_list;
//        position = new int[goods_spec_list.size()];
        goodsSpecListBeen = goods_spec_list.get(0);
        if (goods_spec_list.size() > 1) {
            goodsSpecListBeen2 = goods_spec_list.get(1);

            if (goodsSpecListBeen == null && goodsSpecListBeen2 == null) {
                return;
            }
        } else {
            if (goodsSpecListBeen == null) {
                return;
            }
        }
        position1 = 0;
        position2 = 0;
        //TODO
//        for(int i= 0 ; i < goodsSpecList.size();i++){
//            for(int j = 0 ; j < goodsSpecList.get(i).size(); j++){
//                if(goodsSpecList.get(i).get(j).getIsClick() == 1){
//                    position[i] = j;
//                }
//            }
//        }
        for (int i = 0; i < goodsSpecListBeen.size(); i++) {
            if (goodsSpecListBeen.get(i).getIsClick() == 1) {
                position1 = i;
            }
        }
        if (goods_spec_list.size() > 1) {
            for (int i = 0; i < goodsSpecListBeen2.size(); i++) {
                if (goodsSpecListBeen2.get(i).getIsClick() == 1) {
                    position2 = i;
                }
            }
            tv_goods_type.setText("规格: " + goodsSpecListBeen.get(position1).getItem() + " " + goodsSpecListBeen2.get(position2).getItem());
        } else {
            tv_goods_type.setText("规格: " + goodsSpecListBeen.get(position1).getItem());
        }
        //TODO
//        String typeName;
//        for(int i= 0 ; i < goodsSpecList.size();i++){
//            for(int j = 0 ; j < goodsSpecList.get(i).size(); j++){
//                goodsSpecList.get(i).get(j).getItem()
//            }
//        }
//        tv_goods_type.setText("规格: " + goodsSpecListBeen.get(position1).getItem());

        if (goods_spec_list.size() > 1) {
            List<MallGoodsDetailInfo.SpecGoodsPriceBean> spec_goods_price = result.getSpec_goods_price();
            if (spec_goods_price != null) {
                for (MallGoodsDetailInfo.SpecGoodsPriceBean bean : spec_goods_price) {
                    if (bean.getKey().equals(goodsSpecListBeen.get(position1).getItem_id() + "_" + goodsSpecListBeen2.get(position2).getItem_id())) {
                        tv_goods_price.setText("价格：" + bean.getPrice());
                    }
                }
            }
        } else {
            List<MallGoodsDetailInfo.SpecGoodsPriceBean> spec_goods_price = result.getSpec_goods_price();
            if (spec_goods_price != null) {
                for (MallGoodsDetailInfo.SpecGoodsPriceBean bean : spec_goods_price) {
                    if (bean.getKey().equals(goodsSpecListBeen.get(position1).getItem_id())) {
                        tv_goods_price.setText("价格：" + bean.getPrice());
                    }
                }
            }
        }

    }

    @Override
    public void onError() {
        DialogUtils.dismissDialog(progressDialog);
        //信息获取失败
        ToastUtils.showShort("信息获取失败");
        finish();
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


    private void addToShopCar() {

        MallGoodsDetailInfo.GoodsBean goods = result.getGoods();
        String price = "";
        String type_id = "";
        if (goodsSpecListBeen != null && goodsSpecListBeen2 != null) {
            List<MallGoodsDetailInfo.SpecGoodsPriceBean> spec_goods_price = result.getSpec_goods_price();
            if (spec_goods_price != null) {
                for (MallGoodsDetailInfo.SpecGoodsPriceBean bean : spec_goods_price) {
                    if (bean.getKey().equals(goodsSpecListBeen.get(position1).getItem_id() + "_" + goodsSpecListBeen2.get(position2).getItem_id())) {
                        price = bean.getPrice();
                        type_id = bean.getId();
                        break;
                    }
                }
            }
        } else if (goodsSpecListBeen2 == null) {
            List<MallGoodsDetailInfo.SpecGoodsPriceBean> spec_goods_price = result.getSpec_goods_price();
            if (spec_goods_price != null) {
                for (MallGoodsDetailInfo.SpecGoodsPriceBean bean : spec_goods_price) {
                    if (bean.getKey().equals(goodsSpecListBeen.get(position1).getItem_id())) {
                        price = bean.getPrice();
                        type_id = bean.getId();
                        break;
                    }
                }
            }
        } else {
            price = goods.getShop_price();
        }

        if (price.equals("")) {
            price = goods.getShop_price();
        }
        //查询数据库
        WhereBuilder builder;
        if (type_id.equals("")) {
            builder = WhereBuilder.b("goodsId", "=", goods.getGoods_id());
        } else {
            //查询数据库
            builder = WhereBuilder.b("goodsId", "=", goods.getGoods_id()).and("type_id", "=", type_id);
        }
        //查询第一条数据
        MallShopCarInfo mallShopCarInfo = DBUtils.find(this, MallShopCarInfo.class, builder);

        if (mallShopCarInfo == null) {
            //插入数据库
            MallShopCarInfo info = new MallShopCarInfo();
            info.setGoodsid(goods.getGoods_id());
            info.setTotal(tv_mall_goods_number.getText().toString());
            info.setThumb(result.getGallery().get(0).getImage_url());
            info.setGoodsTitle(goods.getGoods_name());
            info.setGoodsPrice(price);
            if (goodsSpecListBeen != null && goodsSpecListBeen2 != null) {
                info.setTypeId1(goodsSpecListBeen.get(position1).getItem_id());
                info.setTypeName1(goodsSpecListBeen.get(position1).getItem());
                info.setTypeId2(goodsSpecListBeen2.get(position2).getItem_id());
                info.setTypeName2(goodsSpecListBeen2.get(position2).getItem());
                info.setOptionid(type_id);
            } else if (goodsSpecListBeen != null && goodsSpecListBeen2 == null) {
                info.setTypeId1(goodsSpecListBeen.get(position1).getItem_id());
                info.setTypeName1(goodsSpecListBeen.get(position1).getItem());
                info.setOptionid(type_id);
            }
            info.setGoodsId_typeId(info.getGoodsid() + type_id);
            DBUtils.save(this, info);
        } else {
            int num = Integer.parseInt(mallShopCarInfo.getTotal()) + Integer.parseInt(tv_mall_goods_number.getText().toString());
            if (num <= 99) {
                mallShopCarInfo.setTotal(String.valueOf(num));
                DBUtils.update(this, mallShopCarInfo, "goodsNum");
            } else {
                ToastUtils.showShort("已超过个人购买商品数量最大值,不能加入购物车!");
                return;
            }
        }

        String toast = "成功将 " + goods.getGoods_name() + "  加入购物车!";
        CharSequence textColor = TextUtils.getChangeTextColor(toast, toast.indexOf(goods.getGoods_name()), toast.indexOf(goods.getGoods_name()) + goods.getGoods_name().length(), "#f36907");

        ToastUtils.showShort(textColor);
    }

    private String[] htmlHead() {
        String[] html;
        html = new String[2];
        html[0] = "<body style='margin:0 0 -16px;'>";
        html[1] = "</body>";

        return html;

    }

}
