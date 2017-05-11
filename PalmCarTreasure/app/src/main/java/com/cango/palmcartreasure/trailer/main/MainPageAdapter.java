package com.cango.palmcartreasure.trailer.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cango.palmcartreasure.base.BaseLazyFragment;

import java.util.List;

/**
 * Created by cango on 2017/4/6.
 */

public class MainPageAdapter extends FragmentPagerAdapter {
    private List<BaseLazyFragment> fragments;
    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }
    public void setData(List<BaseLazyFragment> fragments){
        this.fragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
