package com.cango.palmcartreasure.trailer.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.baseAdapter.OnLoadMoreListener;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AdminTasksFragment extends BaseFragment implements AdminTasksContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String TYPE = "type";
    /**
     * 管理员所有未分配任务(任务分组也就是任务分配)
     */
    public static final String ADMIN_UNABSORBED = "unabsorbed";
    /**
     * 管理员查看具体小组的所有任务(任务抽回)
     */
    public static final String TASK = "task";
    /**
     * 管理员查询所有分组
     */
    public static final String GROUP = "group";

    public static final String GROUPIDS = "groupIds";

    @BindView(R.id.toolbar_admin)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_right)
    TextView tvRight;
    @BindView(R.id.swipeRefreshLayout_task)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView_task)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_admin_task_bottom)
    TextView tvBottom;
    @BindView(R.id.ll_admin_task_unabsorbed)
    LinearLayout llUnabsorbed;

    @OnClick({R.id.tv_toolbar_right, R.id.tv_admin_task_bottom,R.id.tv_give_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_right:
                String text = tvRight.getText().toString();
                if (text.equals(getString(R.string.check_all))) {
                    //全选
                    if (mType.equals(GROUP)) {
                        toolbarRightOnClickByGroup(true);
                    } else if (mType.equals(TASK)) {
                        toolbarRightOnClickByTask(true);
                    } else if (mType.equals(ADMIN_UNABSORBED)) {
                        toolbarRightOnClickByUnabsorbed(true);
                    } else {

                    }
                    tvRight.setText(getString(R.string.cancel));
                } else {
                    //取消
                    if (mType.equals(GROUP)) {
                        toolbarRightOnClickByGroup(false);
                    } else if (mType.equals(TASK)) {
                        toolbarRightOnClickByTask(false);
                    } else if (mType.equals(ADMIN_UNABSORBED)) {
                        toolbarRightOnClickByUnabsorbed(false);
                    } else {

                    }
                    tvRight.setText(getString(R.string.check_all));
                }
                break;
            case R.id.tv_admin_task_bottom:
                if (mType.equals(GROUP)) {
                    if (checkedAllByGroup()) {
                        //跳转组的任务
                        int[] checkUserIdsByGroup = getCheckUserIdsByGroup();
                        Intent groupTaskIntent = new Intent(getActivity(), AdminTasksActivity.class);
                        groupTaskIntent.putExtra(AdminTasksFragment.TYPE, AdminTasksFragment.TASK);
                        groupTaskIntent.putExtra(AdminTasksFragment.GROUPIDS, checkUserIdsByGroup);
                        mActivity.mSwipeBackHelper.forward(groupTaskIntent);
                    }
                } else if (mType.equals(TASK)) {
                    if (checkedAllByTask()) {
                        //抽回任务
                        int[] checkUserIdsByTask = getCheckUserIdsByTask();
                    }
                }else {

                }
                break;
            case R.id.tv_give_up:
                if (checkedAllByUnabsorbed()) {
                    //任务放弃
                    TaskAbandonRequest[] checkUserIdsByUnabsorbed = getCheckUserIdsByUnabsorbed();
                    mPresenter.giveUpTasks(checkUserIdsByUnabsorbed);
                }
                break;
        }
    }

    private float mLat, mLon;
    private String mType;
    private int[] mGroupIds;
    private AdminTasksActivity mActivity;
    private AdminTasksContract.Presenter mPresenter;
    private BaseAdapter mAdapter;
    private List<GroupTaskCount.DataBean.TaskCountListBean> taskCountListBeanList = new ArrayList<>();
    private List<GroupTaskQuery.DataBean.TaskListBean> taskQueryBeanList = new ArrayList<>();
    private List<TaskManageList.DataBean.TaskListBean> taskManageList = new ArrayList<>();
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;

    public static AdminTasksFragment newInstance(String type) {
        AdminTasksFragment fragment = new AdminTasksFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static AdminTasksFragment newInstance(String type, int[] groupIds) {
        AdminTasksFragment fragment = new AdminTasksFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putIntArray(GROUPIDS, groupIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_admin_tasks;
    }

    @Override
    protected void initView() {
        mActivity = (AdminTasksActivity) getActivity();
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

        if (mType.equals(ADMIN_UNABSORBED)) {
            tvBottom.setVisibility(View.GONE);
            llUnabsorbed.setVisibility(View.VISIBLE);
            mAdapter = new UnabsorbedTaskAdapter(mActivity, taskManageList, true, new UnabsorbedTaskAdapter.MtOnCheckedChangeThenTypeListener() {
                @Override
                public void mtOnCheckedChangedThenType() {
                    checkAllNotifyToolbarRightTextByUnabsorbed();
                }
            });
            mAdapter.setOnItemClickListener(new OnBaseItemClickListener<TaskManageList.DataBean.TaskListBean>() {
                @Override
                public void onItemClick(BaseHolder viewHolder, TaskManageList.DataBean.TaskListBean data, int position) {
                }
            });
        } else if (mType.equals(GROUP)) {
            tvBottom.setVisibility(View.VISIBLE);
            tvBottom.setText(R.string.query);
            llUnabsorbed.setVisibility(View.GONE);
            mAdapter = new AdminTaskAdapter(mActivity, taskCountListBeanList, true, new AdminTaskAdapter.MtOnCheckedChangeThenTypeListener() {
                @Override
                public void mtOnCheckedChangedThenType() {
                    checkAllNotifyToolbarRightTextByGroup();
                }
            });
            mAdapter.setOnItemClickListener(new OnBaseItemClickListener<GroupTaskCount.DataBean.TaskCountListBean>() {
                @Override
                public void onItemClick(BaseHolder viewHolder, GroupTaskCount.DataBean.TaskCountListBean data, int position) {
                }
            });
        } else if (mType.equals(TASK)) {
            tvBottom.setVisibility(View.VISIBLE);
            tvBottom.setText(R.string.revulsion);
            llUnabsorbed.setVisibility(View.GONE);
            mAdapter = new AdminGroupAdapter(mActivity, taskQueryBeanList, true, new AdminGroupAdapter.MtOnCheckedChangeThenTypeListener() {
                @Override
                public void mtOnCheckedChangedThenType() {
                    checkAllNotifyToolbarRightTextByTask();
                }
            });
            mAdapter.setOnItemClickListener(new OnBaseItemClickListener<GroupTaskQuery.DataBean.TaskListBean>() {
                @Override
                public void onItemClick(BaseHolder viewHolder, GroupTaskQuery.DataBean.TaskListBean data, int position) {
                }
            });
        } else {

        }

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                if (!CommUtil.checkIsNull(mType))
                    if (mType.equals(TASK)) {
                        mPresenter.loadGroupTasks(mGroupIds, mLat, mLon, false, mPageCount, PAGE_SIZE);
                    } else {
                        mPresenter.loadAdminTasks(mType, mLat, mLon, false, mPageCount, PAGE_SIZE);
                    }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //开启presenter
        mPresenter.start();
        if (mType.equals(TASK)) {
            mPresenter.loadGroupTasks(mGroupIds, mLat, mLon, true, mPageCount, PAGE_SIZE);
        } else {
            mPresenter.loadAdminTasks(mType, mLat, mLon, true, mPageCount, PAGE_SIZE);
        }
    }

    @Override
    protected void initData() {
        mType = getArguments().getString(TYPE);
        mLat = MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT);
        mLon = MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON);
        mGroupIds = getArguments().getIntArray(GROUPIDS);
    }

    @Override
    public void setPresenter(AdminTasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAdminTasksIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showAdminTasksError() {
        if (isLoadMore) {

        } else {
            showAdminTasksIndicator(false);
        }
        mAdapter.setLoadFailedView(R.layout.load_failed_layout);
    }

    /**
     * 这个接口没有分页，所以一次显示完全后加入end
     *
     * @param tasks
     */
    @Override
    public void showAdminTasks(List<GroupTaskCount.DataBean.TaskCountListBean> tasks) {
        if (isLoadMore) {
            if (tasks.size() == 0) {
                mAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                mTempPageCount++;
                mAdapter.setLoadMoreData(tasks);
            }
        } else {
            mAdapter.setNewData(tasks);
        }
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showAdminGroupTasks(List<GroupTaskQuery.DataBean.TaskListBean> tasks) {
        if (isLoadMore) {
            mTempPageCount++;
            mAdapter.setLoadMoreData(tasks);
        } else {
            mAdapter.setNewData(tasks);
        }
        if (tasks.size() < PAGE_SIZE) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void showAdminUnabsorbedTasks(List<TaskManageList.DataBean.TaskListBean> tasks) {
        if (isLoadMore) {
            mTempPageCount++;
            mAdapter.setLoadMoreData(tasks);
        } else {
            mAdapter.setNewData(tasks);
        }
        if (tasks.size() < PAGE_SIZE) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void showGiveUpTasksAndNotifyUi(boolean isSuccess,String message) {
        if (!CommUtil.checkIsNull(message)){
            ToastUtils.showShort(message);
        }
        onRefresh();
    }

    /**
     * 网络请求的数据为null的时候调用
     */
    @Override
    public void showNoAdminTasks() {
        if (isLoadMore) {

        } else {
        }
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showAdminTaskDetailUi(int taskId) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        if (!CommUtil.checkIsNull(mType))
            if (mType.equals(TASK)) {
                mPresenter.loadGroupTasks(mGroupIds, mLat, mLon, true, mPageCount, PAGE_SIZE);
            } else {
                mPresenter.loadAdminTasks(mType, mLat, mLon, true, mPageCount, PAGE_SIZE);
            }
    }

    private void checkAllNotifyToolbarRightTextByGroup() {
        if (taskCountListBeanList != null) {
            int checkedSize = 0;
            for (GroupTaskCount.DataBean.TaskCountListBean bean : taskCountListBeanList) {
                if (bean.isChecked()) {
                    checkedSize++;
                }
            }
            if (checkedSize == taskCountListBeanList.size()) {
                tvRight.setText(R.string.cancel);
            }
        }
    }

    /**
     * @param confirm true:全选 else 取消
     */
    private void toolbarRightOnClickByGroup(boolean confirm) {
        if (taskCountListBeanList != null) {
            for (GroupTaskCount.DataBean.TaskCountListBean bean : taskCountListBeanList) {
                bean.setChecked(confirm);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkedAllByGroup() {
        if (taskCountListBeanList != null) {
            for (GroupTaskCount.DataBean.TaskCountListBean bean : taskCountListBeanList) {
                if (bean.isChecked())
                    return true;
            }
        }
        return false;
    }

    private int[] getCheckUserIdsByGroup() {
        if (taskCountListBeanList != null) {
            List<GroupTaskCount.DataBean.TaskCountListBean> lists = new ArrayList<>();
            for (GroupTaskCount.DataBean.TaskCountListBean bean : taskCountListBeanList) {
                if (bean.isChecked()) {
                    lists.add(bean);
                }
            }
            int[] userIds = new int[lists.size()];
            for (int i = 0; i < lists.size(); i++) {
                userIds[i] = lists.get(i).getGroupid();
            }
            return userIds;
        }
        return null;
    }

    /**
     * 通过adapter的毁掉来刷新右上角的文字状态
     */
    private void checkAllNotifyToolbarRightTextByTask() {
        if (taskQueryBeanList != null) {
            int checkedSize = 0;
            for (GroupTaskQuery.DataBean.TaskListBean bean : taskQueryBeanList) {
                if (bean.isChecked()) {
                    checkedSize++;
                }
            }
            if (checkedSize == taskQueryBeanList.size()) {
                tvRight.setText(R.string.cancel);
            }
        }
    }

    /**
     * @param confirm true:全选 else 取消
     */
    private void toolbarRightOnClickByTask(boolean confirm) {
        if (taskQueryBeanList != null) {
            for (GroupTaskQuery.DataBean.TaskListBean bean : taskQueryBeanList) {
                bean.setChecked(confirm);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkedAllByTask() {
        if (taskQueryBeanList != null) {
            for (GroupTaskQuery.DataBean.TaskListBean bean : taskQueryBeanList) {
                if (bean.isChecked())
                    return true;
            }
        }
        return false;
    }

    private int[] getCheckUserIdsByTask() {
        if (taskQueryBeanList != null) {
            List<GroupTaskQuery.DataBean.TaskListBean> lists = new ArrayList<>();
            for (GroupTaskQuery.DataBean.TaskListBean bean : taskQueryBeanList) {
                if (bean.isChecked()) {
                    lists.add(bean);
                }
            }
            int[] userIds = new int[lists.size()];
            for (int i = 0; i < lists.size(); i++) {
                userIds[i] = lists.get(i).getGroupid();
            }
            return userIds;
        }
        return null;
    }

    private void checkAllNotifyToolbarRightTextByUnabsorbed() {
        if (taskManageList != null) {
            int checkedSize = 0;
            for (TaskManageList.DataBean.TaskListBean bean : taskManageList) {
                if (bean.isChecked()) {
                    checkedSize++;
                }
            }
            if (checkedSize == taskManageList.size()) {
                tvRight.setText(R.string.cancel);
            }
        }
    }

    /**
     * @param confirm true:全选 else 取消
     */
    private void toolbarRightOnClickByUnabsorbed(boolean confirm) {
        if (taskManageList != null) {
            for (TaskManageList.DataBean.TaskListBean bean : taskManageList) {
                bean.setChecked(confirm);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkedAllByUnabsorbed() {
        if (taskManageList != null) {
            for (TaskManageList.DataBean.TaskListBean bean : taskManageList) {
                if (bean.isChecked())
                    return true;
            }
        }
        return false;
    }

    private TaskAbandonRequest[] getCheckUserIdsByUnabsorbed() {
        if (taskManageList != null) {
            List<TaskAbandonRequest> lists = new ArrayList<>();
            TaskAbandonRequest request=null;
            for (TaskManageList.DataBean.TaskListBean bean : taskManageList) {
                if (bean.isChecked()) {
                    request=new TaskAbandonRequest();
                    request.setAgencyID(bean.getAgencyID());
                    request.setApplyCD(bean.getApplyCD());
                    request.setApplyID(bean.getApplyID());
                    request.setCaseID(bean.getCaseID());
                    lists.add(request);
                }
            }
            TaskAbandonRequest[] requests = new TaskAbandonRequest[lists.size()];
            for (int i = 0; i < lists.size(); i++) {
                requests[i] = lists.get(i);
            }
            return requests;
        }
        return null;
    }
}
