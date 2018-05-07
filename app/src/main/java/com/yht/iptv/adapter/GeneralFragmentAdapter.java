package com.yht.iptv.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by admin on 2016/7/7.
 */
public class GeneralFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentsList;


    public GeneralFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public GeneralFragmentAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

}
