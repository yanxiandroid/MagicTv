package com.yht.iptv.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.adapter.DialogFragmentListFoodOrderAdapter;
import com.yht.iptv.adapter.FoodOrderPageAdapter;
import com.yht.iptv.adapter.FoodOrderViewpagerListAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.FoodOrderInfo;
import com.yht.iptv.presenter.FoodOrderPresenter;
import com.yht.iptv.tools.FixedSpeedScroller;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.SPUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class FoodOrderDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, IPresenterBase<List<FoodOrderInfo>>, DialogInterface.OnKeyListener {

    private Context context;
    private View oldView;
    private ListView listView;
    private List<FoodOrderInfo> foodOrderInfos;
    private DialogFragmentListFoodOrderAdapter adapter;
    private String room_id;
    private ViewPager view_pager;
    private final byte NORMAL_VALUE = 4;
    private List<View> views;
    private FoodOrderPageAdapter pageAdapter;
    private LayoutInflater inflater;
    private FixedSpeedScroller mScroller;
    private int currentPosition;
    private int currentViewPagerItem = 0;
    private TextView tv_goods_total;
    private TextView tv_goods_page;
    private TextView tv_total_price;
    //    private TextView tv_empty;
    private LinearLayout nothing_ll;
    private TextView nothing_text;
    private ImageView line_view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodOrderInfos = new ArrayList<>();
        views = new ArrayList<>();
        room_id = (String) SPUtils.get(context, Constants.ROOM_ID, "");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_food_order, null);
        this.inflater = LayoutInflater.from(context);
        listView = (ListView) view.findViewById(R.id.listview);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
//        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        nothing_ll = (LinearLayout) view.findViewById(R.id.nothing_ll);
        nothing_text = (TextView) view.findViewById(R.id.nothing_text);
        line_view = (ImageView) view.findViewById(R.id.line_view);
        adapter = new DialogFragmentListFoodOrderAdapter(context, foodOrderInfos);
        tv_goods_total = (TextView) view.findViewById(R.id.tv_goods_total);
        tv_goods_page = (TextView) view.findViewById(R.id.tv_goods_page);
        tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
        listView.setAdapter(adapter);
        listView.setOnItemSelectedListener(this);
        pageAdapter = new FoodOrderPageAdapter(views);
        view_pager.setAdapter(pageAdapter);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(view_pager.getContext(), new DecelerateInterpolator());
            mField.set(view_pager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view_pager.setCurrentItem(0);
        mScroller.setmDuration(300);
        FoodOrderPresenter presenter = new FoodOrderPresenter(this);
        presenter.request(room_id, this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        return dialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        getDialog().setOnKeyListener(this);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.width = (int) getResources().getDimension(R.dimen.w_1800);
        params.height = (int) getResources().getDimension(R.dimen.w_558);
        params.y = (int) getResources().getDimension(R.dimen.h_166);
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.cancel();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentPosition = position;
        currentViewPagerItem = 0;
        setText();
        view_pager.setCurrentItem(currentViewPagerItem);
        listView.setSelectionFromTop(position, (int) getResources().getDimension(R.dimen.h_150));
        if (oldView != null) {
            TextView old_tv_title = (TextView) oldView.findViewById(R.id.tv_title);
            ImageView old_iv_select = (ImageView) oldView.findViewById(R.id.iv_select);
            old_tv_title.setTextColor(Color.parseColor("#4cffffff"));
            old_iv_select.setVisibility(View.INVISIBLE);
        }
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        ImageView iv_select = (ImageView) view.findViewById(R.id.iv_select);
        tv_title.setTextColor(Color.parseColor("#ffffff"));
        iv_select.setVisibility(View.VISIBLE);
        oldView = view;
        initViewPager();
        initListView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onSuccess(BaseModel<List<FoodOrderInfo>> dataList) {
        nothing_ll.setVisibility(View.GONE);
        line_view.setVisibility(View.VISIBLE);
        //更新list adapter
        List<FoodOrderInfo> data = dataList.data;
        foodOrderInfos.clear();
        foodOrderInfos.addAll(data);
        adapter.notifyDataSetChanged();

        setText();

        initViewPager();

        initListView();

    }

    @Override
    public void onError() {
//        listView.setEmptyView(tv_empty);
        if(line_view != null && nothing_ll != null && nothing_text != null) {
            line_view.setVisibility(View.GONE);
            nothing_ll.setVisibility(View.VISIBLE);
            nothing_text.setText(getResources().getString(R.string.order_nothing));
        }
    }

    /**
     * 返回viewpager lit数据总页数
     */
    private int totalViewPagers() {
        if(foodOrderInfos.size() != 0 && foodOrderInfos.size() > currentPosition) {
            FoodOrderInfo foodOrderInfo = foodOrderInfos.get(currentPosition);

            //以4个一行为基准
            int totalSize;
            int shi = foodOrderInfo.getDinesList().size() / NORMAL_VALUE;
            int ge = foodOrderInfo.getDinesList().size() % NORMAL_VALUE;
            if (ge == 0) {
                totalSize = shi;
            } else {
                totalSize = shi + 1;
            }

            return totalSize;
        }else{
            return 0;
        }
    }

    private void initViewPager() {
        //更新Viewpager adpter
        views.clear();
        for (int i = 0; i < totalViewPagers(); i++) {
            View view = inflater.inflate(R.layout.viewpager_food_order, null);
            views.add(view);
        }
        pageAdapter.notifyDataSetChanged();

    }

    private void initListView() {
        for (int i = 0; i < views.size(); i++) {
            ListView listView = (ListView) views.get(i).findViewById(R.id.listview);
            FoodOrderViewpagerListAdapter adapter = new FoodOrderViewpagerListAdapter(context, getViewpagerListSize().get(i));
            listView.setAdapter(adapter);
        }
    }


    private List<List<FoodOrderInfo.DinesListBean>> getViewpagerListSize() {
        FoodOrderInfo foodOrderInfo = foodOrderInfos.get(currentPosition);
        List<List<FoodOrderInfo.DinesListBean>> list = new ArrayList<>();
        List<FoodOrderInfo.DinesListBean> list2;
        for (int i = 0; i < totalViewPagers(); i++) {
            list2 = new ArrayList<>();
            if (foodOrderInfo.getDinesList().size() > 4 * (i + 1)) {
                for (int j = NORMAL_VALUE * i; j < NORMAL_VALUE * i + NORMAL_VALUE; j++) {
                    list2.add(foodOrderInfo.getDinesList().get(j));
                }
            } else {
                for (int k = NORMAL_VALUE * i; k < foodOrderInfo.getDinesList().size(); k++) {
                    list2.add(foodOrderInfo.getDinesList().get(k));
                }
            }
            list.add(list2);
        }
        return list;
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(views.size() > 0) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (currentViewPagerItem < totalViewPagers() - 1) {
                    currentViewPagerItem++;
                    view_pager.setCurrentItem(currentViewPagerItem);
                }
                tv_goods_page.setText("< " + (currentViewPagerItem + 1) + "/" + totalViewPagers() + " >");
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (currentViewPagerItem > 0) {
                    currentViewPagerItem--;
                    view_pager.setCurrentItem(currentViewPagerItem);
                }
                tv_goods_page.setText("< " + (currentViewPagerItem + 1) + "/" + totalViewPagers() + " >");
                return true;
            }
        }
        return false;
    }

    /**
     * 获取总商品数量
     */
    /*private int getGoodsNum(){
        int totalNum = 0 ;
        List<FoodOrderInfo.DinesListBean> dinesList = foodOrderInfos.get(currentPosition).getDinesList();
        for (int i = 0 ; i < dinesList.size();i++){
            totalNum += dinesList.get(i).getCount();
        }
        return totalNum;
    }*/

    /**
     * 设置属性
     */
    private void setText() {
        tv_goods_total.setText("共" + foodOrderInfos.get(currentPosition).getDinesList().size() + "件商品");
        tv_goods_page.setText("< " + (currentViewPagerItem + 1) + "/" + totalViewPagers() + " >");
        tv_total_price.setText(textColor());
    }

    private SpannableStringBuilder textColor() {
        String content = "总金额：" + foodOrderInfos.get(currentPosition).getPrice() + "元";
        SpannableStringBuilder builders = new SpannableStringBuilder(content);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#f69b4d"));
        int start = content.indexOf("：");
        builders.setSpan(span, start + 1, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builders;
    }
}
