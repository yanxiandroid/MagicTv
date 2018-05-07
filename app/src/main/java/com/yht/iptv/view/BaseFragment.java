package com.yht.iptv.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.yht.iptv.callback.IKeyEventListener;

/**
 * Created by admin on 2017/10/12.
 */

public abstract class BaseFragment extends Fragment implements IKeyEventListener {


//    private OnButtonClick onButtonClick;
//
//    public OnButtonClick getOnButtonClick() {
//        return onButtonClick;
//    }
//    public void setOnButtonClick(OnButtonClick onButtonClick) {
//        this.onButtonClick = onButtonClick;
//    }
//    public interface OnButtonClick{
//        void onClick(BaseFragment fragment);
//    }

    public abstract boolean onKeyDown(int keyCode, KeyEvent event);
    public abstract boolean dispatchKeyEvent(KeyEvent event);

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

    }

    @Override
    public void onSaveInstanceState (Bundle outState){
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

}

