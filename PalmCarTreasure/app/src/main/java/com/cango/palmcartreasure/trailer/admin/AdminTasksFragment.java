package com.cango.palmcartreasure.trailer.admin;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.cango.palmcartreasure.model.TaskDrawEvent;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailActivity;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AdminTasksFragment extends BaseFragment implements AdminTasksContract.View, SwipeRefreshLayout.OnRefreshListener, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 130;
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
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    @OnClick({R.id.tv_toolbar_right, R.id.tv_admin_task_bottom, R.id.tv_give_up, R.id.tv_arrange})
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
                        List<GroupTaskQuery.DataBean.TaskListBean> checkUserIdsByTask = getCheckUserIdsByTask();
                        if (!CommUtil.checkIsNull(checkUserIdsByTask) && checkUserIdsByTask.size() > 0) {
                            mPresenter.groupTaskDraw(true, checkUserIdsByTask);
                        }
                    }
                } else {

                }
                break;
            case R.id.tv_give_up:
                if (checkedAllByUnabsorbed()) {
                    //任务放弃
                    TaskAbandonRequest[] checkUserIdsByUnabsorbed = getCheckUserIdsByUnabsorbed();
                    mPresenter.giveUpTasks(checkUserIdsByUnabsorbed);
                }
                break;
            case R.id.tv_arrange:
                //任务分配
                List<TaskManageList.DataBean.TaskListBean> taskListBeanList = getSelectedTaskListBean();
                if (!CommUtil.checkIsNull(taskListBeanList) && taskListBeanList.size() > 0) {
                    Intent intent = new Intent(mActivity, StaiffActivity.class);
                    intent.putExtra(StaiffFragment.TYPE, StaiffFragment.PUT_TASKS_GROUP);
                    intent.putParcelableArrayListExtra("taskListBeanList", (ArrayList<? extends Parcelable>) taskListBeanList);
                    mActivity.mSwipeBackHelper.forward(intent, AdminTasksActivity.ACTIVITY_ARRANGE_REQUEST_CODE);
                }
                break;
        }
    }

    private float mLat, mLon;
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLoactionListener;
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
        showLoadingView(false);
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
                    TypeTaskData.DataBean.TaskListBean taskListBean = new TypeTaskData.DataBean.TaskListBean();
                    taskListBean.setAgencyID(data.getAgencyID());
                    taskListBean.setCaseID(data.getCaseID());
                    taskListBean.setApplyID(data.getApplyID());
                    taskListBean.setCustomerName(data.getCustomerName());
                    taskListBean.setCarPlateNO(data.getCarPlateNO());
                    taskListBean.setShortName(data.getShortName());
                    taskListBean.setAgencyStatus(data.getAgencyStatus());
                    taskListBean.setFlowStauts(data.getFlowStauts());
                    taskListBean.setDistance(data.getDistance());
                    taskListBean.setIsStart("F");
                    taskListBean.setIsCheckPoint("F");
                    taskListBean.setIsDone("F");
                    mPresenter.openDetailTask(taskListBean);
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
                    TypeTaskData.DataBean.TaskListBean taskListBean = new TypeTaskData.DataBean.TaskListBean();
                    taskListBean.setAgencyID(data.getAgencyID());
                    taskListBean.setCaseID(data.getCaseID());
                    taskListBean.setApplyID(data.getApplyID());
                    taskListBean.setCustomerName(data.getCustomerName());
                    taskListBean.setCarPlateNO(data.getCarPlateNO());
                    taskListBean.setShortName(data.getShortName());
                    taskListBean.setAgencyStatus(data.getAgencyStatus());
                    taskListBean.setFlowStauts(data.getFlowStauts());
                    taskListBean.setDistance(data.getDistance());
                    taskListBean.setIsStart("F");
                    taskListBean.setIsCheckPoint("F");
                    taskListBean.setIsDone("F");
                    mPresenter.openDetailTask(taskListBean);
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
                if (mLat > 0 && mLon > 0) {
                    if (!CommUtil.checkIsNull(mType)) {
                        if (mType.equals(TASK)) {
                            mPresenter.loadGroupTasks(mGroupIds, mLat, mLon, false, mPageCount, PAGE_SIZE);
                        } else {
                            mPresenter.loadAdminTasks(mType, mLat, mLon, false, mPageCount, PAGE_SIZE);
                        }
                    }
                } else {
                    ToastUtils.showShort(R.string.no_get_location);
                }

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //开启presenter
        mPresenter.start();
//        getFirstData();
        doGetDatas();
    }

    private void getFirstData() {
        if (mLat > 0 && mLon > 0) {
            if (mType.equals(TASK)) {
                mPresenter.loadGroupTasks(mGroupIds, mLat, mLon, true, mPageCount, PAGE_SIZE);
            } else {
                mPresenter.loadAdminTasks(mType, mLat, mLon, true, mPageCount, PAGE_SIZE);
            }
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    @Override
    protected void initData() {
        mActivity = (AdminTasksActivity) getActivity();
        mType = getArguments().getString(TYPE);
//        mLat = MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT);
//        mLon = MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON);
        mGroupIds = getArguments().getIntArray(GROUPIDS);

        mLocationClient = new AMapLocationClient(mActivity.getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(3000);
        mLocationClient.setLocationOption(option);
        mLoactionListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (CommUtil.checkIsNull(aMapLocation)) {

                } else {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        String dateString = df.format(date);
//                        Logger.d(dateString + ": Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
                        if (!CommUtil.checkIsNull(aMapLocation.getLatitude())){
                            BigDecimal latBD = new BigDecimal(String.valueOf(aMapLocation.getLatitude()));
                            mLat = latBD.floatValue();
                        }
                        if (!CommUtil.checkIsNull(aMapLocation.getLongitude())){
                            BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
                            mLon = lonBD.floatValue();
                        }
                    } else {
                        Logger.d("errorCode = " + aMapLocation.getErrorCode() + " errorInfo = " + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLoactionListener);
        mLocationClient.startLocation();
//        double latitude = mLocationClient.getLastKnownLocation().getLatitude();
//        double longitude = mLocationClient.getLastKnownLocation().getLongitude();
//        if (!CommUtil.checkIsNull(latitude)){
//            BigDecimal latBD = new BigDecimal(String.valueOf(latitude));
//            mLat = latBD.floatValue();
//        }
//        if (!CommUtil.checkIsNull(longitude)){
//            BigDecimal lonBD = new BigDecimal(String.valueOf(longitude));
//            mLon = lonBD.floatValue();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient!=null){
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTaskDrawEvent(TaskDrawEvent taskDrawEvent) {
        if (taskDrawEvent.isOnRefresh) {
            onRefresh();
        }
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

    /**
     * 如果是onerror或者code不是0的话毁掉这里，在里面判断是否是第一次，如果第一次的话那么显示sorry图片，如果不是那么应该在列表底部添加错误信息
     */
    @Override
    public void showAdminTasksError() {
        if (isLoadMore) {
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
            llUnabsorbed.setVisibility(View.GONE);
            tvBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 这个接口没有分页，所以一次显示完全后加入end
     *
     * @param tasks
     */
    @Override
    public void showAdminTasks(List<GroupTaskCount.DataBean.TaskCountListBean> tasks) {
        llSorry.setVisibility(View.GONE);
        if (ADMIN_UNABSORBED.equals(mType)) {
            llUnabsorbed.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.GONE);
        } else {
            llUnabsorbed.setVisibility(View.GONE);
            tvBottom.setVisibility(View.VISIBLE);
        }
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
        llSorry.setVisibility(View.GONE);
        if (ADMIN_UNABSORBED.equals(mType)) {
            llUnabsorbed.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.GONE);
        } else {
            llUnabsorbed.setVisibility(View.GONE);
            tvBottom.setVisibility(View.VISIBLE);
        }
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
    public void showGroupTaskDraw(boolean isSuccess, String message) {
        if (!CommUtil.checkIsNull(message))
            ToastUtils.showShort(message);
        if (isSuccess) {
            //刷新自己列表
            onRefresh();
            //通知组任务查询列表刷新
            EventBus.getDefault().post(new TaskDrawEvent(isSuccess));
        } else {

        }
    }

    @Override
    public void showAdminUnabsorbedTasks(List<TaskManageList.DataBean.TaskListBean> tasks) {
        llSorry.setVisibility(View.GONE);
        if (ADMIN_UNABSORBED.equals(mType)) {
            llUnabsorbed.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.GONE);
        } else {
            llUnabsorbed.setVisibility(View.GONE);
            tvBottom.setVisibility(View.VISIBLE);
        }
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
    public void showGiveUpTasksAndNotifyUi(boolean isSuccess, String message) {
        if (!CommUtil.checkIsNull(message)) {
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
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
            llUnabsorbed.setVisibility(View.GONE);
            tvBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAdminTaskDetailUi(TypeTaskData.DataBean.TaskListBean taskListBean) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailFragment.TASKLISTBEAN, taskListBean);
        mActivity.mSwipeBackHelper.forward(intent);
    }

    @Override
    public void showLoadingView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
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
        taskCountListBeanList.clear();
        taskManageList.clear();
        taskQueryBeanList.clear();
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        getFirstData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
    private void doGetDatas() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            getFirstData();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
                new AppSettingsDialog.Builder(this)
                        .setRequestCode(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
                        .setTitle("权限获取失败")
                        .setRationale(R.string.setting_group_and_storage)
                        .build().show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            // Do something after user returned from app settings screen, like showing a Toast.
            doGetDatas();
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

    private List<GroupTaskQuery.DataBean.TaskListBean> getCheckUserIdsByTask() {
        if (taskQueryBeanList != null) {
            List<GroupTaskQuery.DataBean.TaskListBean> lists = new ArrayList<>();
            for (GroupTaskQuery.DataBean.TaskListBean bean : taskQueryBeanList) {
                if (bean.isChecked()) {
                    lists.add(bean);
                }
            }
            return lists;
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
            TaskAbandonRequest request = null;
            for (TaskManageList.DataBean.TaskListBean bean : taskManageList) {
                if (bean.isChecked()) {
                    request = new TaskAbandonRequest();
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

    public List<TaskManageList.DataBean.TaskListBean> getSelectedTaskListBean() {
        List<TaskManageList.DataBean.TaskListBean> TaskListBeanList = new ArrayList<>();
        for (TaskManageList.DataBean.TaskListBean bean : taskManageList) {
            if (bean.isChecked()) {
                TaskListBeanList.add(bean);
            }
        }
        return TaskListBeanList;
    }
}
