package com.yht.iptv.callback;

import android.view.KeyEvent;

/**
 * Created by admin on 2017/10/12.
 */

public interface IKeyEventListener {

    boolean onKeyDown(int keyCode, KeyEvent event);
    boolean dispatchKeyEvent(KeyEvent event);
}
