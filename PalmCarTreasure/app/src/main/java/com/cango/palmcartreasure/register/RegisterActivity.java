package com.cango.palmcartreasure.register;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.login.LoginFragment;
import com.cango.palmcartreasure.login.LoginPresenter;
import com.cango.palmcartreasure.util.CommUtil;

public class RegisterActivity extends BaseActivity {

    private RegisterContract.Presenter mRegisterPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        RegisterFragment registerFragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fl_register_contains);
        if (CommUtil.checkIsNull(registerFragment)) {
            registerFragment = RegisterFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_register_contains, registerFragment);
            transaction.commit();
        }
        mRegisterPresenter = new RegisterPresenter(registerFragment);
    }
}
