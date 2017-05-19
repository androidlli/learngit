package com.cango.palmcartreasure.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by cango on 2017/3/30.
 * 懒加载fragment，配合viewpager使用，最好viewpager设置缓存页
 */

public abstract class BaseLazyFragment extends Fragment {
    private Unbinder unbinder;
    private boolean isInitView, isVisible;
    private boolean isFirstLoad = true;

    protected abstract int getLayoutId();

    public abstract void initData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        isInitView = true;
        lazyLoad();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void lazyLoad() {
        Logger.d(isFirstLoad+"   "+isVisible()+"   "+isInitView);
        if (!isFirstLoad || !isVisible || !isInitView) {
            return;
        } else {
            initData();
            isFirstLoad = false;
        }
    }
}
