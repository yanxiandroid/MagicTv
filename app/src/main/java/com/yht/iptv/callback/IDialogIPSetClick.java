package com.yht.iptv.callback;


import com.yht.iptv.tools.IP_SET_CustomDialog;

/**
 * Created by admin on 2016/6/24.
 */
public interface IDialogIPSetClick {
    void onClick(IP_SET_CustomDialog dialog, String tag, String ip_address, String room_id);
}
