package com.cango.palmcartreasure.sms;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.register.RegisterFragment;
import com.cango.palmcartreasure.register.RegisterPresenter;
import com.cango.palmcartreasure.util.CommUtil;

public class SMSActivity extends BaseActivity {

    private SMSPresenter mSMSPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        String username = getIntent().getStringExtra("username");
        SMSFragment smsFragment = (SMSFragment) getSupportFragmentManager().findFragmentById(R.id.fl_sms_contains);
        if (CommUtil.checkIsNull(smsFragment)) {
            smsFragment = SMSFragment.newInstance(username);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_sms_contains, smsFragment);
            transaction.commit();
        }
        mSMSPresenter = new SMSPresenter(smsFragment);
    }
}
