package com.yht.iptv.view.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yht.iptv.R;
import com.yht.iptv.adapter.DialogFragmentMainRecAdapter;
import com.yht.iptv.model.MainUIBean;
import com.yht.iptv.model.MainUIModel;
import com.yht.iptv.model.TitleBean;
import com.yht.iptv.tools.MyLinearLayoutManager;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.GeneralActivity;
import com.yht.iptv.view.hotel.HotelFoodActivity;
import com.yht.iptv.view.hotel.HotelGeneralActivity;
import com.yht.iptv.view.movie.MovieDetailActivity;
import com.yht.iptv.view.near.BusTimetableFragment;
import com.yht.iptv.view.near.ImageDetailsActivity;
import com.yht.iptv.view.near.NearGeneralActivty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */

public class MainMenuDialogFragment extends DialogFragment implements TvRecyclerView.OnItemListener, DialogInterface.OnKeyListener {

    private Context mContext;
    private LayoutInflater inflater;
    private View oldView;
    private ArrayList<TitleBean> titlelist;
    private DialogFragmentMainRecAdapter adapter;
    private TvRecyclerView recyclerView;
    private int selectPos;
    private List<MainUIModel.SecServiceListBean> mainUIBeenList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        if(arguments != null) {
            mainUIBeenList = getArguments().getParcelableArrayList("mainUIBeenList");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // Get the layout inflater
        inflater = ((Activity) mContext).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_main_menu, null);

        recyclerView = (TvRecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setSelectedItemAtCentered(true);
        recyclerView.setOnItemListener(this);
        recyclerView.setInterceptKeyEvent(false);


        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (savedInstanceState == null) {
            titlelist = new ArrayList<>();

            adapter = new DialogFragmentMainRecAdapter(mContext, titlelist);

            recyclerView.setAdapter(adapter);

            recyclerView.setOnItemListener(this);
        } else {
            titlelist = savedInstanceState.getParcelableArrayList("titlelist");
            selectPos = savedInstanceState.getInt("selectedPos");

            adapter = new DialogFragmentMainRecAdapter(mContext, titlelist);

            recyclerView.setAdapter(adapter);

            recyclerView.setOnItemListener(this);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setSelection(selectPos);
                }
            }, 50);
        }

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setOnKeyListener(this);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.y = (int) getResources().getDimension(R.dimen.h_150);
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
    }


    /**
     * 接收来自TitleRequestService的标题数据
     */
    @Subscribe
    public void onFoodTitleInfo(List<TitleBean> titlelist) {
        if (titlelist.size() == 0) {
            this.dismiss();
            return;
        }
        titlelist.add(0, new TitleBean(-1, ""));
        titlelist.add(0, new TitleBean(-1, ""));
        titlelist.add(new TitleBean(-1, ""));
        titlelist.add(new TitleBean(-1, ""));
        this.titlelist.clear();
        this.titlelist.addAll(titlelist);
        adapter.notifyDataSetChanged();
        //设置选中位置
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setSelection(2);
            }
        }, 50);
        LogUtils.tag("title").d("biaoti");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        titlelist = null;
        adapter = null;
        recyclerView = null;
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        TitleBean titleBean = titlelist.get(position);
//        switch (titleBean.getType()) {
//            case Constants.TITLE_MOVIE:
//                Intent intent = new Intent(mContext, MediaDetailActivity.class);
//                intent.putExtra("categoryId", titleBean.getId()+"");
//                mContext.startActivity(intent);
//                dismiss();
//                break;
//            case Constants.TITLE_FOOD:
//                //跳转food界面
//                break;
//        }
//    }

    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.h_120);
        itemView.setLayoutParams(params);
        ImageView iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        iv_bg.setVisibility(View.INVISIBLE);
        //缩小动画
        AnimationUtils.scaleColorAnimation(tv_title, 500, 1.5f, 1.0f, 1.0f, 0.4f);

    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.h_140);
        itemView.setLayoutParams(params);
        ImageView iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        //放大动画
        AnimationUtils.scaleColorAnimation(tv_title, 500, 1.0f, 1.5f, 0.4f, 1.0f);
        iv_bg.setVisibility(View.VISIBLE);

    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        TitleBean titleBean = titlelist.get(position);
        switch (titleBean.getType()) {
            case Constants.TITLE_MOVIE:
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("categoryId", titleBean.getId() + "");
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                break;
            case Constants.TITLE_FOOD:
                //跳转food界面
                Intent intent2 = new Intent(mContext, HotelFoodActivity.class);
                intent2.putExtra("restaurantId", titleBean.getId() + "");
                mContext.startActivity(intent2);
                ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                break;
            case Constants.TITLE_SERVICE:
                if(mainUIBeenList != null) {
                    switch (mainUIBeenList.get(position - 2).getTag()) {
                        case 1:
                            //消息
                            if (getFragmentManager().findFragmentByTag("MessagePushDialogFragment") == null) {
                                //进入消息页面
                                MessagePushDialogFragment messagePushDialogFragment = new MessagePushDialogFragment();
                                messagePushDialogFragment.show(getFragmentManager(), "MessagePushDialogFragment");
                            }
                            break;
                        case 2:
                            //客房服务
                            if (getFragmentManager().findFragmentByTag("HotelPhoneDialogFragment") == null) {
                                //进入客房页面
                                HotelPhoneDialogFragment hotelPhoneDialogFragment = new HotelPhoneDialogFragment();
                                hotelPhoneDialogFragment.show(getFragmentManager(), "HotelPhoneDialogFragment");
                            }
                            break;
                        case 10: case 11: case 12:
                            Intent intent1 = new Intent(mContext, HotelGeneralActivity.class);
                            intent1.putExtra("position", mainUIBeenList.get(position - 2).getTag());
                            mContext.startActivity(intent1);
                            ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                            break;
                        default:
                            Intent intent01 = new Intent(mContext, GeneralActivity.class);
                            if(mainUIBeenList.get(position - 2).getApi() != null) {
                                intent01.putExtra("mainUIBeenList", mainUIBeenList.get(position - 2));
                            }
                            mContext.startActivity(intent01);
                            ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                            break;
                    }
                }else{
                    dismiss();
                }
                break;
            case Constants.TITLE_NEAR:
                switch (mainUIBeenList.get(position - 2).getTag()) {
                    case 1:
                        if (getFragmentManager().findFragmentByTag("BusTimetableFragment") == null) {
                            BusTimetableFragment busTimetableFragment = new BusTimetableFragment();
                            busTimetableFragment.show(getFragmentManager(), "BusTimetableFragment");
                        }
                        break;
                    case 2:
                        Intent intent1 = new Intent(mContext, ImageDetailsActivity.class);
                        mContext.startActivity(intent1);
                        ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                        break;
                    case 10: case 11: case 12:
                        Intent intent3 = new Intent(mContext, NearGeneralActivty.class);
                        intent3.putExtra("position", mainUIBeenList.get(position - 2).getTag());
                        mContext.startActivity(intent3);
                        ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                        break;
                    default:
                        Intent intent01 = new Intent(mContext, GeneralActivity.class);
                        if(mainUIBeenList.get(position - 2).getApi() != null) {
                            intent01.putExtra("mainUIBeenList", mainUIBeenList.get(position - 2));
                        }
                        mContext.startActivity(intent01);
                        ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                        break;
                }
                break;
            case Constants.TITLE_OTHER:
                Intent intent01 = new Intent(mContext, GeneralActivity.class);
                if(mainUIBeenList.get(position - 2).getApi() != null) {
                    intent01.putExtra("mainUIBeenList", mainUIBeenList.get(position - 2));
                }
                mContext.startActivity(intent01);
                ((Activity) mContext).overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                break;
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            if (recyclerView.getSelectedPosition() == 2) {
                return true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            int childCount = recyclerView.getAdapter().getItemCount();
            if (recyclerView.getSelectedPosition() == childCount - 3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedPos", recyclerView.getSelectedPosition());
        outState.putParcelableArrayList("titlelist", titlelist);
    }
}
