package com.yht.iptv.view.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.adapter.DialogFragmentMessageAdapter;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MessagePushInfo;
import com.yht.iptv.presenter.PushMessagePresenter;
import com.yht.iptv.service.PageRecordService;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.OkHttpUtils;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class MessagePushDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, IPresenterBase<List<MessagePushInfo>> {

    private Context context;
    private TextView tv_message_detail;
    private View oldView;
    private ListView listView;
    private List<MessagePushInfo> messagePushInfos;
    private DialogFragmentMessageAdapter adapter;
    private String room_id;
    private TextView tv_empty;
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
        messagePushInfos = new ArrayList<>();
        room_id = (String) SPUtils.get(context, Constants.ROOM_ID, "");

        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_START);
        bundle.putString("behaviour",Constants.MESSAGE);
        ServiceUtils.startService(PageRecordService.class,bundle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_main_message, null);
        nothing_ll = (LinearLayout) view.findViewById(R.id.nothing_ll);
        nothing_text = (TextView) view.findViewById(R.id.nothing_text);
        listView = (ListView) view.findViewById(R.id.listview);
        tv_message_detail = (TextView) view.findViewById(R.id.tv_message_detail);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        line_view = (ImageView) view.findViewById(R.id.line_view);
        adapter = new DialogFragmentMessageAdapter(context, messagePushInfos);
        listView.setAdapter(adapter);
        listView.setOnItemSelectedListener(this);
        PushMessagePresenter pushMessagePresenter = new PushMessagePresenter(context, this);
        pushMessagePresenter.request(this, room_id);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        return dialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.width = (int) getResources().getDimension(R.dimen.w_1800);
        params.height = (int) getResources().getDimension(R.dimen.w_558);
        params.y = (int) getResources().getDimension(R.dimen.h_166);
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,60 * 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.cancel(this);
        Bundle bundle = new Bundle();
        bundle.putByte(Constants.PAGE_STATUS,Constants.PAGE_END);
        ServiceUtils.startService(PageRecordService.class,bundle);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        tv_message_detail.setText(messagePushInfos.get(position).getMessage());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSuccess(BaseModel<List<MessagePushInfo>> dataList) {
        nothing_ll.setVisibility(View.GONE);
        line_view.setVisibility(View.VISIBLE);
        List<MessagePushInfo> messagePushInfos = dataList.data;
        this.messagePushInfos.clear();
        this.messagePushInfos.addAll(messagePushInfos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
//        listView.setEmptyView(tv_empty);
        line_view.setVisibility(View.GONE);
        nothing_ll.setVisibility(View.VISIBLE);
        nothing_text.setText(context.getResources().getString(R.string.message_nothing));
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
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


    Handler handler = new Handler();
}
