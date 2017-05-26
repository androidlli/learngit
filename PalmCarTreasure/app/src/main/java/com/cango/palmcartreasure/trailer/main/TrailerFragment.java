package com.cango.palmcartreasure.trailer.main;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.customview.CalendarDialogFragment;
import com.cango.palmcartreasure.customview.DotTrailerDialogFragment;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TrailerEvent;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.trailer.map.TrailerMapActivity;
import com.cango.palmcartreasure.trailer.map.TrailerMapFragment;
import com.cango.palmcartreasure.trailer.mine.MineActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksFragment;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailActivity;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.rd.Orientation;
import com.rd.PageIndicatorView;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class TrailerFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 140;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @BindView(R.id.toolbar_trailer)
    Toolbar mToolbarTrailer;
    @BindView(R.id.vp_trailer)
    ViewPager mViewPager;
    @BindView(R.id.iv_vp)
    ImageView ivVp;
    @BindView(R.id.ll_for_task)
    LinearLayout llForTask;
    @BindView(R.id.ll_new_task)
    LinearLayout llNewTask;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView mPageIndicatorView;
    @BindView(R.id.ll_trailer_center)
    LinearLayout llCenter;
    @BindView(R.id.ll_start_task)
    LinearLayout llStartTask;
    @BindView(R.id.ll_trailer_navigation)
    LinearLayout llTrailerNav;
    @BindView(R.id.ll_dot_trailer)
    LinearLayout llDotTrailer;
    @BindView(R.id.ll_send_car_library)
    LinearLayout llSendCarLibrary;
    @BindView(R.id.iv_task)
    ImageView ivTask;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    private Badge badge1, badge2;

    @OnClick({R.id.iv_start_task, R.id.ll_tasks, R.id.ll_mine, R.id.ll_trailer_navigation, R.id.ll_dot_trailer, R.id.ll_send_car_library,
            R.id.ll_for_task, R.id.ll_query, R.id.ll_new_task, R.id.ll_complete_task})
    public void onClick(View view) {
        switch (view.getId()) {
            //开始任务
            case R.id.iv_start_task:
                if (!CommUtil.checkIsNull(currentBean)) {
                    if (currentBean.getIsStart().equals("T"))
                        showCalendarDialog();
                    else {
                        ToastUtils.showLong("不能开始任务了！");
                    }
                }
                break;
            case R.id.ll_mine:
                mActivity.mSwipeBackHelper.forward(MineActivity.class);
                break;
            case R.id.ll_trailer_navigation:
                Intent trailNavIntent = new Intent(mActivity, TrailerMapActivity.class);
                trailNavIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.TRAILER_NAV);
                trailNavIntent.putExtra(TrailerMapFragment.TASKLISTBEAN, currentBean);
                mActivity.mSwipeBackHelper.forward(trailNavIntent);
                break;
            //拖车打点如果可以大点才可以,因为进行开始任务、拖车大点、拖车入库、都会重新请求数据，并且把当前页数保存
            case R.id.ll_dot_trailer:
//                openCamera();
                if (!CommUtil.checkIsNull(currentBean)) {
                    if (currentBean.getIsCheckPoint().equals("T"))
                        openCamera();
                    else {
                        ToastUtils.showLong("请先开始任务！");
                    }
                }
                break;
            case R.id.ll_send_car_library:
                if (!CommUtil.checkIsNull(currentBean)) {
                    if (currentBean.getIsDone().equals("T")) {
                        Intent sendCarLibraryIntent = new Intent(mActivity, TrailerMapActivity.class);
                        sendCarLibraryIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.SEND_CAR_LIBRARY);
                        sendCarLibraryIntent.putExtra(TrailerMapFragment.TASKLISTBEAN, currentBean);
                        mActivity.mSwipeBackHelper.forward(sendCarLibraryIntent);
                    } else {
                        ToastUtils.showLong("请先拖车打点！");
                    }
                }
                break;
            case R.id.ll_for_task:
                Intent tasksIntent = new Intent(mActivity, TrailerTasksActivity.class);
                tasksIntent.putExtra("type", TrailerTasksFragment.INPROGRESS_TASK);
                mActivity.mSwipeBackHelper.forward(tasksIntent);
                break;
            case R.id.ll_new_task:
                Intent taskNewIntent = new Intent(mActivity, TrailerTasksActivity.class);
                taskNewIntent.putExtra("type", TrailerTasksFragment.NEW_TASK);
                mActivity.mSwipeBackHelper.forward(taskNewIntent);
                break;
            case R.id.ll_complete_task:
                Intent taskCompleteIntent = new Intent(mActivity, TrailerTasksActivity.class);
                taskCompleteIntent.putExtra("type", TrailerTasksFragment.DONE_TASK);
                mActivity.mSwipeBackHelper.forward(taskCompleteIntent);
                break;
            case R.id.ll_query:
                Intent queryTasksIntent = new Intent(mActivity, TrailerTasksActivity.class);
                queryTasksIntent.putExtra("type", TrailerTasksFragment.SEARCH);
                mActivity.mSwipeBackHelper.forward(queryTasksIntent);
                break;
            default:
                break;
        }
    }

    private boolean isFromSMS;
    private String mCurrentPhotoPath;
    private int vpType = -1;//0-进行中 1-新分配 2-没有数据
    private TrailerActivity mActivity;
    private TrailerTaskService mService;
    private List<View> viewList = new ArrayList<>();
    private MainPageOtherAdapter mPageAdapter;
    private List<TypeTaskData.DataBean.TaskListBean> taskListBeanList = new ArrayList<>();
    private TypeTaskData.DataBean.TaskListBean currentBean;
    private int currentPosition = 0;
    private boolean isShowPosition;
    private CalendarDialogFragment mCalendarDialog;
    private DotTrailerDialogFragment mDotDialog;
    //定位相关
    private AMapLocationClient mLocationClient;
    private boolean isShouldFirstAddData = true;
    private double mLat, mLon;
    private double pointLat, pointLon;
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
                        mLat = aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
//                        BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
//                        mLon = lonBD.floatValue();
                        mLon = aMapLocation.getLongitude();
                    }
                    if (mLat > 0 && mLon > 0) {
                        if (isShouldFirstAddData) {
                            isShouldFirstAddData = false;
                            addData();
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
                        ToastUtils.showShort(R.string.put_sim_and_permissions);
                    }
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public static TrailerFragment newInstance() {
        TrailerFragment fragment = new TrailerFragment();
        return fragment;
    }

    public static TrailerFragment newInstance(boolean isFromSMS) {
        TrailerFragment fragment = new TrailerFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFromSMS", isFromSMS);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer;
    }

    @Override
    protected void initView() {
        ivTask.setImageResource(R.drawable.task_on);
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbarTrailer.setLayoutParams(layoutParams);
            mToolbarTrailer.setPadding(0, statusBarHeight, 0, 0);
        }
        mPageAdapter = new MainPageOtherAdapter();
        mPageAdapter.setViewList(viewList);
        mViewPager.setAdapter(mPageAdapter);
        mPageIndicatorView.setViewPager(mViewPager);
        mPageIndicatorView.setOrientation(Orientation.HORIZONTAL);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                currentBean = taskListBeanList.get(position);
                if (vpType == 0) {
                    llStartTask.setVisibility(View.GONE);
                } else if (vpType == 1) {
                    llStartTask.setVisibility(View.VISIBLE);
                } else {

                }
                ObjectAnimator.ofFloat(llCenter, "rotationX", 0.0F, 360F)
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mService = NetManager.getInstance().create(TrailerTaskService.class);
        mLoadView.smoothToShow();
        llCenter.setVisibility(View.INVISIBLE);
        ivVp.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.INVISIBLE);

        openPermissions();
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
        Logger.d("onPermissionsGranted");
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
//    }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pointLat = mLat;
            pointLon = mLon;
            showDotDialog();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            deleteImageFile();
        }
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            openPermissions();
        }
    }

    private void addData() {
        if (mLat > 0 && mLon > 0) {
            mService.getTaskInprogressList(MtApplication.mSPUtils.getInt(Api.USERID), mLat,
                    mLon, 1, 5)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TypeTaskData>() {
                        @Override
                        protected void _onNext(TypeTaskData o) {
                            if (isAdded()) {
                                int code = o.getCode();
                                if (code == 0) {
                                    if (CommUtil.checkIsNull(o.getData())) {
                                        doNewTask();
                                    } else {
                                        if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0) {
                                            mLoadView.smoothToHide();
                                            updateView(0, o);

                                        } else {
                                            doNewTask();
                                        }
                                    }
                                } else {
                                    mLoadView.smoothToHide();
                                    llCenter.setVisibility(View.INVISIBLE);
                                    ivVp.setVisibility(View.VISIBLE);
                                    mViewPager.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (isAdded()) {
                                mLoadView.smoothToHide();
                                llCenter.setVisibility(View.INVISIBLE);
                                ivVp.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    @Override
    protected void initData() {
        mActivity = (TrailerActivity) getActivity();
        if (!CommUtil.checkIsNull(getArguments())) {
            isFromSMS = getArguments().getBoolean("isFromSMS");
        }
        initLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFromSMS) {
            MtApplication.clearExceptLastActivitys();
            isFromSMS = false;

            mLoadView.smoothToShow();
            isShowPosition = true;

            viewList.clear();
            taskListBeanList.clear();
            currentBean = null;
            vpType = -1;
            if (!CommUtil.checkIsNull(badge1)) {
                badge1.hide(true);
            }
            if (!CommUtil.checkIsNull(badge2)) {
                badge2.hide(true);
            }
            mPageAdapter.notifyDataSetChanged();
            addData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushTrailerEvent(TrailerEvent trailerEvent){
        mLoadView.smoothToShow();
        isShowPosition = true;

        viewList.clear();
        taskListBeanList.clear();
        currentBean = null;
        vpType = -1;
        if (!CommUtil.checkIsNull(badge1)) {
            badge1.hide(true);
        }
        if (!CommUtil.checkIsNull(badge2)) {
            badge2.hide(true);
        }
        mPageAdapter.notifyDataSetChanged();
        addData();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
    }

    private void doNewTask() {
        mService.getNewTaskList(MtApplication.mSPUtils.getInt(Api.USERID), mLat,
                mLon, 1, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TypeTaskData>() {
                    @Override
                    protected void _onNext(TypeTaskData o) {
                        if (isAdded()) {
                            mLoadView.smoothToHide();
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    llCenter.setVisibility(View.INVISIBLE);
                                    ivVp.setVisibility(View.VISIBLE);
                                    mViewPager.setVisibility(View.INVISIBLE);
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0) {
                                        mLoadView.smoothToHide();
                                        updateView(1, o);
                                    } else {
                                        llCenter.setVisibility(View.INVISIBLE);
                                        ivVp.setVisibility(View.VISIBLE);
                                        mViewPager.setVisibility(View.INVISIBLE);
                                    }
                                }
                            } else {
                                llCenter.setVisibility(View.INVISIBLE);
                                ivVp.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (isAdded()) {
                            mLoadView.smoothToHide();
                            llCenter.setVisibility(View.INVISIBLE);
                            ivVp.setVisibility(View.VISIBLE);
                            mViewPager.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void updateView(int type, TypeTaskData o) {
        vpType = type;
        llCenter.setVisibility(View.VISIBLE);
        ivVp.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        badge1 = null;
        badge2 = null;
        if (type == 0) {
            badge1 = new QBadgeView(getActivity())
                    .setBadgeBackgroundColor(Color.WHITE)
                    .setShowShadow(false)
                    .setBadgeTextColor(getResources().getColor(R.color.colorPrimary))
                    .bindTarget(llForTask)
                    .setBadgeNumber(o.getData().getTotalCount())
                    .setBadgeGravity(Gravity.TOP | Gravity.END)
                    .setGravityOffset(SizeUtil.px2dp(getContext(), 28), true);
        } else if (type == 1) {
            badge2 = new QBadgeView(getActivity())
                    .setBadgeBackgroundColor(Color.WHITE)
                    .setShowShadow(false)
                    .setBadgeTextColor(getResources().getColor(R.color.colorPrimary))
                    .bindTarget(llNewTask)
                    .setBadgeNumber(o.getData().getTotalCount())
                    .setBadgeGravity(Gravity.TOP | Gravity.END)
                    .setGravityOffset(SizeUtil.px2dp(getContext(), 28), true);
        }

        for (final TypeTaskData.DataBean.TaskListBean bean : o.getData().getTaskList()) {
            taskListBeanList.add(bean);
            View inflate = mActivity.getLayoutInflater().inflate(R.layout.trailer_vp_item, null);
            AutoRelativeLayout arlContain = (AutoRelativeLayout) inflate.findViewById(R.id.arl_contain);
            TextView tvPlate = (TextView) inflate.findViewById(R.id.tv_vp_item_plate);
            TextView tvName = (TextView) inflate.findViewById(R.id.tv_vp_item_name);
            TextView tvMents = (TextView) inflate.findViewById(R.id.tv_vp_item_monthpayments);
            TextView tvDistance = (TextView) inflate.findViewById(R.id.tv_distance);
            TextView tvTag = (TextView) inflate.findViewById(R.id.tv_vp_item_tag);
            TextView tvStatus = (TextView) inflate.findViewById(R.id.tv_status);
            tvPlate.setText(bean.getCustomerName());
            tvName.setText(bean.getApplyCD());
            tvMents.setText(bean.getCarPlateNO());
            tvDistance.setText(bean.getDistance());
            tvTag.setText(bean.getShortName());
            tvStatus.setText(bean.getFlowStauts());
            arlContain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra(TaskDetailFragment.TASKLISTBEAN, bean);
                    mActivity.mSwipeBackHelper.forward(intent);
                }
            });
            viewList.add(inflate);
        }
        currentBean = taskListBeanList.get(0);
        mPageAdapter.notifyDataSetChanged();
        if (isShowPosition) {
            mViewPager.setCurrentItem(currentPosition);
            isShowPosition = false;
        }
        if (vpType == 0) {
            llStartTask.setVisibility(View.GONE);
        } else if (vpType == 1) {
            llStartTask.setVisibility(View.VISIBLE);
        } else {
        }
    }

    private void showDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {
            mDotDialog = new DotTrailerDialogFragment();
            mDotDialog.setmListener(new DotTrailerDialogFragment.OnStatusListener() {
                @Override
                public void onStatusClick(final int type) {
                    File file = new File(mCurrentPhotoPath);
                    Luban.get(mActivity)
                            .load(file)                     //传人要压缩的图片
                            .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                            .setCompressListener(new OnCompressListener() { //设置回调

                                @Override
                                public void onStart() {
                                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    if (isAdded()) {
                                        mLoadView.smoothToShow();
                                    }
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // TODO 压缩成功后调用，返回压缩后的图片文件
                                    upLoadImage(type, file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过去出现问题时调用
                                    if (isAdded()) {
                                        mLoadView.smoothToHide();
                                    }
                                }
                            }).launch();    //启动压缩
                }
            });
        }
        if (mDotDialog.isVisible()) {

        } else {
            mDotDialog.show(getFragmentManager(), "DotDialog");
        }
    }

    private void upLoadImage(int type, File file) {
        if (mLat > 0 && mLon > 0 && pointLat > 0 && pointLon > 0) {
            String notifyCust = type == 0 ? "1" : "0";
            RequestBody userId = RequestBody.create(null, MtApplication.mSPUtils.getInt(Api.USERID) + "");
            RequestBody LAT = RequestBody.create(null, pointLat + "");
            RequestBody LON = RequestBody.create(null, pointLon + "");
            RequestBody agencyID = RequestBody.create(null, currentBean.getAgencyID() + "");
            RequestBody caseID = RequestBody.create(null, currentBean.getCaseID() + "");
            RequestBody notifyCustImm = RequestBody.create(null, notifyCust);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), photoBody);
            mService.checkPiontSubmit(userId, LAT, LON, agencyID, caseID, notifyCustImm, part)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskAbandon>() {
                        @Override
                        protected void _onNext(TaskAbandon o) {
                            if (isAdded()) {
                                deleteImageFile();
                                mLoadView.smoothToHide();
                            }
                            //拖车大点是否成功，如果成功的话，从新刷新vp
                            int code = o.getCode();
                            if (!CommUtil.checkIsNull(o.getMsg())) {
                                ToastUtils.showLong(o.getMsg());
                            }
                            if (code == 0) {

                                isShowPosition = true;

                                viewList.clear();
                                taskListBeanList.clear();
                                currentBean = null;
                                vpType = -1;
                                if (!CommUtil.checkIsNull(badge1)) {
                                    badge1.hide(true);
                                }
                                if (!CommUtil.checkIsNull(badge2)) {
                                    badge2.hide(true);
                                }
                                mPageAdapter.notifyDataSetChanged();
                                addData();
                            } else {

                            }
                        }

                        @Override
                        protected void _onError() {
                            if (isAdded()) {
                                deleteImageFile();
                                mLoadView.smoothToHide();
                            }
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    private void closeDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {

        } else {
            if (mDotDialog.isVisible()) {
                mDotDialog.dismiss();
            }
        }
    }

    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {
            mCalendarDialog = new CalendarDialogFragment();
            mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date) {

                    if (mLat > 0 && mLon > 0) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateString = formatter.format(date);
                        if (isAdded()) {
                            mLoadView.smoothToShow();
                        }
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
                        objectMap.put("agencyID", currentBean.getAgencyID());
                        objectMap.put("caseID", currentBean.getCaseID());
                        objectMap.put("planDonetime", dateString);
                        objectMap.put("LAT", mLat);
                        objectMap.put("LON", mLon);
                        mService.startTaskSubmit(objectMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new RxSubscriber<TaskAbandon>() {
                                    @Override
                                    protected void _onNext(TaskAbandon o) {
                                        if (isAdded()) {
                                            mLoadView.smoothToHide();
                                            int code = o.getCode();
                                            if (code == 0) {
                                                //刷新viewpager
//                                                mFragments.clear();
                                                viewList.clear();
                                                taskListBeanList.clear();
                                                currentBean = null;
                                                vpType = -1;
                                                if (!CommUtil.checkIsNull(badge1)) {
                                                    badge1.hide(true);
                                                }
                                                if (!CommUtil.checkIsNull(badge2)) {
                                                    badge2.hide(true);
                                                }
                                                mPageAdapter.notifyDataSetChanged();
                                                addData();
                                            } else {

                                            }
                                        }
                                    }

                                    @Override
                                    protected void _onError() {
                                        if (isAdded()) {
                                            mLoadView.smoothToHide();
                                        }
                                    }
                                });
                    } else {
                        ToastUtils.showShort(R.string.no_get_location);
                    }
                }
            });
        }
        if (mCalendarDialog.isVisible()) {

        } else {
            mCalendarDialog.show(getFragmentManager(), "CalendarDialog");
        }
    }

    private void closeCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {

        } else {
            if (mCalendarDialog.isVisible()) {
                mCalendarDialog.dismiss();
            }
        }
    }

    private void openCamera() {
        boolean hasSystemFeature = mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (hasSystemFeature) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        photoURI = FileProvider.getUriForFile(mActivity, "com.cango.palmcartreasure.fileprovider", photoFile);

                    } else {
                        photoURI = Uri.fromFile(photoFile);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean deleteImageFile() {
        if (mCurrentPhotoPath != null) {
            File emptyFile = new File(mCurrentPhotoPath);
            if (emptyFile.exists())
                return emptyFile.delete();
        }
        return false;
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
