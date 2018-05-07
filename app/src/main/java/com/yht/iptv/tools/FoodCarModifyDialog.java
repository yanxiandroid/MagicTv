package com.yht.iptv.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.iptv.R;
import com.yht.iptv.model.FoodCarInfo;
import com.yht.iptv.utils.DBUtils;
import com.yht.iptv.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.db.sqlite.WhereBuilder;

import java.text.DecimalFormat;

/**
 * Created by Q on 2017/10/27.
 */

public class FoodCarModifyDialog extends AppCompatDialog {
    private Context context;
    private Button[] buttons;
    private ImageView dialog_img;
    private TextView dialog_name;
    private TextView dialog_price;
    private TextView dialog_num;
    private OnCancelListener onCancelListener;
    private FoodCarInfo info;
    private long totalNum;

    public FoodCarModifyDialog(Context context) {
        super(context);
    }

    public FoodCarModifyDialog(Context context, int theme) {
        super(context, theme);
    }

    public FoodCarModifyDialog(Context context, FoodCarInfo info, OnCancelListener onCancelListener,long totalNum) {
        super(context, R.style.MyDialogDefine);
        this.onCancelListener = onCancelListener;
        this.context = context;
        this.info = info;
        this.totalNum = totalNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_food_car);
        buttons = new Button[2];
        buttons[0] = (Button) findViewById(R.id.dialog_left);
        buttons[1] = (Button) findViewById(R.id.dialog_right);
        dialog_img = (ImageView) findViewById(R.id.dialog_img);
        dialog_name = (TextView) findViewById(R.id.dialog_name);
        dialog_price = (TextView) findViewById(R.id.dialog_price);
        dialog_num = (TextView) findViewById(R.id.dialog_num);

        Glide.with(context).load(info.getImage()).into(dialog_img);
        long num = Integer.parseInt(info.getNum());
        float price = Float.parseFloat(info.getPrice());
        dialog_name.setText(info.getName());
        dialog_price.setText("￥:" + numberFormat(price));
        dialog_num.setText("*" + num);

        setOnCancelListener(onCancelListener);


    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            setShopCarNum(0);
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            setShopCarNum(1);
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 设置商品数量
     *
     * @param type 0 减 1 加
     */
    private void setShopCarNum(int type) {
        String num = info.getNum();
        int newNum;
        if (type == 0) {  //按左键
            newNum = Integer.parseInt(num) - 1;
            totalNum --;
            if(totalNum <= 0){
                totalNum = 0;
            }
            if (newNum <= 0) {
                newNum = 0;
            }
        } else {
            newNum = Integer.parseInt(num) + 1;
            totalNum ++;
            if(totalNum > 99){
                totalNum = 99;
                ToastUtils.showShort("已超过个人购买商品数量最大值,不能继续修改!");
                return;
            }
            if (newNum > 99) {
                newNum = 99;
                ToastUtils.showShort("已超过个人购买商品数量最大值,不能继续修改!");
                return;
            }
        }

        info.setNum(String.valueOf(newNum));
        String  newPrice = numberFormat(Float.parseFloat(info.getPrice()));
        dialog_price.setText("￥:" + newPrice);
        dialog_num.setText("*" + newNum);
        EventBus.getDefault().post(info);
    }

    private String numberFormat(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

}
