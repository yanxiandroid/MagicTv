package com.yht.iptv.view.hotel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.adapter.GeneralFragmentAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.FoodCarInfo;
import com.yht.iptv.model.HotelFoodListBean;
import com.yht.iptv.model.HotelFoodTypeBean;
import com.yht.iptv.presenter.HotelFoodTypePresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.tools.VerticalViewPager;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ShowImageUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import org.xutils.db.sqlite.WhereBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 客房点餐
 * Created by Q on 2017/10/23.
 */

public class HotelFoodActivity extends BaseActivity implements ViewPager.OnPageChangeListener, HotelFoodFragment.addCarLitener, IPresenterBase<List<HotelFoodTypeBean>> {
    private ImageView food_up, food_down;
    private TextView car_num;
    private VerticalViewPager viewpager;
    private List<Fragment> fragmentsList;
    private GeneralFragmentAdapter adapter;
    private FixedSpeedScroller mScroller;
    private String restaurantId;
    private int currentItem = 0;
    private List<FoodCarInfo> dataList;
    private int allNumber = 0;
    private ImageView iv_food_car_icon;

    private List<HotelFoodTypeBean> typeBeanList;

    private LinearLayout nothing_ll;
    private TextView nothing_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_START);
        bundle.putString("behaviour",Constants.CATERING);
        ServiceUtils.startService(PageRecordService.class,bundle);

        restaurantId = getIntent().getStringExtra("restaurantId");
        setContentView(R.layout.activity_hotel_food);
        initView();
    }

    private void initView() {

        viewpager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        ImageView iv_bg = (ImageView) findViewById(R.id.iv_bg);
        car_num = (TextView) findViewById(R.id.car_num_text);
        iv_food_car_icon = (ImageView) findViewById(R.id.iv_food_car_icon);
        nothing_ll = (LinearLayout) findViewById(R.id.nothing_ll);
        nothing_text = (TextView) findViewById(R.id.nothing_text);
        ShowImageUtils.showImageView(this, R.drawable.hotel_food_bg, iv_bg);

        typeBeanList = new ArrayList<>();
        HotelFoodTypePresenter presenter = new HotelFoodTypePresenter(this, this);
        presenter.request(this, restaurantId);
    }

    private void initData() {

        fragmentsList = new ArrayList<>();

        for (int i = 0; i < typeBeanList.size(); i++) {
            HotelFoodFragment hotelFoodFragment = new HotelFoodFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("typeList", typeBeanList.get(i));
            bundle.putString("restaurantId", restaurantId);
            hotelFoodFragment.setArguments(bundle);
            fragmentsList.add(hotelFoodFragment);
        }

        adapter = new GeneralFragmentAdapter(getSupportFragmentManager(), fragmentsList);
        viewpager.setAdapter(adapter);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(viewpager.getContext(), new AccelerateInterpolator());
            mField.set(viewpager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mScroller.setmDuration(500);
        viewpager.setCurrentItem(currentItem);
        viewpager.addOnPageChangeListener(this);


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_END);
            ServiceUtils.startService(PageRecordService.class,bundle);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,60 * 1000);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,60 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    Handler handler = new Handler();

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        food_up = (ImageView) adapter.getItem(viewpager.getCurrentItem()).getView().findViewById(R.id.hotel_food_up);
        food_down = (ImageView) adapter.getItem(viewpager.getCurrentItem()).getView().findViewById(R.id.hotel_food_down);
        if (currentItem == 0) {
            food_up.setVisibility(View.GONE);
        } else {
            food_up.setVisibility(View.VISIBLE);
        }

        if (currentItem == typeBeanList.size() - 1) {
            food_down.setVisibility(View.GONE);
        } else {
            food_down.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (viewpager.getCurrentItem() == currentItem) {
                viewpager.setCurrentItem((currentItem + 1 >= typeBeanList.size() ? currentItem : currentItem + 1));
                return true;
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (viewpager.getCurrentItem() == (currentItem == 0 ? currentItem + 1 : currentItem)) {
                viewpager.setCurrentItem(currentItem - 1);
                return true;
            }
        }


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE) {
                finish();
                overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                return true;
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if(adapter != null) {
                HotelFoodFragment hotelFoodFragment = (HotelFoodFragment) adapter.getItem(viewpager.getCurrentItem());
                int result = hotelFoodFragment.onKeyRight();
                if (result == 1) {
                    return true;
                }
            }
        }


        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            if(adapter != null) {
                HotelFoodFragment hotelFoodFragment = (HotelFoodFragment) adapter.getItem(viewpager.getCurrentItem());
                int result = hotelFoodFragment.onKeyLeft();
                if (result == 1) {
                    return true;
                }
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            Intent in = new Intent(HotelFoodActivity.this, HotelFoodCarActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", restaurantId);
//            bundle.putParcelableArrayList("dataList", (ArrayList<? extends Parcelable>) dataList);
            in.putExtras(bundle);
            startActivity(in);
            overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void addCar(HotelFoodListBean foodListBean) {

        String price = foodListBean.getPrice();
        if (!(price != null && !price.equals("") && price.matches("^[+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"))) {
            ToastUtils.showShort("价格格式不正确,无法加入购物车!");
            return;
        }
        //查询数据库
        WhereBuilder builder = WhereBuilder.b("foodId", "=", foodListBean.getId()).and("restaurantId", "=", restaurantId);
        //查询第一条数据
        FoodCarInfo foodCarInfo = DBUtils.find(this, FoodCarInfo.class, builder);

        if (foodCarInfo == null) {
            FoodCarInfo info = new FoodCarInfo();
            info.setName(foodListBean.getName());
            info.setNum("1");
            info.setPrice(foodListBean.getPrice());
            if (foodListBean.getFileUpload() != null) {
                info.setImage(foodListBean.getFileUpload().getPath());
            }
            info.setFoodId(String.valueOf(foodListBean.getId()));
            info.setRestaurantId(String.valueOf(foodListBean.getRestaurantId()));
            info.setId(String.valueOf(foodListBean.getId()) + String.valueOf(foodListBean.getRestaurantId()));
            DBUtils.save(HotelFoodActivity.this, info);
        } else {

            int num = Integer.parseInt(foodCarInfo.getNum()) + 1;
            if (num <= 99) {
                foodCarInfo.setNum(String.valueOf(num));
                DBUtils.update(this, foodCarInfo, "num");
            } else {
                ToastUtils.showShort("已超过个人购买商品数量最大值,不能加入购物车!");
                num = 99;
                foodCarInfo.setNum(String.valueOf(num));
                DBUtils.update(this, foodCarInfo, "num");
                return;
            }

        }
        allNumber += 1;
        car_num.setText(allNumber + "");
        car_num.setVisibility(View.VISIBLE);
        String content = "成功把 " + foodListBean.getName() + " 加入购物车";
        SpannableStringBuilder builders = new SpannableStringBuilder(content);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#f69b4d"));
        int start = content.indexOf(foodListBean.getName());
        builders.setSpan(span, start, foodListBean.getName().length() + start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ToastUtils.showLong(builders);
    }

    @Override
    public View getCarIcon() {
        return iv_food_car_icon;
    }

    @Override
    public int getAllNumber() {
        return allNumber;
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataList = DBUtils.findAll(this, FoodCarInfo.class);
        if (dataList != null && dataList.size() != 0) {
            car_num.setVisibility(View.VISIBLE);
            car_num.setText(dispAllNum() + "");
            allNumber = (int) dispAllNum();
        } else {
            car_num.setVisibility(View.GONE);
            allNumber = 0;
        }

    }


    /**
     * 处理总数量
     */
    private long dispAllNum() {
        long allNum = 0;
        for (int i = 0; i < dataList.size(); i++) {
            long num = Integer.parseInt(dataList.get(i).getNum());
            allNum += num;
        }
        return allNum;
    }

    @Override
    public void onSuccess(BaseModel<List<HotelFoodTypeBean>> dataList) {
        if (dataList.data == null || dataList.data.size() == 0) {
//            finish();
//            ToastUtils.showShort("该餐厅暂无菜品提供!");
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_ll.setBackgroundResource(R.drawable.hotel_food_bg);
            nothing_text.setText(getResources().getString(R.string.food_nothing));
            return;
        }
        if (dataList.data.size() == 1 && dataList.data.get(0) == null) {
//            finish();
//            ToastUtils.showShort("菜品信息错误!");
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_ll.setBackgroundResource(R.drawable.hotel_food_bg);
            nothing_text.setText(getResources().getString(R.string.food_nothing));
            return;
        }
        this.typeBeanList.clear();
        this.typeBeanList.addAll(dataList.data);
        nothing_ll.setVisibility(View.GONE);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.cancel();
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class,bundle);
    }

    @Override
    public void onError() {
        finish();
        overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
    }
}
