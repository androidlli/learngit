package com.cango.palmcartreasure.trailer.task;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.baseAdapter.OnLoadMoreListener;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailActivity;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

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

/**
 * Created by cango on 2017/4/7.
 */

public class TrailerTasksFragment extends BaseFragment implements TaskContract.View, SwipeRefreshLayout.OnRefreshListener, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 120;
    public static final String NEW_TASK = "newTask";
    public static final String INPROGRESS_TASK = "inprogressTask";
    public static final String DONE_TASK = "doneTask";
    public static final String SEARCH = "search";
    private static final String TYPE = "type";
    @BindView(R.id.toolbar_task)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_task_down)
    ImageView ivDown;
    @BindView(R.id.ll_task_search)
    LinearLayout llSearch;
    @BindView(R.id.swipeRefreshLayout_task)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView_task)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;

    @OnClick({R.id.ll_task_search, R.id.ll_select_task})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_task_search:
                rlShadow.setVisibility(View.VISIBLE);
                mSearchPW.update();
                mSearchPW.showAsDropDown(mToolbar);
                break;
            case R.id.ll_select_task:
                if (SEARCH.equals(mType)) {

                } else {
                    ivDown.setImageResource(R.drawable.upward);
                    rlShadow.setVisibility(View.VISIBLE);
                    mSelectPW.update();
                    mSelectPW.showAsDropDown(mToolbar);
                }
                break;
            default:
                break;
        }
    }

    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;
    /**
     * newTask;inprogressTask;doneTask
     */
    private String mType;
    private TaskContract.Presenter mPresenter;
    private TrailerTasksActivity mActivity;
    private TrailerTaskAdapter mAdapter;
    private List<TypeTaskData.DataBean.TaskListBean> taskListBeanList = new ArrayList<>();
    private PopupWindow mSearchPW, mSelectPW;
    //定位相关
    private AMapLocationClient mLocationClient;
    private boolean isShouldFirstAddData=true;
    private double mLat, mLon;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mLat = 0;
                mLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    if (!CommUtil.checkIsNull(aMapLocation.getLatitude())) {
//                        BigDecimal latBD = new BigDecimal(String.valueOf(aMapLocation.getLatitude()));
//                        mLat = latBD.floatValue();
                        mLat=aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
//                        BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
//                        mLon = lonBD.floatValue();
                        mLon=aMapLocation.getLongitude();
                    }
                    if (mLat>0&&mLon>0){
                        if (isShouldFirstAddData){
                            mLoadView.smoothToHide();
                            isShouldFirstAddData=false;
                            getData();
                        }
                    }
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    String dateString = df.format(date);
//                    Logger.d(dateString + ": Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
                } else {
                    mLat = 0;
                    mLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode == 12 || errorCode == 13) {
                        ToastUtils.showLong(R.string.put_sim_and_permissions);
                    }
                    Logger.d("errorCode = " + aMapLocation.getErrorCode() + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public static TrailerTasksFragment getInstance(String type) {
        TrailerTasksFragment trailerTasksFragment = new TrailerTasksFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        trailerTasksFragment.setArguments(bundle);
        return trailerTasksFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer_task;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new TrailerTaskAdapter(mActivity, taskListBeanList, true,mType);
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                if (!CommUtil.checkIsNull(mType)){
                    if (mLat>0&&mLon>0){
                        mPresenter.loadTasks(mType, mLat, mLon, false, mPageCount, PAGE_SIZE);
                    }else {
                        ToastUtils.showShort(R.string.no_get_location);
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<TypeTaskData.DataBean.TaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, TypeTaskData.DataBean.TaskListBean data, int position) {
                mPresenter.openDetailTask(data);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSearchPW = getPopupWindow(getActivity(), R.layout.task_search_popup);
        mSelectPW = getPopupWindow(getActivity(), R.layout.task_selectstatu_popup);

        openPermissions();
    }

    @Override
    protected void initData() {
        if (!CommUtil.checkIsNull(getArguments())) {
            mType = getArguments().getString(TYPE);
        }
        mActivity = (TrailerTasksActivity) getActivity();
        initLocation();
    }

    @Override
    public void setPresenter(TaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTasksIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTasksError() {
        if (isLoadMore) {
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoTasks() {
        if (isLoadMore) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showTaskDetailUi(TypeTaskData.DataBean.TaskListBean taskListBean) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailFragment.TASKLISTBEAN, taskListBean);
        mActivity.mSwipeBackHelper.forward(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showTasks(final List<TypeTaskData.DataBean.TaskListBean> tasks) {
        llSorry.setVisibility(View.GONE);
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
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        taskListBeanList.clear();
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        if (mLat > 0 && mLon > 0) {
            if (!CommUtil.checkIsNull(mType))
                mPresenter.loadTasks(mType, mLat, mLon,
                        true, mPageCount, PAGE_SIZE);
        } else {
            ToastUtils.showShort("位置获取失败！");
        }
    }

    private void getData() {
        String title;
        if (mLat > 0 && mLon > 0) {
            if (INPROGRESS_TASK.equals(mType)) {
                title = getString(R.string.for_task);
                ivDown.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
                //开启presenter
                mPresenter.start();
                mPresenter.loadTasks(mType, mLat, mLon,
                        true, mPageCount, PAGE_SIZE);
            } else if (NEW_TASK.equals(mType)) {
                title = getString(R.string.new_task);
                ivDown.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
                //开启presenter
                mPresenter.start();
                mPresenter.loadTasks(mType, mLat, mLon,
                        true, mPageCount, PAGE_SIZE);
            } else if (DONE_TASK.equals(mType)) {
                title = getString(R.string.complete_task);
                ivDown.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
                //开启presenter
                mPresenter.start();
                mPresenter.loadTasks(mType, mLat, mLon,
                        true, mPageCount, PAGE_SIZE);
            } else {
                title = getString(R.string.query);
                ivDown.setVisibility(View.INVISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                mPresenter.start();
                mPresenter.loadTasks(mType, mLat, mLon,
                        true, mPageCount, PAGE_SIZE);
            }
            tvTitle.setText(title);
        } else {
            ToastUtils.showLong("位置获取失败！");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mLocationClient.startLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mLocationClient.startLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_group_and_storage)
                    .build().show();
        }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            openPermissions();
        }
    }

    private void changeTypeThenRefresh(String type) {
        mType = type;
        onRefresh();
    }

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        if (layoutId == R.layout.task_selectstatu_popup) {
            popupView.findViewById(R.id.tv_proceed_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mActivity.mSwipeBackHelper.forward(TaskDetailActivity.class);
                    tvTitle.setText(R.string.for_task);
                    changeTypeThenRefresh(INPROGRESS_TASK);
                    popupWindow.dismiss();

                }
            });
            popupView.findViewById(R.id.tv_new_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvTitle.setText(R.string.new_task);
                    changeTypeThenRefresh(NEW_TASK);
                    popupWindow.dismiss();
                }
            });
            popupView.findViewById(R.id.tv_complete_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvTitle.setText(R.string.complete_task);
                    changeTypeThenRefresh(DONE_TASK);
                    popupWindow.dismiss();
                }
            });
        }
        if (layoutId == R.layout.task_search_popup) {
            final EditText etId = (EditText) popupView.findViewById(R.id.et_id);
            final EditText etName = (EditText) popupView.findViewById(R.id.et_name);
            RadioGroup rgDate = (RadioGroup) popupView.findViewById(R.id.rg_date);
            final RadioButton rbMonth = (RadioButton) popupView.findViewById(R.id.rb_month);
            final RadioButton rbHalfYear = (RadioButton) popupView.findViewById(R.id.rb_half_year);
            final RadioButton rbYear = (RadioButton) popupView.findViewById(R.id.rb_year);
            Button btnCancal = (Button) popupView.findViewById(R.id.btn_cancal);
            btnCancal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etId.setText(null);
//                    etId.setHint("申请编号");
                    etName.setText(null);
//                    etName.setHint("客户姓名");
                    rbMonth.setChecked(false);
                    rbHalfYear.setChecked(false);
                    rbYear.setChecked(false);
                }
            });
            Button btnConfirm = (Button) popupView.findViewById(R.id.btn_confirm);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = etId.getText().toString().trim();
                    String name = etName.getText().toString().trim();
//                    时间标识（0：当月；1：半年；2：一年）
                    int dateFlag = -1;
                    if (rbMonth.isChecked()) {
                        dateFlag = 0;
                    }
                    if (rbHalfYear.isChecked()) {
                        dateFlag = 1;
                    }
                    if (rbYear.isChecked()) {
                        dateFlag = 2;
                    }
                    if (CommUtil.checkIsNull(id) && CommUtil.checkIsNull(name) && dateFlag > 0) {
                        ToastUtils.showLong(R.string.please_input_search_conditions);
                    } else {
                        taskListBeanList.clear();
                        mPresenter.taskQuery(true, mType, id, name, dateFlag, mPageCount, PAGE_SIZE);
                        popupWindow.dismiss();
                    }
                }
            });
        }
        popupWindow.setContentView(popupView);
        // TODO: 2016/5/17 设置动画
        // TODO: 2016/5/17 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        popupWindow.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (layoutId == R.layout.task_selectstatu_popup) {
                    ivDown.setImageResource(R.drawable.down);
                }
                rlShadow.setVisibility(View.GONE);
            }
        });
        // TODO：更新popupwindow的状态
        popupWindow.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        return popupWindow;
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(mActivity.getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        mLocationClient.setLocationListener(mLoactionListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
}
