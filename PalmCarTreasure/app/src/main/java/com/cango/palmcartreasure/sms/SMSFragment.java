package com.cango.palmcartreasure.sms;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.util.BarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SMSFragment extends BaseFragment implements SMSContract.View {

    @BindView(R.id.toolbar_sms)
    Toolbar mToolbar;

    private String mUserName;

    @OnClick({R.id.tv_sms_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sms_complete:
                //通过eventbus来设置删除acivitylist
                Intent intent=new Intent(mActivity,TrailerActivity.class);
                intent.putExtra("isFromSMS",true);
                mActivity.mSwipeBackHelper.forward(intent);
                break;
        }
    }

    private SMSContract.Presenter mPresenter;
    private SMSActivity mActivity;

    public static SMSFragment newInstance(String username) {
        SMSFragment fragment = new SMSFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_sm;
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
        mActivity = (SMSActivity) getActivity();
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
    }

    @Override
    protected void initData() {
        mUserName = (String) getArguments().get("username");
    }

    @Override
    public void setPresenter(SMSContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
