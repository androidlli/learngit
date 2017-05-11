package com.cango.palmcartreasure.trailer.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends BaseFragment {

    private AdminActivity mActivity;

    @OnClick({R.id.iv_employee_group,R.id.iv_task_allocation,R.id.iv_task_query})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_employee_group:
                Intent staiffIntent=new Intent(getActivity(),StaiffActivity.class);
                staiffIntent.putExtra(StaiffFragment.TYPE,StaiffFragment.SHOW_GROUP);
                mActivity.mSwipeBackHelper.forward(staiffIntent);
                break;
            case R.id.iv_task_allocation:
                Intent taskIntent = new Intent(getActivity(),AdminTasksActivity.class);
                taskIntent.putExtra(AdminTasksFragment.TYPE,AdminTasksFragment.ADMIN_UNABSORBED);
                mActivity.mSwipeBackHelper.forward(taskIntent);
                break;
            case R.id.iv_task_query:
                Intent groupTaskIntent = new Intent(getActivity(),AdminTasksActivity.class);
                groupTaskIntent.putExtra(AdminTasksFragment.TYPE,AdminTasksFragment.GROUP);
                mActivity.mSwipeBackHelper.forward(groupTaskIntent);
                break;
        }
    }

    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_admin;
    }

    @Override
    protected void initView() {
        mActivity= (AdminActivity) getActivity();
    }

    @Override
    protected void initData() {

    }
}
