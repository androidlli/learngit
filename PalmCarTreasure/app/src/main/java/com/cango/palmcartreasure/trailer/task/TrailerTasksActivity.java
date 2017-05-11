package com.cango.palmcartreasure.trailer.task;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;
import com.orhanobut.logger.Logger;

public class TrailerTasksActivity extends BaseActivity {

    private TaskContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
        }
        setContentView(R.layout.activity_trailer_tasks);
//        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        String type = getIntent().getStringExtra("type");
        TrailerTasksFragment taskFragment = (TrailerTasksFragment) getSupportFragmentManager().findFragmentById(R.id.fl_trailer_task_contains);
        if (CommUtil.checkIsNull(taskFragment)){
            taskFragment=TrailerTasksFragment.getInstance(type);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_trailer_task_contains,taskFragment);
            transaction.commit();
        }
        mPresenter=new TrailerTaskPresenter(taskFragment);
    }

}
