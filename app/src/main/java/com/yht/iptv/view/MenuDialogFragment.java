package com.yht.iptv.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.yht.iptv.adapter.DialogFragmentMenuAdapter;
import com.yht.iptv.service.CheckNewVersionService;
import com.yht.iptv.utils.AnimationUtils;
import com.yht.iptv.utils.ServiceUtils;
import com.yht.iptv.utils.ToastUtils;
import com.yht.iptv.view.hotel.HotelFoodActivity;
import com.yht.iptv.view.hotel.HotelFoodCarActivity;

/**
 * Created by admin on 2017/11/1.
 */

public class MenuDialogFragment extends DialogFragment implements TvRecyclerView.OnItemListener, DialogInterface.OnKeyListener {

    private Context mContext;
    private TvRecyclerView recyclerView;
    private String[] array;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // Get the layout inflater
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_main_menu, null);

        recyclerView = (TvRecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setSelectedItemAtCentered(true);
        recyclerView.setOnItemListener(this);
        recyclerView.setInterceptKeyEvent(false);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        array = getResources().getStringArray(R.array.menu_tab);


        DialogFragmentMenuAdapter adapter = new DialogFragmentMenuAdapter(mContext, array);

        recyclerView.setAdapter(adapter);

        recyclerView.setOnItemListener(this);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setSelection(2);
            }
        },50);

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

    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.h_120);
        itemView.setLayoutParams(params);
        ImageView iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        iv_bg.setVisibility(View.INVISIBLE);
        //缩小动画
        AnimationUtils.scaleColorAnimation(tv_title, 500,1.5f,1.0f,1.0f,0.4f);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.h_140);
        itemView.setLayoutParams(params);
        ImageView iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        //放大动画
        AnimationUtils.scaleColorAnimation(tv_title, 500,1.0f, 1.5f,0.4f,1.0f);
        iv_bg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {

    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {
        switch (position - 2){
            case 0:
                //购物车
                this.dismiss();
                Intent in = new Intent(mContext, HotelFoodCarActivity.class);
                startActivity(in);
                ((Activity)mContext).overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                break;
            case 1:
                //多屏互动
                this.dismiss();
                MutiScreenDialogFragment mutiScreenDialogFragment = new MutiScreenDialogFragment();
                mutiScreenDialogFragment.show(getFragmentManager(),"mutiScreenDialogFragment");
                Bundle bundle = new Bundle();
                bundle.putBoolean("phone_url",true);
                ServiceUtils.startService(CheckNewVersionService.class,bundle);
                break;
            case 2:
                //美食订单
                this.dismiss();
                FoodOrderDialogFragment foodOrderDialogFragment = new FoodOrderDialogFragment();
                foodOrderDialogFragment.show(getFragmentManager(),"foodOrderDialogFragment");
                break;
            case 3:
                //关于我们
                this.dismiss();
                AboutMeDialogFragment aboutMeDialogFragment = new AboutMeDialogFragment();
                aboutMeDialogFragment.show(getFragmentManager(),"aboutMeDialogFragment");
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
}
