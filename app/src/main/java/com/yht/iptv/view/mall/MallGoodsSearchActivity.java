package com.yht.iptv.view.mall;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lzy.okgo.OkGo;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterMallBase;
import com.yht.iptv.model.BaseMallModel;
import com.yht.iptv.model.MainPageInfo;
import com.yht.iptv.model.MallGoodsListInfo;
import com.yht.iptv.presenter.MallListPresenter;
import com.yht.iptv.tools.CustomListViewTv;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/21.
 */


public class MallGoodsSearchActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, TextWatcher, AdapterView.OnItemClickListener, View.OnClickListener, ViewTreeObserver.OnGlobalFocusChangeListener {

    private EditText et_search;
    private CustomListViewTv listview;
    private ImageView iv_cover;
    private List<String> list1;
    private GridView gridView;
    private Button bt_remove;
    private Button bt_clear;

    private MallGoodsSearchListAdapter mallGoodsSearchListAdapter;
    private MainUpView mainUpView;
    private LinearLayout ll_main;
    private EffectNoDrawBridge bridget;
    private View mOldFocus;
    private int totalPage  = 1;

    private boolean isLoading;

    private MyHandler handler;

    private List<MallGoodsListInfo.GoodsListBean> mallGoodsListInfos;//商品类型

    private int currentPage = 1;
    private final int pagSize = 20;
    private MallListPresenter mallListPresenter;
    private MainPageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_goods_search);

        initData();

        initViews();

        initMove();
        info = DBUtils.find(this, MainPageInfo.class);
        if(info == null){
            ToastUtils.showShort("用户信息为空,请返回首页获取!");
            return;
        }

        mallListPresenter = new MallListPresenter(this, new GiftDetailListener());
        mallListPresenter.request(this, "", String.valueOf(info.getId()), String.valueOf(currentPage), String.valueOf(pagSize), "");
    }


    private void initData() {
        list1 = new ArrayList<>();

        mallGoodsListInfos = new ArrayList<>();

        currentPage = 1;

        for (int i = 'A'; i <= 'Z'; i++) {
            list1.add("" + (char) i);
        }
        for (int i = '0'; i <= '9'; i++) {
            list1.add("" + (char) i);
        }

        handler = new MyHandler(this);

    }

    private void initViews() {

        et_search = (EditText) findViewById(R.id.et_search);
        listview = (CustomListViewTv) findViewById(R.id.listview);
        bt_remove = (Button) findViewById(R.id.bt_remove);
        bt_clear = (Button) findViewById(R.id.bt_clear);

        mainUpView = (MainUpView) findViewById(R.id.mainUpView);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_main.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        mallGoodsSearchListAdapter = new MallGoodsSearchListAdapter(mallGoodsListInfos, this);
        SearchGridViewAdapter gridViewAdapter = new SearchGridViewAdapter(this, list1);

        listview.setAdapter(mallGoodsSearchListAdapter);
        gridView.setAdapter(gridViewAdapter);

        listview.setOnItemSelectedListener(this);
        listview.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemSelectedListener(this);
        et_search.addTextChangedListener(this);
        bt_remove.setOnClickListener(this);
        bt_clear.setOnClickListener(this);

        gridView.requestFocus();

        listview.postDelayed(new Runnable() {
            @Override
            public void run() {
                gridView.setSelection(0);
            }
        }, 300);

    }

    //条目被选中
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent instanceof ListView) {
            bridget.setVisibleWidget(false);
            bridget.setTranDurAnimTime(200);
            mallGoodsSearchListAdapter.setCurrentPos(position);
            mallGoodsSearchListAdapter.notifyDataSetChanged();
            listview.setSelectionFromTop(position, getDimension(R.dimen.h_401));
            Message message = Message.obtain();
            message.obj = view;
            message.what = 0;
            handler.sendMessageDelayed(message, 10);
//            movieDetail(position);

            if (totalPage > 1 && !isLoading && parent.getLastVisiblePosition() == (parent.getCount() - 1)) {
                //到底了
                if (currentPage == totalPage) {
                    //加载完成
                    //已经加载完成
                } else if (parent.getLastVisiblePosition() == (parent.getCount() - 1)) {
                    isLoading = true;
                    currentPage++;
                    //获取商品详情
                    showMallRequest("");
                }
            }

        } else if (parent instanceof GridView) {
            bridget.setVisibleWidget(false);
            bridget.setTranDurAnimTime(200);
            mainUpView.setFocusView(view, mOldFocus, 1.5f);
            mOldFocus = view;
        }
    }

    private void showMallRequest(String keywords) {
        OkGo.getInstance().cancelTag(this);
        mallListPresenter.request(this, "", String.valueOf(info.getId()) , String.valueOf(currentPage), String.valueOf(pagSize), keywords);
    }

//    //电影详情
//    private void movieDetail(int position) {
//        if(dataList.get(position) != null) {
//            if(dataList.get(position).getFileUpload() != null) {
//                //BitmapUtils.loadingImage(iv_cover, dataList.get(position).getFileUpload().getPath(),getDimension(R.dimen.px282),getDimension(R.dimen.px423));
//                //ImageLoaderUtils.getImageLoaderUtils().displayImage(dataList.get(position).getFileUpload().getPath(), iv_cover);
//                GlideUtils.getGlideUtils().displayImage(this,dataList.get(position).getFileUpload().getPath(), iv_cover);
//            }else{
//                //ImageLoaderUtils.getImageLoaderUtils().displayFromDrawable(R.drawable.movie_photo_failed,iv_cover);
//                GlideUtils.getGlideUtils().displayImage(this,R.drawable.movie_photo_failed,iv_cover);
//            }
//        }
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //字体改变监听
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentPage = 1;
        showMallRequest(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent instanceof ListView) {
            if (mallGoodsListInfos.get(position) != null) {
                Intent intent = new Intent(this, MallGoodsDetailActivity.class);
                intent.putExtra("goodsId", mallGoodsListInfos.get(position).getId());
                startActivity(intent);
            }
        } else if (parent instanceof GridView) {
            String beforeStr = et_search.getText().toString();
            et_search.setText(beforeStr + list1.get(position));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_remove:
                String keywords = et_search.getText().toString();
                if (!keywords.equals("")) {
                    //删除最后一个字符
                    String subKeywords = keywords.substring(0, keywords.length() - 1);
                    et_search.setText(subKeywords);
                }
                break;
            case R.id.bt_clear:
                //设置文本框内容为空
                et_search.setText("");
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (getCurrentFocus() instanceof GridView) {
                GridView gridView = (GridView) getCurrentFocus();
                int numColumns = gridView.getNumColumns();
                if (gridView.getSelectedItemPosition() < numColumns) {
                    return true;
                }
            }

        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (getCurrentFocus() instanceof ListView) {
                ListView listView = (ListView) getCurrentFocus();
                int count = listView.getAdapter().getCount();
                if (listView.getSelectedItemPosition() == count - 1) {
                    return true;
                }
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (getCurrentFocus() instanceof ListView) {
                gridView.requestFocus();
                return true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (getCurrentFocus() instanceof GridView) {
                GridView gridView = (GridView) getCurrentFocus();
                int numColumn = gridView.getNumColumns();
                int selectPos = gridView.getSelectedItemPosition();
                int count = gridView.getAdapter().getCount();
                if (selectPos != -1) {
                    if ((selectPos + 1) >= count || (selectPos + 1) % numColumn == 0) {//最后一个//最右边
                        listview.requestFocus();
                        return true;
                    }
                }
            }
            if (getCurrentFocus() instanceof Button) {
                if (getCurrentFocus().getId() == R.id.bt_clear) {
                    return true;
                }
            }
        }

        return super.dispatchKeyEvent(event);
    }

    private void initMove() {
        // 建议使用 noDrawBridge.
        mainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
        mainUpView.setUpRectResource(R.drawable.checked_frame); // 设置移动边框的图片.
        mainUpView.setDrawUpRectPadding(new Rect(-getDimension(R.dimen.w_25), -getDimension(R.dimen.h_25), -getDimension(R.dimen.w_25), -getDimension(R.dimen.h_25))); // 边框图片设置间距.
        bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
        bridget.setTranDurAnimTime(0);
        bridget.setVisibleWidget(false);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (newFocus instanceof Button) {
            bridget.setVisibleWidget(true);
            mainUpView.setUnFocusView(mOldFocus);
        } else {
            bridget.setVisibleWidget(false);
            if (newFocus instanceof ListView) {
                mainUpView.setDrawUpRectPadding(new Rect(getDimension(R.dimen.w_18), getDimension(R.dimen.h_18), getDimension(R.dimen.w_18), getDimension(R.dimen.h_18))); // 边框图片设置间距.
                View selectedView = ((ListView) newFocus).getSelectedView();
                int position = ((ListView) newFocus).getSelectedItemPosition();
                if (position == -1) {
                    ((ListView) newFocus).setSelectionFromTop(0, getDimension(R.dimen.h_401));
                    selectedView = ((ListView) newFocus).getSelectedView();
                }
                if (selectedView != null) {
                    selectedView.bringToFront();
                }
                mallGoodsSearchListAdapter.setCurrentPos(0);
                mallGoodsSearchListAdapter.notifyDataSetChanged();
                Message message = Message.obtain();
                message.obj = selectedView;
                message.what = 0;
                handler.sendMessageDelayed(message, 10);
                bridget.setTranDurAnimTime(200);
                return;
            }
            if (newFocus instanceof GridView) {
                mallGoodsSearchListAdapter.setCurrentPos(-1);
                mallGoodsSearchListAdapter.notifyDataSetChanged();
                mainUpView.setDrawUpRectPadding(new Rect(-getDimension(R.dimen.w_18), -getDimension(R.dimen.h_18), -getDimension(R.dimen.w_18), -getDimension(R.dimen.h_18))); // 边框图片设置间距.
                View selectedView = ((GridView) newFocus).getSelectedView();
                if (selectedView != null) {
                    selectedView.bringToFront();
                }
                mainUpView.setFocusView(selectedView, mOldFocus, 1.5f);
                mOldFocus = selectedView;
                bridget.setTranDurAnimTime(200);
                return;
            }
        }
        mOldFocus = newFocus;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

//    @Override
//    public void onSuccess(BaseModel<List<NewMovieDetailBean>> dataList) {
//        if (dataList.data.size() <= 0) {
//            ToastUtils.showToast(this,"抱歉，该影片没有被搜索到!");
//            if(currentPage == 1 || currentPage == 0) {
//                this.dataList.clear();
//                mallGoodsSearchListAdapter.notifyDataSetChanged();
//            }
//            return;
//        }
//        if(dataList.data.get(0)==null){
//            ToastUtils.showToast(this,"视频信息错误!");
//            return;
//        }
//        this.totalPage = dataList.totalPage;
//        if(currentPage == 1 || currentPage == 0) {
//            this.dataList.clear();
//        }
//        this.dataList.addAll(dataList.data);
//        mallGoodsSearchListAdapter.notifyDataSetChanged();
////        movieDetail(0);
//        isLoading = false;
//    }
//
//    @Override
//    public void onError() {
//        ToastUtils.showToast(this,"网络连接异常!");
//        isLoading = false;
//    }

    /**
     * 获取商品类型详情
     */
    private class GiftDetailListener implements IPresenterMallBase<MallGoodsListInfo> {

        @Override
        public void onSuccess(BaseMallModel<MallGoodsListInfo> dataList) {
            List<MallGoodsListInfo.GoodsListBean> goods_list = dataList.result.getGoods_list();
//            mallGoodsListInfos.clear();
//            mallGoodsListInfos.addAll(goods_list);
//            mallGoodsSearchListAdapter.notifyDataSetChanged();

            if (dataList.result.getGoods_list().size() <= 0) {

                if (currentPage == 1 || currentPage == 0) {
                    mallGoodsListInfos.clear();
                    mallGoodsSearchListAdapter.notifyDataSetChanged();
                }
                return;
            }

//            totalPage = dataList.result.getGoods_list();
            if (currentPage == 1 || currentPage == 0) {
                mallGoodsListInfos.clear();
            }
            mallGoodsListInfos.addAll(goods_list);
            mallGoodsSearchListAdapter.notifyDataSetChanged();
//        movieDetail(0);
            isLoading = false;
        }

        @Override
        public void onError() {
            ToastUtils.showShort("抱歉，没有找到所需的商品!");
        }
    }


//    private List<String> getItem2() {
//        String[] stringArray = getResources().getStringArray(R.array.mall_goods_test2);
//        List<String> stringList = new ArrayList<>();
//        for (int i = 0; i < stringArray.length; i++) {
//            stringList.add(stringArray[i]);
//        }
//        return stringList;
//    }


    private static class MyHandler extends Handler {

        private WeakReference<MallGoodsSearchActivity> activityWeakReference;

        public MyHandler(MallGoodsSearchActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MallGoodsSearchActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    activity.moveRact((View) msg.obj);
                    break;
            }
        }
    }

    public void moveRact(View view) {
        mainUpView.setFocusView(view, mOldFocus, 1.0f);
        mOldFocus = view;
    }
}

