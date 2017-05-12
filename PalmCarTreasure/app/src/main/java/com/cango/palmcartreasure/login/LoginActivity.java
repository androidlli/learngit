package com.cango.palmcartreasure.login;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fl_main_contains);
        boolean isFromLogout = getIntent().getBooleanExtra("isFromLogout", false);
        if (CommUtil.checkIsNull(loginFragment)) {
            if (isFromLogout){
                loginFragment=LoginFragment.newInstance(isFromLogout);
            }else {
                loginFragment = LoginFragment.newInstance();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_main_contains, loginFragment);
            transaction.commit();
        }
        mLoginPresenter = new LoginPresenter(loginFragment);
    }
}

