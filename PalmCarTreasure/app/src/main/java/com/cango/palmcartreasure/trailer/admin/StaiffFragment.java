package com.cango.palmcartreasure.trailer.admin;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.MtItemDecoration;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.Member;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
<<<<<<< HEAD
=======
import com.orhanobut.logger.Logger;
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StaiffFragment extends BaseFragment implements StaiffContract.View {

    public static final String TYPE = "type";
    public static final String SHOW_GROUP = "show_group";
    public static final String PUT_TASKS_GROUP = "put_tasks_group";
<<<<<<< HEAD
    public static final int ACTIVITY_REQUEST_CODE = 1000;
=======
    public static final int ACTIVITY_REQUEST_CODE=1000;
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794

    @BindView(R.id.toolbar_staiff)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_right)
    TextView tvRight;
    @BindView(R.id.recyclerView_staiff)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
<<<<<<< HEAD
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794

    @OnClick({R.id.tv_toolbar_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_right:
                String text = tvRight.getText().toString();
                if (getString(R.string.new_group).equals(text)) {
                    //添加分组 对应show_group
                    Intent addIntent = new Intent(getActivity(), GroupActivity.class);
                    addIntent.putExtra("type", GroupFragment.ADD);
//                    mActivity.mSwipeBackHelper.forward(addIntent);
<<<<<<< HEAD
                    mActivity.mSwipeBackHelper.forward(addIntent, ACTIVITY_REQUEST_CODE);
                } else if (getString(R.string.confirm).equals(text)) {
                    //确定  针对于给他进行分配任务的
                    if (currentGroupId != -1) {
                        mPresenter.taskArrange(true, currentGroupId, taskListBeanList);
=======
                    mActivity.mSwipeBackHelper.forward(addIntent,ACTIVITY_REQUEST_CODE);
                } else if (getString(R.string.confirm).equals(text)) {
                    //确定  针对于给他进行分配任务的
                    if (currentGroupId!=-1){
                        mPresenter.taskArrange(true,currentGroupId,taskListBeanList);
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                    }
                } else {

                }
                break;
        }
    }

    private String mType;
    private StaiffActivity mActivity;
    private StaiffContract.Presenter mPresenter;
    private StaiffAdapter mAdapter;
<<<<<<< HEAD
    private List<GroupList.DataBean.GroupListBean> mGroupListBeanList = new ArrayList<>();
    private int currentGroupId = -1;
=======
    private List<GroupList.DataBean.GroupListBean> mGroupListBeanList=new ArrayList<>();
    private int currentGroupId=-1;
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
    private List<TaskManageList.DataBean.TaskListBean> taskListBeanList;
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;

    public static StaiffFragment newInstance(String type) {
        StaiffFragment fragment = new StaiffFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static StaiffFragment newInstance(String type, List<TaskManageList.DataBean.TaskListBean> taskListBeanList) {
        StaiffFragment fragment = new StaiffFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putParcelableArrayList("taskListBeanList", (ArrayList<? extends Parcelable>) taskListBeanList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_staiff;
    }

    @Override
    protected void initView() {
        mActivity = (StaiffActivity) getActivity();
//        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
//        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
//        if (Build.VERSION.SDK_INT >= 21) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
//            mToolbar.setLayoutParams(layoutParams);
//            mToolbar.setPadding(0, statusBarHeight, 0, 0);
//        }
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.admin_return);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (mType.equals(SHOW_GROUP)) {
            tvRight.setText(getString(R.string.new_group));
        } else if (mType.equals(PUT_TASKS_GROUP)) {
            tvRight.setText(getString(R.string.confirm));
        } else {
        }

        mAdapter = new StaiffAdapter(mActivity, mGroupListBeanList, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<GroupList.DataBean.GroupListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, GroupList.DataBean.GroupListBean data, int position) {
                //确定    对应put_tasks_group
<<<<<<< HEAD
                if (SHOW_GROUP.equals(mType)) {
=======
                if (SHOW_GROUP.equals(mType)){
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                    Member memberLeader = new Member();
                    memberLeader.setId(data.getGroupLeaderID());
                    memberLeader.setName(data.getGroupLeader());
                    memberLeader.setGroupLeader(true);
                    memberLeader.setSelected(true);
                    List<Member> currentMembers = new ArrayList<>();
                    for (GroupList.DataBean.GroupListBean.UserListBean bean : data.getUserList()) {
                        if (memberLeader.getId() == bean.getUserid()) {

                        } else {
                            Member member = new Member();
                            member.setId(bean.getUserid());
                            member.setName(bean.getUserName());
                            member.setGroupLeader(false);
                            member.setSelected(true);
                            currentMembers.add(member);
                        }
                    }

                    Intent updateIntent = new Intent(getActivity(), GroupActivity.class);
                    updateIntent.putExtra("type", GroupFragment.UPDATE);
                    updateIntent.putExtra("groupId", data.getGroupid());
                    updateIntent.putExtra("groupName", data.getGroupName());
                    updateIntent.putExtra("memberLeader", memberLeader);
                    updateIntent.putParcelableArrayListExtra("currentMembers", (ArrayList<? extends Parcelable>) currentMembers);
//                    mActivity.mSwipeBackHelper.forward(updateIntent);
<<<<<<< HEAD
                    mActivity.mSwipeBackHelper.forward(updateIntent, ACTIVITY_REQUEST_CODE);
                } else if (PUT_TASKS_GROUP.equals(mType)) {
                    currentGroupId = data.getGroupid();
                    for (GroupList.DataBean.GroupListBean bean : mGroupListBeanList) {
=======
                    mActivity.mSwipeBackHelper.forward(updateIntent,ACTIVITY_REQUEST_CODE);
                }else if (PUT_TASKS_GROUP.equals(mType)){
                    currentGroupId=data.getGroupid();
                    for (GroupList.DataBean.GroupListBean bean:mGroupListBeanList) {
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                        bean.setSelected(false);
                    }
                    data.setSelected(true);
                    mAdapter.notifyDataSetChanged();
<<<<<<< HEAD
                } else {
=======
                }else {
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794

                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new MtItemDecoration(2, SizeUtil.dp2px(mActivity, 12), true));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.start();
        mPresenter.loadStaiff("", true, 0, 0);
    }

    @Override
    protected void initData() {
        mType = getArguments().getString(TYPE);
        if (SHOW_GROUP.equals(mType)) {

        } else if (PUT_TASKS_GROUP.equals(mType)) {
            taskListBeanList = getArguments().getParcelableArrayList("taskListBeanList");
        } else {

        }
    }

    @Override
    public void setPresenter(StaiffContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStaiffIndicator(boolean active) {
        if (active)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showStaiffError() {
        if (isLoadMore) {
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showStaiff(List<GroupList.DataBean.GroupListBean> staiffs) {
        llSorry.setVisibility(View.GONE);
        mAdapter.setNewData(staiffs);
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showNoStaiff() {
        if (isLoadMore) {

        } else {
        }
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showStaiffDetailUi(int staiffId) {

    }

    @Override
    public void showTaskArrangeSuccess(boolean isSuccess, String message) {
        if (!CommUtil.checkIsNull(message))
            ToastUtils.showShort(message);
<<<<<<< HEAD
        if (isSuccess){
            mActivity.setResult(Activity.RESULT_OK);
            mActivity.mSwipeBackHelper.swipeBackward();
        }
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

<<<<<<< HEAD
=======
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(resultCode);
        if (requestCode==ACTIVITY_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                mPresenter.loadStaiff("", true, 0, 0);
            }
        }
    }
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
}
