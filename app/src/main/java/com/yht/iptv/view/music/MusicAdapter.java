package com.yht.iptv.view.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MusicDetailBean;

import java.util.List;

/**
 * Created by admin on 2017/2/24.
 */
public class MusicAdapter extends BaseAdapter {

    private Context mContext;

    private List<MusicDetailBean> musicDetailBeanList;
    private final LayoutInflater inflater;
    private int playStatus;
    private int position;//显示按钮的行数

    // private PlayIconSettingListener listener;


    public MusicAdapter(Context mContext, List<MusicDetailBean> musicDetailBeanList) {
        this.mContext = mContext;
        this.musicDetailBeanList = musicDetailBeanList;
        inflater = LayoutInflater.from(mContext);
        position = -1;
    }



//    public interface PlayIconSettingListener{
//        void onStatus();
//    }

    public void setOnPlayIconStatus(int position, int playStatus){
        this.playStatus = playStatus;
        this.position = position;
    }


    @Override
    public int getCount() {
        return musicDetailBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.music_listview_item, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
            holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String number;
        if(position+1 < 10){
            number = "0" + (position + 1) + " . ";
        }else{
            number = (position + 1) + " . ";
        }
        holder.tv_music_name.setText(number + musicDetailBeanList.get(position).getName());

        if(this.position == position) {
            if (playStatus == 0) {
                //暂停状态
                holder.iv_icon.setImageResource(R.drawable.music_play);
                MusicAnimManger.getInstance().executeListener(true);
            } else if(playStatus == 1){
                holder.iv_icon.setImageResource(R.drawable.music_pause);
                MusicAnimManger.getInstance().executeListener(false);
            } else{
                holder.iv_icon.setImageResource(R.color.transparent);
                MusicAnimManger.getInstance().executeListener(true);
            }
        }else{
            holder.iv_icon.setImageResource(R.color.transparent);
        }

        return convertView;
    }

    private class ViewHolder{
        ImageView iv_icon;
        TextView tv_music_name;
        RelativeLayout rl_item;
    }

}
