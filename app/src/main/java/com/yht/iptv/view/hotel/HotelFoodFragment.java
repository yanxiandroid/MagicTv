package com.yht.iptv.view.hotel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.adapter.HotelFoodViewPagerAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.HotelFoodListBean;
import com.yht.iptv.model.HotelFoodTypeBean;
import com.yht.iptv.presenter.HotelFoodListPresenter;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.tools.ScalePageTransformer;
import com.yht.iptv.tools.VerticalScrollTextView;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.ToastUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 客房点餐通用Fragment
 * Created by Q on 2017/10/20.
 */

public class HotelFoodFragment extends Fragment implements IPresenterBase<List<HotelFoodListBean>>, HotelFoodViewPagerAdapter.OnItemClickListener, ViewPager.OnPageChangeListener {
    private Context context;
    private TextView food_type, food_name, food_price, current_num, total_num;
    private ViewPager mViewPager;
    private VerticalScrollTextView food_introduction;

    private List<HotelFoodListBean> dataList;
    private String type_name;
    private String restaurantId;
    private FixedSpeedScroller mScroller;
    private addCarLitener addCarLitener;
    private RelativeLayout rl_main;
    private float[] mCurrentPosition = new float[2];

    private HotelFoodTypeBean typeBean;
    private RelativeLayout rl_title;
    private LinearLayout nothing_ll;
    private TextView nothing_text;

    public HotelFoodFragment() {

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addCarLitener = (HotelFoodFragment.addCarLitener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeBean = (HotelFoodTypeBean) bundle.getSerializable("typeList");
            restaurantId = bundle.getString("restaurantId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Constants.verticalscrolltext = "food";
        View view = inflater.inflate(R.layout.fragment_hotel_food, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        nothing_ll = (LinearLayout) view.findViewById(R.id.nothing_ll);
        nothing_text = (TextView) view.findViewById(R.id.nothing_text);
        food_type = (TextView) view.findViewById(R.id.hotel_food_type_text);
        rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
        rl_title.setAlpha(0f);
        rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
        food_name = (TextView) view.findViewById(R.id.hotel_food_name);
        food_price = (TextView) view.findViewById(R.id.hotel_food_price);
        current_num = (TextView) view.findViewById(R.id.hotel_food_current_num);
        total_num = (TextView) view.findViewById(R.id.hotel_food_total_num);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        food_introduction = (VerticalScrollTextView) view.findViewById(R.id.hotel_food_introduction);
        dataList = new ArrayList<>();
        HotelFoodListPresenter presenter = new HotelFoodListPresenter(context, this);
        presenter.request(this, restaurantId, typeBean.getTypeEn());

    }

    private void initData(int position) {
        food_name.setText(dataList.get(position).getName());
        food_name.setSelected(true);
        food_price.setText("￥:" + dataList.get(position).getPrice());
        current_num.setText(position + 1 + "");
        total_num.setText("/" + dataList.size());
        food_introduction.setText(dataList.get(position).getDescription());
        AnimationUtils.alphaAnimation(rl_title,300,0f,1f);
    }

    private void initViewPager() {


        HotelFoodViewPagerAdapter adapter = new HotelFoodViewPagerAdapter(context, dataList);
        mViewPager.setAdapter(adapter);
        adapter.setOnItemCLickLitener(this);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(mViewPager.getContext(), new DecelerateInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mScroller.setmDuration(300);
        mViewPager.setCurrentItem(0);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onSuccess(BaseModel<List<HotelFoodListBean>> dataList) {
        if (dataList.data == null || dataList.data.size() == 0) {
//            getActivity().finish();
//            ToastUtils.showShort(getResources().getString(R.string.food_nothing));
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_ll.setBackgroundResource(R.drawable.hotel_food_bg);
            nothing_text.setText(getResources().getString(R.string.food_nothing));
            return;
        }
        if (dataList.data.size() == 1 && dataList.data.get(0) == null) {
//            getActivity().finish();
//            ToastUtils.showShort(getResources().getString(R.string.food_nothing));
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_ll.setBackgroundResource(R.drawable.hotel_food_bg);
            nothing_text.setText(getResources().getString(R.string.food_nothing));
            return;
        }
        this.dataList.clear();
        this.dataList.addAll(dataList.data);
        nothing_ll.setVisibility(View.GONE);
        if (Constants.currentLanguage.equals("en")) {
            food_type.setText(typeBean.getTypeEn());
            food_type.setSelected(true);
        } else {
            food_type.setText(typeBean.getType());
            food_type.setSelected(true);
        }

        initData(0);
        initViewPager();
    }

    @Override
    public void onError() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    long mKeyTime;

    @Override
    public void onItemClick(final int position) {
        if(Constants.mainPageInfo.getPaymentSetting().getDines() == 0){
            ToastUtils.showShort("暂不支持下单,抱歉!");
            return;
        }
        int allNumber = addCarLitener.getAllNumber();
        if (allNumber >= 99) {
            ToastUtils.showShort("商品总数不能大于99件");
            return;
        }
        //间隔1s点击一次
        if (System.currentTimeMillis() - mKeyTime < 1000) {
            return;
        }
        mKeyTime = System.currentTimeMillis();

        //得到商品的坐标
        //获取商品信息
        RelativeLayout primaryItem = (RelativeLayout) ((HotelFoodViewPagerAdapter) mViewPager.getAdapter()).getPrimaryItem();
        final ImageView item_img = (ImageView) primaryItem.findViewById(R.id.item_img);

        final ImageView goods = new ImageView(context);
        goods.setImageDrawable(item_img.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.w_600), (int) context.getResources().getDimension(R.dimen.h_300));
        rl_main.addView(goods, params);

        //获取rl view

        int[] parentLocation = new int[2];
        rl_main.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        item_img.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        final View carIcon = addCarLitener.getCarIcon();
        carIcon.getLocationInWindow(endLoc);

        //        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
//        float startX = startLoc[0] - parentLocation[0] + item_img.getWidth() / 2;
//        float startY = startLoc[1] - parentLocation[1] + item_img.getHeight() / 2;

        float startX = startLoc[0];
        float startY = startLoc[1];

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
//        float toX = endLoc[0] - parentLocation[0] + carIcon.getWidth() / 5;
//        float toY = endLoc[1] - parentLocation[1];

        float toX = endLoc[0] - (int) context.getResources().getDimension(R.dimen.w_250);
        float toY = endLoc[1] - (int) context.getResources().getDimension(R.dimen.h_100);

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        final PathMeasure mPathMeasure = new PathMeasure(path, false);


        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        AnimatorSet set = new AnimatorSet();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(goods, "scaleX", 1.0f, 0.01f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(goods, "scaleY", 1.0f, 0.01f);
//        valueAnimator.setDuration(1000);
        // 匀速线性插值器
//        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
//        valueAnimator.start();
        set.playTogether(valueAnimator, scaleX, scaleY);
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(1000);
        set.start();
//      六、动画结束后的处理
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                rl_main.removeView(goods);
                addCarLitener.addCar(dataList.get(position));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        initData(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public int onKeyLeft() {
        if (mViewPager.getCurrentItem() == 0) {
            //最后一页
            return 1;
        }
        return 0;
    }


    public int onKeyRight() {
        if (mViewPager.getCurrentItem() == dataList.size() - 1) {
            //最后一页
            return 1;
        }
        return 0;
    }


    public interface addCarLitener {

        void addCar(HotelFoodListBean foodListBean);

        View getCarIcon();

        int getAllNumber();
    }

}
