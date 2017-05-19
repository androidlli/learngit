package com.cango.palmcartreasure.trailer.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.util.CommUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class StaiffActivity extends BaseActivity {

    private StaiffPresenter staiffPresenter;

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
            if (type.equals(StaiffFragment.SHOW_GROUP)){
                staiffFragment = StaiffFragment.newInstance(type);
            }else if (type.equals(StaiffFragment.PUT_TASKS_GROUP)){
                ArrayList<TaskManageList.DataBean.TaskListBean> taskListBeanList = getIntent().getParcelableArrayListExtra("taskListBeanList");
                staiffFragment=StaiffFragment.newInstance(type,taskListBeanList);
            }else {
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_staiff_contains, staiffFragment);
            transaction.commit();
        }
        staiffPresenter = new StaiffPresenter(staiffFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==StaiffFragment.ACTIVITY_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                if (staiffPresenter!=null){
                    staiffPresenter.loadStaiff("",true,0,0);
                }
            }
        }
    }
}
