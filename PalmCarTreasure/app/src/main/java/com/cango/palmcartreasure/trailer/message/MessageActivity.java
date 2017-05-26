package com.cango.palmcartreasure.trailer.message;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.util.CommUtil;
import com.orhanobut.logger.Logger;

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.fl_message_contains);
        if (CommUtil.checkIsNull(messageFragment)) {
            messageFragment = MessageFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_message_contains, messageFragment);
            transaction.commit();
        }
        MessagePresenter messagePresenter = new MessagePresenter(messageFragment);
    }

    @Override
    public void onBackPressed() {
        if (MtApplication.activityList!=null){
            Logger.d(MtApplication.activityList.size());
        }
        if (MtApplication.activityList!=null&&MtApplication.activityList.size()>1){
            super.onBackPressed();
        }else {
            mSwipeBackHelper.forwardAndFinish(TrailerActivity.class);
        }
    }
}
