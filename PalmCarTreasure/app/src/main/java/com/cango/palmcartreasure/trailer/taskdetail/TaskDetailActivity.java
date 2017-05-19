package com.cango.palmcartreasure.trailer.taskdetail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.trailer.task.TrailerTaskPresenter;
import com.cango.palmcartreasure.trailer.task.TrailerTasksFragment;
import com.cango.palmcartreasure.util.CommUtil;

public class TaskDetailActivity extends BaseActivity {

    private TaskDetailContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {

        }

        int type=0;
        TypeTaskData.DataBean.TaskListBean taskListBean = getIntent().getParcelableExtra(TaskDetailFragment.TASKLISTBEAN);
        TaskDetailFragment detailFragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_detail_contains);
        if (CommUtil.checkIsNull(detailFragment)){
            detailFragment=TaskDetailFragment.newInstance(type,taskListBean);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_detail_contains,detailFragment);
            transaction.commit();
        }
        mPresenter=new TaskDetailPresenter(detailFragment);
    }
}
