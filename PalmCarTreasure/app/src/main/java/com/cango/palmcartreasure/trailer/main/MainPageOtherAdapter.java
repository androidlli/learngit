package com.cango.palmcartreasure.trailer.main;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cango on 2017/4/6.
 */

public class MainPageOtherAdapter extends PagerAdapter {
    private List<View> mViewList;

    public void setViewList(List<View> viewList) {
        mViewList = viewList;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        View gridView = (View) object;
        int index = mViewList.indexOf(gridView);
        if (index >= 0) {
            return index;
        }
        return POSITION_NONE;
    }
}
