package com.cango.palmcartreasure.trailer.personal;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.model.PersonalInfo;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

public class PersonalFragment extends BaseFragment implements PersonalContract.View {
    @BindView(R.id.toolbar_personal)
    Toolbar mToolbar;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    private PersonalActivity mActivity;
    private PersonalContract.Presenter mPresenter;

    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity = (PersonalActivity) getActivity();
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mPresenter.start();
        mPresenter.loadPersonalData(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(PersonalContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPersonalDataIndicator(boolean active) {
        if (active) {
            mLoadView.smoothToShow();
        } else {
            mLoadView.smoothToHide();
        }
    }

    @Override
    public void showPersonalDataError(String message) {
        if (!CommUtil.checkIsNull(message)) {
            ToastUtils.showLong(message);
        }
    }

    @Override
    public void showPersonalData(PersonalInfo.DataBean dataBean) {
        if (!CommUtil.checkIsNull(dataBean)){
            tvGender.setText(dataBean.getGender());
            tvMobile.setText(dataBean.getMobile());
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
