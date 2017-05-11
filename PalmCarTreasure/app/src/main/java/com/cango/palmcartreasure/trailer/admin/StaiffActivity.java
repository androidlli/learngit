package com.cango.palmcartreasure.trailer.admin;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;

public class StaiffActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staiff);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else {
//        }
        String type = getIntent().getStringExtra(StaiffFragment.TYPE);
        StaiffFragment staiffFragment = (StaiffFragment) getSupportFragmentManager().findFragmentById(R.id.fl_staiff_contains);
        if (CommUtil.checkIsNull(staiffFragment)) {
            staiffFragment = StaiffFragment.newInstance(type);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_staiff_contains, staiffFragment);
            transaction.commit();
        }
        StaiffPresenter staiffPresenter = new StaiffPresenter(staiffFragment);
    }

}
