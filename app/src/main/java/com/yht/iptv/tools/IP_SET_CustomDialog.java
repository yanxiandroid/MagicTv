package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yht.iptv.R;
import com.yht.iptv.callback.IDialogIPSetClick;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.DialogUtils;
import com.yht.iptv.utils.SPUtils;
import com.yht.iptv.utils.ToastUtils;

/**
 * Created by admin on 2016/6/24.
 */
public class IP_SET_CustomDialog extends AppCompatDialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private IDialogIPSetClick onClickListener;
    private EditText[] editTexts;
    private Context context;
    private EditText et_room_id;
    private String currentIp;
    private String currentRoom;
    private CheckBox cb_tv;
    private CheckBox cb_pms;
    private String payIdea;

    public IP_SET_CustomDialog(Context context) {
        super(context);
    }

    public IP_SET_CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public IP_SET_CustomDialog(Context context, IDialogIPSetClick onClickListener) {
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.context = context;
    }

    public IP_SET_CustomDialog(Context context, String currentIp, String currentRoom, IDialogIPSetClick onClickListener) {
        super(context, R.style.MyDialogDefine);
        this.onClickListener = onClickListener;
        this.context = context;
        this.currentIp = currentIp;
        this.currentRoom = currentRoom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ipset_toast);
        int signChoose = (int) SPUtils.get(context, Constants.SIGNCHOOSE,0);
        payIdea = (String) SPUtils.get(context, Constants.PAY_CHOOSE, Constants.ONLINE_PAY);
        editTexts = new EditText[4];
        et_room_id = (EditText) findViewById(R.id.et_room_id);
        editTexts[0] = (EditText) findViewById(R.id.et_address1);
        editTexts[1] = (EditText) findViewById(R.id.et_address2);
        editTexts[2] = (EditText) findViewById(R.id.et_address3);
        editTexts[3] = (EditText) findViewById(R.id.et_address4);
        Button bt_confirm = (Button) findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) findViewById(R.id.bt_cancel);
        RadioGroup rg_group = (RadioGroup) findViewById(R.id.rg_group);
        RadioButton rb_tv = (RadioButton) findViewById(R.id.rb_tv);
        RadioButton rb_hdmi = (RadioButton) findViewById(R.id.rb_hdmi);
        cb_tv = (CheckBox) findViewById(R.id.cb_tv);
        cb_pms = (CheckBox) findViewById(R.id.cb_pms);
        cb_tv.setOnCheckedChangeListener(this);
        cb_pms.setOnCheckedChangeListener(this);
        rg_group.setOnCheckedChangeListener(this);
        bt_cancel.setVisibility(View.GONE);
        setCancelable(false);
        bt_confirm.setOnClickListener(this);
        bt_confirm.setTag(Constants.CONFIRM);
        bt_cancel.setOnClickListener(this);
        bt_cancel.setTag(Constants.CANCEL);

        setCurrentIpRoom(bt_cancel);

        if(signChoose == 0){
            rg_group.check(rb_tv.getId());
        }else{
            rg_group.check(rb_hdmi.getId());
        }

        if(payIdea.equals(Constants.ONLINE_PAY)){
            cb_tv.setChecked(true);
            cb_pms.setChecked(false);
        } else if(payIdea.equals(Constants.PMS_PAY)){
            cb_tv.setChecked(false);
            cb_pms.setChecked(true);
        }else if(payIdea.equals(Constants.ALL_PAY)){
            cb_tv.setChecked(true);
            cb_pms.setChecked(true);
        }

        et_room_id.requestFocus();
    }

    @Override
    public void onClick(View view) {
        if(view.getTag().equals(Constants.CANCEL)){
            DialogUtils.dismissDialog(this);
        }else {
            if (et_room_id.getText().toString().equals("")) {
                ToastUtils.showShort("房间号不能为空");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                if (editTexts[i].getText().toString().equals("")) {
                    ToastUtils.showShort("地址不能有空项");
                    return;
                }
                sb.append(editTexts[i].getText().toString());
                if (i != 3) {
                    sb.append(".");
                }
            }
            String s = sb.toString();

            String tag = (String) view.getTag();

            onClickListener.onClick(this, tag, s, et_room_id.getText().toString());
        }
    }

    private void setCurrentIpRoom(Button bt_cancel){
        if(currentIp != null && !currentIp.equals("") && currentRoom != null && !currentRoom.equals("")){
            String[] ips = currentIp.split("\\.");
            et_room_id.setText(currentRoom);
            et_room_id.setSelection(currentRoom.length());
            editTexts[0].setText(ips[0]);
            editTexts[0].setSelection(ips[0].length());
            editTexts[1].setText(ips[1]);
            editTexts[1].setSelection(ips[1].length());
            editTexts[2].setText(ips[2]);
            editTexts[2].setSelection(ips[2].length());
            editTexts[3].setText(ips[3]);
            editTexts[3].setSelection(ips[3].length());
            bt_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if(group.getCheckedRadioButtonId() == R.id.rb_tv){
            SPUtils.put(context, Constants.SIGNCHOOSE,0);
        }else{
            SPUtils.put(context, Constants.SIGNCHOOSE,1);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(cb_tv.isChecked() && cb_pms.isChecked()){
//            SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ALL_PAY);
//        }
//        if(cb_tv.isChecked() && !cb_pms.isChecked()){
//            SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ONLINE_PAY);
//        }
//        if(!cb_tv.isChecked() && cb_pms.isChecked()){
//            SPUtils.put(context, Constants.PAY_CHOOSE, Constants.PMS_PAY);
//        }
//        if(!cb_tv.isChecked() && !cb_pms.isChecked()){
//            SPUtils.put(context, Constants.PAY_CHOOSE, Constants.NONE_PAY);
//        }
        switch (buttonView.getId()){
            case R.id.cb_tv:
                if(cb_tv.isChecked() && cb_pms.isChecked()){
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ALL_PAY);
                }
                if(cb_tv.isChecked() && !cb_pms.isChecked()){
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ONLINE_PAY);
                }
                if(!cb_tv.isChecked() && cb_pms.isChecked()){
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.PMS_PAY);
                }
                if(!cb_tv.isChecked() && !cb_pms.isChecked()){
                    cb_tv.setChecked(true);
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ONLINE_PAY);
                    ToastUtils.showShort("至少应该选择一种支付方式");
                }
                break;

            case R.id.cb_pms:
                if(cb_tv.isChecked() && cb_pms.isChecked()){
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ALL_PAY);
                }
                if(cb_tv.isChecked() && !cb_pms.isChecked()){
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.ONLINE_PAY);
                }
                if(!cb_tv.isChecked() && cb_pms.isChecked()){
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.PMS_PAY);
                }
                if(!cb_tv.isChecked() && !cb_pms.isChecked()){
                    cb_pms.setChecked(true);
                    SPUtils.put(context, Constants.PAY_CHOOSE, Constants.PMS_PAY);
                    ToastUtils.showShort("至少应该选择一种支付方式");
                }
                break;
        }
    }
}
