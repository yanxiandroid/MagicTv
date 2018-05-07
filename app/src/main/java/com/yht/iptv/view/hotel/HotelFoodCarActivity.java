package com.yht.iptv.view.hotel;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.adapter.HotelFoodCarRecyclerViewAdapter;
import com.yht.iptv.callback.IDialogClick;
import com.yht.iptv.callback.IDialogListener;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.FoodCarInfo;
import com.yht.iptv.model.FoodPayStatus;
import com.yht.iptv.model.FoodShopCarInfo;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.OrderDetailBean;
import com.yht.iptv.model.OrderDetailInfo;
import com.yht.iptv.model.VideoPayBean;
import com.yht.iptv.presenter.AccountPayPresenter;
import com.yht.iptv.presenter.FoodPayPresenter;
import com.yht.iptv.presenter.FoodPayStatusPresenter;
import com.yht.iptv.presenter.OrderApplyPresenter;
import com.yht.iptv.tools.CustomDialog;
import com.yht.iptv.tools.FoodCarModifyDialog;
import com.yht.iptv.tools.InstallDialog;
import com.yht.iptv.tools.PayDialog;
import com.yht.iptv.tools.PayMentChargeDialog;
import com.yht.iptv.tools.PaySuccessDialog;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.Convert;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.GsonUtils;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.QrCodeCreate;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.TextUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.db.sqlite.WhereBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.yht.iptv.R.id.car_item_name;
import static com.yht.iptv.R.id.car_item_num;
import static com.yht.iptv.R.id.car_item_price;

/**
 * 客房点餐购物车
 * Created by Q on 2017/10/25.
 */

public class HotelFoodCarActivity extends BaseActivity implements TvRecyclerView.OnItemListener, View.OnClickListener, View.OnFocusChangeListener, IPresenterBase<List<OrderDetailBean>> {
    private TextView food_car_name, food_room, food_prompt, food_car_num, food_car_price;
    private LinearLayout car_empty_ll;
    private ImageView food_car_apply_img, food_car_clear_img;
    private TvRecyclerView recyclerView;
    private List<FoodCarInfo> foodCarInfo;
    private HotelFoodCarRecyclerViewAdapter adapter;
    private Button[] buttons;
    private FoodCarModifyDialog dialog;
    private boolean isKeyEnable;
    private OrderApplyPresenter presenter;
    //    private String roomId = "9999";
//    private String customerId = "qipa";
//    private String customerName = "qipa";
    private PaySuccessDialog paySuccessDialog;

    //    private MediaPayDialog mediaPayDialog;
    private PayDialog payDialog;
    private PayMentChargeDialog payMentChargeDialog;
    private FoodPayPresenter foodPayPresenter;
    private FoodPayStatusPresenter foodPayStatusPresenter;
    //    private VideoPayBean videoPayBean;//请求bean
//    private static final int GET_STATUS = 1;
    private String price;//总价
    private String orderNum;//订单号
    private int currentPosition;
    private MainPageInfo info;
    private String roomId;
    private InstallDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_food_car);
        EventBus.getDefault().register(this);
        info = Constants.mainPageInfo;
        roomId = (String) SPUtils.get(this, Constants.ROOM_ID, "");
        initView();
        initViewData();
        initData();
    }

    private void initView() {
        buttons = new Button[2];
        food_car_name = (TextView) findViewById(R.id.food_car_name);
        food_room = (TextView) findViewById(R.id.food_room);
        food_prompt = (TextView) findViewById(R.id.food_prompt);
        food_car_num = (TextView) findViewById(R.id.food_car_num);
        food_car_price = (TextView) findViewById(R.id.food_car_price);
        buttons[0] = (Button) findViewById(R.id.food_car_apply);
        buttons[1] = (Button) findViewById(R.id.food_car_clear);
        food_car_apply_img = (ImageView) findViewById(R.id.food_car_apply_img);
        food_car_clear_img = (ImageView) findViewById(R.id.food_car_clear_img);
        recyclerView = (TvRecyclerView) findViewById(R.id.food_car_recycler);
        car_empty_ll = (LinearLayout) findViewById(R.id.car_empty_ll);
        RelativeLayout rl_main = (RelativeLayout) findViewById(R.id.rl_main);

        if(Constants.currentLanguage.equals("zh")) {
            food_room.setText(roomId + "号房");
            food_car_name.setText(info.getZhName());
        }else{
            food_room.setText("No:" + roomId);
            food_car_name.setText(info.getEnName());
        }
        foodCarInfo = new ArrayList<>();

    }

    private void initData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setInterceptKeyEvent(true);
        recyclerView.setSelectedItemAtCentered(true);
//        recyclerView.setSelectedItemOffset(getDimension(R.dimen.w_10), getDimension(R.dimen.w_1453));
        recyclerView.setOnItemListener(this);
        adapter = new HotelFoodCarRecyclerViewAdapter(HotelFoodCarActivity.this, foodCarInfo);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 2; i++) {
            buttons[i].setOnClickListener(this);
            buttons[i].setOnFocusChangeListener(this);
        }

        presenter = new OrderApplyPresenter(this, this);
        foodPayPresenter = new FoodPayPresenter(this, new FoodPayListener());
        foodPayStatusPresenter = new FoodPayStatusPresenter(this, new FoodPayStatusListener());

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setSelection(1);
            }
        }, 50);
    }

    private void initViewData() {
        List<FoodCarInfo> info = DBUtils.findAll(this, FoodCarInfo.class);
        if (info != null && info.size() > 0) {
            foodCarInfo.clear();
            foodCarInfo.addAll(info);
            foodCarInfo.add(0, new FoodCarInfo());
            foodCarInfo.add(new FoodCarInfo());
            if(Constants.currentLanguage.equals("zh")) {
                food_car_num.setText(dispAllNum() + "件商品");
                food_car_price.setText("总计" + dispAllPrice() + "元");
                String s = "按 OK 键修改商品数量和类型";
                CharSequence textColor = TextUtils.getChangeTextColor(s, s.indexOf(" ") + 1, s.lastIndexOf(" "), "#ffd699");
                food_prompt.setText(textColor);
            }else{
                food_car_num.setText("Total number :" + dispAllNum());
                food_car_price.setText("All:￥" + dispAllPrice());
                String s = "Press OK key change product number";
                CharSequence textColor = TextUtils.getChangeTextColor(s, s.indexOf(" ") + 1, s.indexOf("OK") + "OK".length()+1, "#ffd699");
                food_prompt.setText(textColor);
            }
        } else {
            car_empty_ll.setVisibility(View.VISIBLE);
            if(Constants.currentLanguage.equals("zh")) {
                food_car_num.setText("0件商品");
            }else{
                food_car_num.setText("Total number :0");
            }
            food_car_price.setText("");
            buttons[0].setFocusable(false);
            buttons[1].setFocusable(false);
            food_prompt.setText("");
        }

    }


    /**
     * 处理总价
     */
    private String dispAllPrice() {
        float allPrice = 0;
        for (int i = 0; i < foodCarInfo.size(); i++) {
            if (foodCarInfo.get(i).getNum() != null) {
                long num = Integer.parseInt(foodCarInfo.get(i).getNum());
                float price = Float.parseFloat(foodCarInfo.get(i).getPrice());
                allPrice += num * price;
            }
        }
        return numberFormat(allPrice);
    }

    /**
     * 处理总数量
     */
    private long dispAllNum() {
        long allNum = 0;
        for (int i = 0; i < foodCarInfo.size(); i++) {
            if (foodCarInfo.get(i).getNum() != null) {
                long num = Integer.parseInt(foodCarInfo.get(i).getNum());
                allNum += num;
            }
        }
        return allNum;
    }

    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {

        float translationY = itemView.getTranslationY();
        float translationX = itemView.getTranslationX();
        ImageView img = (ImageView) itemView.findViewById(R.id.car_item_img);
        RelativeLayout nameRl = (RelativeLayout) itemView.findViewById(R.id.car_item_name_rl);
        RelativeLayout priceRl = (RelativeLayout) itemView.findViewById(R.id.car_item_price_rl);
        TextView car_name = (TextView) itemView.findViewById(car_item_name);
        TextView car_price = (TextView) itemView.findViewById(car_item_price);
        TextView car_num = (TextView) itemView.findViewById(car_item_num);
//        AnimationUtils.scaleXYTranslationXYAnimation(itemView, 500, 1.1f, 1.0f, translationY, 0, translationX, 0);
//        AnimationUtils.scaleTranslationXAnimation(priceRl, 500, 1.14f, 1.0f, translationX, 0);
//        AnimationUtils.scaleAnimation(nameRl, 500, true, 1.26f, 1.0f);
//        AnimationUtils.scaleTranslationAnimation(priceRl, 500, 1.37f, 1.0f, translationY, 0);
        priceRl.setBackgroundColor(getResources().getColor(R.color.car_price_preselected));
        nameRl.setVisibility(View.GONE);
        car_name.setVisibility(View.GONE);
        car_num.setTextColor(getResources().getColor(R.color.car_num_preselected));
        car_price.setTextColor(getResources().getColor(R.color.car_num_preselected));
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {

        TextView car_name = (TextView) itemView.findViewById(car_item_name);
        TextView car_price = (TextView) itemView.findViewById(car_item_price);
        TextView car_num = (TextView) itemView.findViewById(car_item_num);
        RelativeLayout nameRl = (RelativeLayout) itemView.findViewById(R.id.car_item_name_rl);
        RelativeLayout priceRl = (RelativeLayout) itemView.findViewById(R.id.car_item_price_rl);
        float translationY = itemView.getTranslationY();
        float translationX = itemView.getTranslationX();
        float height = itemView.getHeight() * 0.05f;
        float width = priceRl.getWidth() * 0.1f;
//        AnimationUtils.scaleXYTranslationXYAnimation(itemView, 500, 1.0f, 1.1f, translationY, -height, translationX, width);
//        AnimationUtils.scaleTranslationXAnimation(priceRl, 500, 1.0f, 1.14f, translationX, width);
//        AnimationUtils.scaleAnimation(nameRl, 500, true, 1.0f, 1.26f);
//        AnimationUtils.scaleTranslationAnimation(priceRl, 500, 1.0f, 1.37f, translationY, height2);
        priceRl.setBackgroundColor(getResources().getColor(R.color.car_price_selected));
        nameRl.setVisibility(View.VISIBLE);
        car_name.setVisibility(View.VISIBLE);
        car_num.setTextColor(getResources().getColor(R.color.white));
        car_price.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, final int position) {
        currentPosition = position;
        dialog = new FoodCarModifyDialog(HotelFoodCarActivity.this
                , foodCarInfo.get(position)
                , new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                deleteNum();
                dialog.dismiss();
            }
        }, dispAllNum());

        DialogUtils.showDialog(dialog);

    }

    private void deleteNum() {
        FoodCarInfo carInfo = foodCarInfo.get(currentPosition);
        WhereBuilder builder = WhereBuilder.b("foodId", "=", carInfo.getFoodId()).and("restaurantId", "=", carInfo.getRestaurantId());
        if(carInfo.getNum().equals("0")){
            DBUtils.delete(this, FoodCarInfo.class, builder);
            foodCarInfo.remove(currentPosition);
            if (currentPosition == 1) {
                currentPosition = 1;
            } else {
                currentPosition = currentPosition - 1;
            }
        }else {
            DBUtils.update(this, carInfo, "num");
        }
        if(foodCarInfo.size() <= 2){
            car_empty_ll.setVisibility(View.VISIBLE);
            food_car_num.setText(0 + "件商品");
            food_car_price.setText("");
            food_prompt.setText("");
            buttons[0].setFocusable(false);
            buttons[1].setFocusable(false);
            food_car_apply_img.setVisibility(View.GONE);
            food_car_clear_img.setVisibility(View.GONE);
            buttons[0].setTextColor(Color.parseColor("#ffffff"));
            buttons[1].setTextColor(Color.parseColor("#20ffffff"));
        }

        //按条件删除num为0的记录
//        WhereBuilder whereBuilder = WhereBuilder.b("num", "=", "0");
//        DBUtils.delete(this, FoodCarInfo.class, whereBuilder);
        adapter.notifyDataSetChanged();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setSelection(currentPosition);
            }
        }, 10);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.food_car_apply:
                if(Constants.mainPageInfo.getPaymentSetting().getDines() == 1) {
                    OrderDetailInfo orderDetailInfo = setOderInfo(info.getCustomerName(), info.getCustomerId());
                    if (orderDetailInfo.getOrderDetail().size() != 0) {
                        showDialog(orderDetailInfo);
                    } else {
                        ToastUtils.showShort("购物车总商品数量不能为0");
                    }
                }else{
                    ToastUtils.showShort("暂不支持点餐,抱歉!");
                }
                break;
            case R.id.food_car_clear:
                foodCarInfo.clear();
                DBUtils.delete(this, FoodCarInfo.class);
                adapter.notifyDataSetChanged();
                car_empty_ll.setVisibility(View.VISIBLE);
                food_car_num.setText(0 + "件商品");
                food_car_price.setText("");
                food_prompt.setText("");
                buttons[0].setFocusable(false);
                buttons[1].setFocusable(false);
                food_car_apply_img.setVisibility(View.GONE);
                food_car_clear_img.setVisibility(View.GONE);
                buttons[0].setTextColor(Color.parseColor("#ffffff"));
                buttons[1].setTextColor(Color.parseColor("#20ffffff"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.food_car_apply:
                if (hasFocus) {
                    food_car_apply_img.setVisibility(View.VISIBLE);
                    buttons[0].setTextColor(Color.parseColor("#ffffff"));
                } else {
                    food_car_apply_img.setVisibility(View.GONE);
                    buttons[0].setTextColor(Color.parseColor("#20ffffff"));
                }
                break;
            case R.id.food_car_clear:
                if (hasFocus) {
                    food_car_clear_img.setVisibility(View.VISIBLE);
                    buttons[1].setTextColor(Color.parseColor("#ffffff"));
                } else {
                    food_car_clear_img.setVisibility(View.GONE);
                    buttons[1].setTextColor(Color.parseColor("#20ffffff"));
                }
                break;
            default:
                break;
        }

    }


    private String numberFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }


//    @SuppressLint("HandlerLeak")
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GET_STATUS:
//                    foodPayStatusPresenter.request(this, orderNum);
//                    break;
//            }
//        }
//    };


    private void showDialog(final OrderDetailInfo orderDetailInfo) {
        CustomDialog customDialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    initDialog(HotelFoodCarActivity.this,getStrings(R.string.loading));
                    DialogUtils.dismissDialog(dialog);
                    String json = GsonUtils.toJson(orderDetailInfo);
                    //请求网络传送json
                    presenter.request(this, json);
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                }
            }
        }, R.string.order_apply);
        DialogUtils.showDialog(customDialog);
    }

    private void accountDialog(final VideoPayBean videoPayBean){
        CustomDialog dialog = new CustomDialog(this, new IDialogClick() {
            @Override
            public void onClick(CustomDialog dialog, String tag) {
                if (tag.equals(Constants.CONFIRM)) {
                    if(videoPayBean != null) {
                        DialogUtils.dismissDialog(dialog);
                        initDialog(HotelFoodCarActivity.this,getStrings(R.string.loading));
                        //请求房账支付
                        AccountPayPresenter payPresenter = new AccountPayPresenter(new IPresenterBase() {
                            @Override
                            public void onSuccess(BaseModel dataList) {
                                DialogUtils.dismissDialog(progressDialog);
                                DialogUtils.dismissDialog(payDialog);
                                DialogUtils.dismissDialog(payMentChargeDialog);
                                DBUtils.delete(HotelFoodCarActivity.this, FoodCarInfo.class);
                                foodCarInfo.clear();
                                adapter.notifyDataSetChanged();
                                paySuccessDialog = new PaySuccessDialog(HotelFoodCarActivity.this, new IDialogListener() {
                                    @Override
                                    public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                                        HotelFoodCarActivity.this.finish();
                                        overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                                    }
                                }, price);
                                DialogUtils.showDialog(paySuccessDialog);
                                if(Constants.currentLanguage.equals("zh")) {
                                    food_car_num.setText(dispAllNum() + "件商品");
                                    food_car_price.setText("总计" + dispAllPrice() + "元");
                                }else{
                                    food_car_num.setText("Total number :" + dispAllNum());
                                    food_car_price.setText("All:￥" + dispAllPrice());
                                }
                            }

                            @Override
                            public void onError() {
                                DialogUtils.dismissDialog(progressDialog);
                                ToastUtils.showShort("网络异常!");
                            }
                        });
                        payPresenter.requestDines(videoPayBean.getOrderNum());
                    }
                } else if (tag.equals(Constants.CANCEL)) {
                    DialogUtils.dismissDialog(dialog);
                }
            }
        }, R.string.account_toast);
        DialogUtils.showDialog(dialog);
    }


    private OrderDetailInfo setOderInfo(String custormName, String custormId) {

        List<OrderDetailInfo.OrderList> orderLists = new ArrayList<>();
        OrderDetailInfo info = new OrderDetailInfo();
        for (int i = 0; i < foodCarInfo.size(); i++) {
            if (foodCarInfo.get(i).getNum() != null) {
                if (!foodCarInfo.get(i).getNum().equals("0")) {
                    float price = Float.parseFloat(foodCarInfo.get(i).getPrice());
                    long num = Long.parseLong(foodCarInfo.get(i).getNum());
                    OrderDetailInfo.OrderList orderList = info.new OrderList();
                    orderList.setNum(num);
                    orderList.setPrice(foodCarInfo.get(i).getPrice());
                    orderList.setSubTotalPrice(String.valueOf(numberFormat(price * num)));
                    orderList.setServerId(Long.parseLong(foodCarInfo.get(i).getFoodId()));
                    orderLists.add(orderList);
                }
            }
        }
        info.setCustomerId(custormId);
        info.setCustomerName(custormName);
        info.setRoomId(roomId);
        info.setTotalPrice(dispAllPrice());
        info.setOrderDetail(orderLists);

        return info;
    }


    /**
     * 提交订单
     *
     * @param dataList
     */
    @Override
    public void onSuccess(BaseModel<List<OrderDetailBean>> dataList) {
        if (dataList.data == null || dataList.data.size() == 0) {
            ToastUtils.showShort("订单信息有误,请重新提交订单!");
            return;
        }

        OrderDetailBean orderDetailBean = dataList.data.get(0);

        price = orderDetailBean.getTotalPrice();
//        for (int i = 0; i < dataList.data.get(0).getOrderDetail().size(); i++) {
//            json += dataList.data.get(0).getOrderDetail().get(i).getServerId() + ",";
//        }
        List<FoodShopCarInfo> infos = new ArrayList<>();
        for (int i = 0; i < orderDetailBean.getOrderDetail().size(); i++) {
            FoodShopCarInfo info = new FoodShopCarInfo();
            info.setId(orderDetailBean.getOrderDetail().get(i).getServerId());
            info.setCount(orderDetailBean.getOrderDetail().get(i).getNum());
            infos.add(info);
        }

        String json = Convert.toJson(infos);

        foodPayPresenter.request(json, price, roomId, info.getCustomerId());
    }

    @Override
    public void onError() {
        DialogUtils.dismissDialog(progressDialog);
        ToastUtils.showShort("订单提交失败!");
    }


    /**
     * 请求支付
     */
    private class FoodPayListener implements IPresenterBase<List<VideoPayBean>> {
        VideoPayBean videoPayBean;

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            DialogUtils.dismissDialog(progressDialog);
            if (data != null && data.get(0) != null) {
                videoPayBean = data.get(0);
                orderNum = videoPayBean.getOrderNum();
                if (videoPayBean.getStatus() == 1) {//已支付
                    OkGo.getInstance().cancelAll();
//                    mHandler.removeMessages(GET_STATUS);
                    DialogUtils.dismissDialog(payDialog);
                } else {
                    //未支付
                    if (payMentChargeDialog == null) {
                        payMentChargeDialog = new PayMentChargeDialog(HotelFoodCarActivity.this, new IDialogListener() {
                            @Override
                            public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                                if (tag.equals(Constants.CONFIRM)) {
                                    DialogUtils.dismissDialog(payDialog);
                                    payDialog = null;
                                    setMediaPayDialog(0, videoPayBean);
                                } else if (tag.equals(Constants.CANCEL)) {
                                    DialogUtils.dismissDialog(payDialog);
                                    payDialog = null;
                                    setMediaPayDialog(1, videoPayBean);
                                } else if(tag.equals(Constants.THIRD)){
                                    accountDialog(videoPayBean);
                                }
                            }
                        }, price,2, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                    DialogUtils.showDialog(payMentChargeDialog);
                }

            } else {
                ToastUtils.showShort("服务器出现问题");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
            }
        }

        @Override
        public void onError() {
            ToastUtils.showShort("服务器出现问题");
            DialogUtils.dismissDialog(progressDialog);
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
        }
    }


    /**
     * 支付二维码弹窗
     */
    private void setMediaPayDialog(int payment, VideoPayBean videoPayBean) {
        if (payDialog == null) {
            try {
                Bitmap bitmap;
                if (payment == 0) {
                    bitmap = QrCodeCreate.createQRCode(videoPayBean.getAliCode(), getDimension(R.dimen.w_502), getDimension(R.dimen.h_10));
                } else {
                    bitmap = QrCodeCreate.createQRCode(videoPayBean.getWxCode(), getDimension(R.dimen.w_502), getDimension(R.dimen.h_10));
                }
                payDialog = new PayDialog(HotelFoodCarActivity.this, payment, bitmap
                        , new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, "￥" + price, 2);
                DialogUtils.showDialog(payDialog);
                OkHttpUtils.cancel();
//                mHandler.removeMessages(GET_STATUS);
//                mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);

            } catch (Exception e) {
                ToastUtils.showShort("二维码信息获取失败!");
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
            }
        } else if (payDialog.isShowing()) {
            OkHttpUtils.cancel();
//            mHandler.removeMessages(GET_STATUS);
//            mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
        }
    }


//    /**
//     * 查询支付状态
//     */
//    private class FoodPayStatusListener implements IPresenterBase<List<VideoPayBean>> {
//
//        @Override
//        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
//            List<VideoPayBean> data = dataList.data;
//            if (data != null && data.get(0) != null) {
//                if (data.get(0).getStatus() == 1) {//已支付
//                    DialogUtils.dismissDialog(payDialog);
//                    DialogUtils.dismissDialog(payMentChargeDialog);
//                    DBUtils.delete(HotelFoodCarActivity.this, FoodCarInfo.class);
//                    adapter.notifyDataSetChanged();
//                    paySuccessDialog = new PaySuccessDialog(HotelFoodCarActivity.this, new IDialogListener() {
//                        @Override
//                        public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
//                            HotelFoodCarActivity.this.finish();
//                        }
//                    }, price);
//                    DialogUtils.showDialog(paySuccessDialog);
//                } else {
//                    if (payDialog != null && payDialog.isShowing()) {
//                        OkGo.getInstance().cancelAll();
//                        mHandler.removeMessages(GET_STATUS);
//                        mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
//                    }
//                }
//
//            } else {
//                if (payDialog != null && payDialog.isShowing()) {
//                    OkGo.getInstance().cancelAll();
//                    mHandler.removeMessages(GET_STATUS);
//                    mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
//                }
//            }
//        }
//
//        @Override
//        public void onError() {
//            if (payDialog != null && payDialog.isShowing()) {
//                OkGo.getInstance().cancelAll();
//                mHandler.removeMessages(GET_STATUS);
//                mHandler.sendEmptyMessageDelayed(GET_STATUS, 1000);
//            }
//
//        }
//    }

    private class FoodPayStatusListener implements IPresenterBase<List<VideoPayBean>> {

        @Override
        public void onSuccess(BaseModel<List<VideoPayBean>> dataList) {
            List<VideoPayBean> data = dataList.data;
            if (data != null && data.get(0) != null) {
                if (data.get(0).getStatus() == 1) {//已支付
                    DialogUtils.dismissDialog(payDialog);
                    DialogUtils.dismissDialog(payMentChargeDialog);
                    DBUtils.delete(HotelFoodCarActivity.this, FoodCarInfo.class);
                    foodCarInfo.clear();
                    adapter.notifyDataSetChanged();
                    paySuccessDialog = new PaySuccessDialog(HotelFoodCarActivity.this, new IDialogListener() {
                        @Override
                        public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                            HotelFoodCarActivity.this.finish();
                            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                        }
                    }, price);
                    DialogUtils.showDialog(paySuccessDialog);
                    if(Constants.currentLanguage.equals("zh")) {
                        food_car_num.setText(dispAllNum() + "件商品");
                        food_car_price.setText("总计" + dispAllPrice() + "元");
                    }else{
                        food_car_num.setText("Total number :" + dispAllNum());
                        food_car_price.setText("All:￥" + dispAllPrice());
                    }
                }else{
                    ToastUtils.showShort("订单未支付!");
                }
            }
        }

        @Override
        public void onError() {
            //网络连接异常
            ToastUtils.showShort("订单未支付");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtils.dismissDialog(paySuccessDialog);
        DialogUtils.dismissDialog(payDialog);
        DialogUtils.dismissDialog(payMentChargeDialog);
//        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            if (!isKeyEnable) {
                isKeyEnable = true;
            } else {
                return true;
            }
        }
//        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
//            if(recyclerView.getSelectedPosition() == 1){
//                return true;
//            }
//        }
//
//        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){
//            if(recyclerView.getSelectedPosition() == recyclerView.getAdapter().getItemCount() - 2){
//                return true;
//            }
//        }

        if (event.getAction() == KeyEvent.ACTION_UP) {
            isKeyEnable = false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Subscribe
    public void onNumChanged(FoodCarInfo info) {
//        if (foodCarInfo.get(currentPosition).getNum().equals("0")) {
//            foodCarInfo.remove(currentPosition);
//            if (currentPosition == 1) {
//                currentPosition = 1;
//            } else {
//                currentPosition = currentPosition - 1;
//            }
//        } else {
//            foodCarInfo.set(currentPosition, info);
//        }
//        if (info != null && foodCarInfo.size() > 2) {
//            food_car_num.setText(dispAllNum() + "件商品");
//            food_car_price.setText("总计" + dispAllPrice() + "元");
//        } else {
//            car_empty_ll.setVisibility(View.VISIBLE);
//            food_car_num.setText(0 + "件商品");
//            food_car_price.setText("");
//            buttons[0].setFocusable(false);
//            buttons[1].setFocusable(false);
//            food_prompt.setText("");
//        }

        foodCarInfo.set(currentPosition, info);

        if(Constants.currentLanguage.equals("zh")) {
            food_car_num.setText(dispAllNum() + "件商品");
            food_car_price.setText("总计" + dispAllPrice() + "元");
        }else{
            food_car_num.setText("Total number :" + dispAllNum());
            food_car_price.setText("All:￥" + dispAllPrice());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatus(FoodPayStatus status){
        if(payDialog!= null && payDialog.isShowing() && status.isStatus()){
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            DBUtils.delete(HotelFoodCarActivity.this, FoodCarInfo.class);
            foodCarInfo.clear();
            adapter.notifyDataSetChanged();
            paySuccessDialog = new PaySuccessDialog(HotelFoodCarActivity.this, new IDialogListener() {
                @Override
                public void onClick(AppCompatDialog dialog, String tag, String str1, String str2) {
                    HotelFoodCarActivity.this.finish();
                    overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                }
            }, price);
            DialogUtils.showDialog(paySuccessDialog);
            if(Constants.currentLanguage.equals("zh")) {
                food_car_num.setText(dispAllNum() + "件商品");
                food_car_price.setText("总计" + dispAllPrice() + "元");
            }else{
                food_car_num.setText("Total number :" + dispAllNum());
                food_car_price.setText("All:￥" + dispAllPrice());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoviePayTimeOut(String timeOut) {
        if (payDialog != null && payDialog.isShowing() && timeOut.equals("timeOut")) {
            DialogUtils.dismissDialog(payDialog);
            DialogUtils.dismissDialog(payMentChargeDialog);
            foodPayStatusPresenter.request(this,orderNum);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initDialog(Activity activity, String msg) {
        progressDialog = new InstallDialog(activity,msg);
        DialogUtils.showDialog(progressDialog);
    }
}
