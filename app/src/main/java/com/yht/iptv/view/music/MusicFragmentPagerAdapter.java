package com.yht.iptv.view.music;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yht.iptv.R;
import com.yht.iptv.model.MusicTypeBean;

import java.util.List;


public class MusicFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<MusicTypeBean> musicTypeBeanList ;
    private Context context;
    private List<Fragment> fragments;


    public MusicFragmentPagerAdapter(FragmentManager fm, Context context, List<MusicTypeBean> musicTypeBeanList, List<Fragment> fragments) {
        super(fm);
        this.musicTypeBeanList = musicTypeBeanList;
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public long getItemId(int position) {
        return musicTypeBeanList.get(position).getId();
    }

    @Override
    public Fragment getItem(int position) {
        if(position < fragments.size()) {
            return fragments.get(position);
        }else{
            return fragments.get(0);
        }
    }

    @Override
    public int getCount() {
        return musicTypeBeanList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.music_tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(musicTypeBeanList.get(position).getName());
        return view;
    }

}