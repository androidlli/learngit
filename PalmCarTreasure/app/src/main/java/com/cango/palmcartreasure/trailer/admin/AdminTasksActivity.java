package com.cango.palmcartreasure.trailer.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;

public class AdminTasksActivity extends BaseActivity {

    public static final int ACTIVITY_ARRANGE_REQUEST_CODE=1001;
    private AdminTaskPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else {
//        }
        String type = getIntent().getStringExtra(AdminTasksFragment.TYPE);
        int[] groupIds = getIntent().getIntArrayExtra(AdminTasksFragment.GROUPIDS);
        AdminTasksFragment adminTasksFragment = (AdminTasksFragment) getSupportFragmentManager().findFragmentById(R.id.fl_admin_contains);
        if (CommUtil.checkIsNull(adminTasksFragment)) {
            if (getIntent().hasExtra(adminTasksFragment.GROUPIDS)){
                adminTasksFragment=adminTasksFragment.newInstance(type,groupIds);
            }else {
                adminTasksFragment = AdminTasksFragment.newInstance(type);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_admin_contains, adminTasksFragment);
            transaction.commit();
        }
        mPresenter = new AdminTaskPresenter(adminTasksFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==ACTIVITY_ARRANGE_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                AdminTasksFragment adminTasksFragment = (AdminTasksFragment) getSupportFragmentManager().findFragmentById(R.id.fl_admin_contains);
                if (adminTasksFragment!=null){
                    adminTasksFragment.onRefresh();
                }
            }
        }
    }
}
