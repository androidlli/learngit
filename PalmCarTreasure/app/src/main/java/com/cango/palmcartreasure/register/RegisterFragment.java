package com.cango.palmcartreasure.register;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.login.LoginActivity;
import com.cango.palmcartreasure.sms.SMSActivity;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment implements RegisterContract.View {

    @BindView(R.id.toolbar_register)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    EditText etUserName;

    @OnClick({R.id.tv_register_login,R.id.tv_next_step})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_register_login:
                mActivity.mSwipeBackHelper.forwardAndFinish(LoginActivity.class);
                break;
            case R.id.tv_next_step:
                if (TextUtils.isEmpty(etUserName.getText())){
                    ToastUtils.showShort(R.string.register_username_not_null);
                }else {
                    Intent intent=new Intent(mActivity,SMSActivity.class);
                    intent.putExtra("username",etUserName.getText().toString().trim());
                    //在baseactiviy中把activity加入了list
//                    MtApplication.addActivity(mActivity);
                    mActivity.mSwipeBackHelper.forward(intent);
                }
                break;
        }
    }

    private RegisterActivity mActivity;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView() {
        mActivity= (RegisterActivity) getActivity();
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
