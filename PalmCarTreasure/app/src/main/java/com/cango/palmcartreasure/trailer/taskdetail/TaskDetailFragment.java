package com.cango.palmcartreasure.trailer.taskdetail;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.cango.palmcartreasure.model.TaskDetailData;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.trailer.map.TrailerMapActivity;
import com.cango.palmcartreasure.trailer.map.TrailerMapFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.FileUtils;
import com.cango.palmcartreasure.util.ScreenUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class TaskDetailFragment extends BaseFragment implements TaskDetailContract.View, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 150;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final String TYPE = "type";
    public static final String TASKLISTBEAN = "taskListBean";
    @BindView(R.id.toolbar_detail)
    Toolbar mToolbar;
    @BindView(R.id.tv_applyno)
    TextView tvApplyNo;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.recyclerView_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;
    private SubActionButton subActionButton1, subActionButton2, subActionButton3, subActionButton4;

    @OnClick({R.id.tv_download, R.id.ll_callrecord, R.id.ll_home_visit, R.id.ll_case_info, R.id.ll_customer_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_download:
                rlShadow.setVisibility(View.VISIBLE);
                downloadPW.update();
                downloadPW.showAsDropDown(mToolbar);
                break;
            case R.id.ll_callrecord:
                iv1.setImageResource(R.drawable.telephone_urged);
                iv2.setImageResource(R.drawable.home_information_off);
                iv3.setImageResource(R.drawable.case_information_off);
                iv4.setImageResource(R.drawable.customer_information_off);
                onRefresh(0);
                break;
            case R.id.ll_home_visit:
                iv1.setImageResource(R.drawable.telephone_collection_off);
                iv2.setImageResource(R.drawable.family_visit);
                iv3.setImageResource(R.drawable.case_information_off);
                iv4.setImageResource(R.drawable.customer_information_off);
                onRefresh(1);
                break;
            case R.id.ll_case_info:
                iv1.setImageResource(R.drawable.telephone_collection_off);
                iv2.setImageResource(R.drawable.home_information_off);
                iv3.setImageResource(R.drawable.case_information);
                iv4.setImageResource(R.drawable.customer_information_off);
                onRefresh(2);
                break;
            case R.id.ll_customer_info:
                iv1.setImageResource(R.drawable.telephone_collection_off);
                iv2.setImageResource(R.drawable.home_information_off);
                iv3.setImageResource(R.drawable.case_information_off);
                iv4.setImageResource(R.drawable.customer_information);
                onRefresh(3);
                break;
        }
    }

    private int mType;
    //是不是进行中任务
    private boolean isStart;
    private String mCurrentPhotoPath;
    private TypeTaskData.DataBean.TaskListBean mTaskListBean;
    private TaskDetailActivity mActivity;
    private TaskDetailContract.Presenter mPresenter;
    private TrailerTaskService mService;
    private SectionedRecyclerViewAdapter mAdapter;
    private PopupWindow downloadPW;
    private CalendarDialogFragment mCalendarDialog;
    private DotTrailerDialogFragment mDotDialog;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private double pointLat, pointLon;
    private boolean isCheckPoint;
    private boolean isStartTask;
    //开始下载任务
    private boolean isDoDownLoad;
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

    public static TaskDetailFragment newInstance(int type, TypeTaskData.DataBean.TaskListBean taskListBean) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putParcelable(TASKLISTBEAN, taskListBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_task_detail;
    }

    @Override
    protected void initView() {
        isStart = mTaskListBean.getIsStart().equals("T");
        tvApplyNo.setText(mTaskListBean.getCustomerName());
        tvId.setText(mTaskListBean.getApplyCD());
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
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

        mAdapter = new SectionedRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mService = NetManager.getInstance().create(TrailerTaskService.class);

        //表示是已完成任务不需要显示星际菜单
        if ("F".equals(mTaskListBean.getIsStart()) && "F".equals(mTaskListBean.getIsCheckPoint()) && "F".equals(mTaskListBean.getIsDone())) {

        } else {
            final ImageView fabIconNew = new ImageView(mActivity);
            fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
            final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(mActivity)
                    .setContentView(fabIconNew)
                    .build();
            rightLowerButton.setAlpha(0.5f);
            SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(mActivity);
            ImageView rlIcon1 = new ImageView(mActivity);
            ImageView rlIcon2 = new ImageView(mActivity);
            ImageView rlIcon3 = new ImageView(mActivity);
            ImageView rlIcon4 = new ImageView(mActivity);

            rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.start_task));
            rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.trailer_navigation));
            rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.dot_trailer));
            rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.send_car_library));
            FrameLayout.LayoutParams subParams = new FrameLayout.LayoutParams(SizeUtil.dp2px(mActivity, 50), SizeUtil.dp2px(mActivity, 50));
            subActionButton1 = rLSubBuilder.setContentView(rlIcon1).setLayoutParams(subParams).build();
            subActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommUtil.checkIsNull(mTaskListBean)) {
                        if (mTaskListBean.getIsStart().equals("T")) {
//                            showCalendarDialog();
                            isStartTask = true;
                            openPermissions();
                        } else {
                            ToastUtils.showLong("不能开始任务了！");
                        }
                    }
                }
            });
            subActionButton2 = rLSubBuilder.setContentView(rlIcon2).setLayoutParams(subParams).build();
            subActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent trailNavIntent = new Intent(mActivity, TrailerMapActivity.class);
                    trailNavIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.TRAILER_NAV);
                    trailNavIntent.putExtra(TrailerMapFragment.TASKLISTBEAN, mTaskListBean);
                    mActivity.mSwipeBackHelper.forward(trailNavIntent);
                }
            });
            subActionButton3 = rLSubBuilder.setContentView(rlIcon3).setLayoutParams(subParams).build();
            subActionButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommUtil.checkIsNull(mTaskListBean)) {
                        if (mTaskListBean.getIsCheckPoint().equals("T")) {
//                            openCamera();
                            isCheckPoint = true;
                            openPermissions();
                        } else {
                            ToastUtils.showLong("请先开始任务！");
                        }
                    }
                }
            });
            subActionButton4 = rLSubBuilder.setContentView(rlIcon4).setLayoutParams(subParams).build();
            subActionButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommUtil.checkIsNull(mTaskListBean)) {
                        if (mTaskListBean.getIsDone().equals("T")) {
                            Intent sendCarLibraryIntent = new Intent(mActivity, TrailerMapActivity.class);
                            sendCarLibraryIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.SEND_CAR_LIBRARY);
                            sendCarLibraryIntent.putExtra(TrailerMapFragment.TASKLISTBEAN, mTaskListBean);
                            mActivity.mSwipeBackHelper.forward(sendCarLibraryIntent);
                        } else {
                            ToastUtils.showLong("请先拖车打点！");
                        }
                    }
                }
            });

            FloatingActionMenu rightLowerMenu;
            if (isStart) {
                rightLowerMenu = new FloatingActionMenu.Builder(mActivity)
                        .addSubActionView(subActionButton1)
                        .addSubActionView(subActionButton2)
                        .addSubActionView(subActionButton3)
                        .addSubActionView(subActionButton4)
                        .setRadius(ScreenUtil.getScreenWidth(mActivity) / 3)
                        .attachTo(rightLowerButton)
                        .build();
            } else {
                rightLowerMenu = new FloatingActionMenu.Builder(mActivity)
                        .addSubActionView(subActionButton2)
                        .addSubActionView(subActionButton3)
                        .addSubActionView(subActionButton4)
                        .setRadius(ScreenUtil.getScreenWidth(mActivity) / 3)
                        .attachTo(rightLowerButton)
                        .build();
            }

            rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingActionMenu menu) {
                    // Rotate the icon of rightLowerButton 45 degrees clockwise
                    fabIconNew.setRotation(0);
                    PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                    ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                    animation.start();
//                    rightLowerButton.setAlpha(1);
                    PropertyValuesHolder pvAA = PropertyValuesHolder.ofFloat(View.ALPHA, 1f);
                    ObjectAnimator animationAA = ObjectAnimator.ofPropertyValuesHolder(rightLowerButton, pvAA);
                    animationAA.start();
                }

                @Override
                public void onMenuClosed(FloatingActionMenu menu) {
                    // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                    fabIconNew.setRotation(45);
                    PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                    ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                    animation.start();
//                    rightLowerButton.setAlpha(0.3f);
                    PropertyValuesHolder pvAA = PropertyValuesHolder.ofFloat(View.ALPHA, 0.5f);
                    ObjectAnimator animationAA = ObjectAnimator.ofPropertyValuesHolder(rightLowerButton, pvAA);
                    animationAA.start();
                }
            });
        }

        iv1.setImageResource(R.drawable.telephone_urged);
        iv2.setImageResource(R.drawable.home_information_off);
        iv3.setImageResource(R.drawable.case_information_off);
        iv4.setImageResource(R.drawable.customer_information_off);
        downloadPW = getPopupWindow(getActivity(), R.layout.task_download);

        mPresenter.start();
        mPresenter.loadTaskDetail(mType, true, mTaskListBean.getAgencyID(), mTaskListBean.getCaseID());
        openPermissions();
    }

    @Override
    protected void initData() {
        mActivity = (TaskDetailActivity) getActivity();
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE);
            mTaskListBean = getArguments().getParcelable(TASKLISTBEAN);
        }
        initLocation();
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTaskDetailIndicator(boolean active) {
        if (isAdded()) {
            if (active) {
                mLoadView.smoothToShow();
            } else {
                mLoadView.smoothToHide();
            }
        }
    }

    @Override
    public void showTasksDetailError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        llSorry.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTaskDetail(TaskDetailData taskDetailData) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.removeAllSections();
//        mAdapter.addSection(new ExpandableContactsSection())
        List<TaskDetailData.TaskSection> taskSectionList = taskDetailData.getTaskSectionList();
//        if (mType != 2) {
        for (TaskDetailData.TaskSection section : taskSectionList) {
            mAdapter.addSection(new ExpandableContactsSection(section.getIvId(), section.getTitle(), section.getTaskInfoList()));
        }
//        }
//        else {
        //案件信息加入imei
//            int size1 = taskSectionList.get(0).getTaskInfoList().size();
//            TaskDetailData.TaskSection.TaskInfo remove1 = taskSectionList.get(0).getTaskInfoList().remove(size1 - 2);
//            int size2 = taskSectionList.get(0).getTaskInfoList().size();
//            TaskDetailData.TaskSection.TaskInfo remove2 = taskSectionList.get(0).getTaskInfoList().remove(size2 - 1);
//            for (int i = 0; i < taskSectionList.size(); i++) {
//                mAdapter.addSection(new ExpandableContactsSection(taskSectionList.get(i).getIvId(),
//                        taskSectionList.get(i).getTitle(), taskSectionList.get(i).getTaskInfoList()));
//            }
//            mAdapter.addSection(new ImeiSection(remove1.getRight()));
//            mAdapter.addSection(new ImeiSection(remove2.getRight()));
//        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDownLoadResult(boolean isSussess, File file, String msg) {
        ToastUtils.showShort(msg);
        if (isSussess) {
//            File file1=new File(file.getAbsolutePath());
//            Intent pdfFileIntent = getPdfFileIntent(file1);
//            startActivity(pdfFileIntent);
        } else {
        }
    }

    public void onRefresh(int type) {
        mType = type;
        mPresenter.loadTaskDetail(mType, true, mTaskListBean.getAgencyID(), mTaskListBean.getCaseID());
    }

    @Override
    public void showNoTaskDetail() {
        mRecyclerView.setVisibility(View.INVISIBLE);
//        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
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
            if (isStartTask) {
                isStartTask = false;
                showCalendarDialog();
            }
            if (isCheckPoint) {
                isCheckPoint = false;
                openCamera();
            }
//            if (isDoDownLoad){
//                isDoDownLoad=false;
//
//            }
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

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvLetter = (TextView) popupView.findViewById(R.id.tv_entrust_letter);
        tvLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLat > 0 && mLon > 0) {
                    popupWindow.dismiss();
                    rlShadow.setVisibility(View.GONE);
                    //判断委托函是否下载过
                    boolean checkDownOver = checkDownOver(mTaskListBean.getApplyCD(), "拖车委托函");
                    if (checkDownOver) {
                        ToastUtils.showShort("你已经下载过此文件了");
                    } else {
                        try {
                            String parentDir = createDownFile("委托函");
                            mPresenter.downLoadFile(mType, true, MtApplication.mSPUtils.getInt(Api.USERID), mTaskListBean.getAgencyID(),
                                    mTaskListBean.getCaseID(), "1", parentDir, new OnDownloadListener() {
                                        @Override
                                        public void onDownloadSuccess(File file) {
                                            if (isActive()) {
                                                showDownLoadResult(true, file, "下载成功！");
                                            }
                                        }

                                        @Override
                                        public void onDownloading(int progress) {

                                        }

                                        @Override
                                        public void onDownloadFailed(String error) {
                                            if (isActive()) {
                                                showDownLoadResult(false, null, "下载失败！");
                                            }
                                        }
                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ToastUtils.showShort(R.string.no_get_location);
                }
            }
        });
        TextView tvDoc = (TextView) popupView.findViewById(R.id.tv_trailer_doc);
        tvDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLat > 0 && mLon > 0) {
                    try {
                        popupWindow.dismiss();
                        rlShadow.setVisibility(View.GONE);
                        //判断委托函是否下载过
                        boolean checkDownOver = checkDownOver(mTaskListBean.getApplyCD(), "拖车任务书");
                        if (checkDownOver) {
                            ToastUtils.showShort("你已经下载过此文件了");
                        } else {
                            String parentDir = createDownFile("拖车任务书");
                            mPresenter.downLoadFile(mType, true, MtApplication.mSPUtils.getInt(Api.USERID), mTaskListBean.getAgencyID(),
                                    mTaskListBean.getCaseID(), "2", parentDir, new OnDownloadListener() {
                                        @Override
                                        public void onDownloadSuccess(File file) {
                                            Logger.d("onDownloadSuccess");
                                            if (isActive()) {
                                                showDownLoadResult(true, file, "下载成功！");
                                            }
                                        }

                                        @Override
                                        public void onDownloading(int progress) {

                                        }

                                        @Override
                                        public void onDownloadFailed(String error) {
                                            Logger.d("error:" + "error");
                                            if (isActive()) {
                                                showDownLoadResult(false, null, "下载失败！");
                                            }
                                        }
                                    });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort(R.string.no_get_location);
                }
            }
        });
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlShadow.setVisibility(View.GONE);
            }
        });
        popupWindow.update();
        return popupWindow;
    }

    private boolean checkDownOver(String applyCD, String type) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String suffix = ".pdf";
        if (storageDir == null || !FileUtils.isDir(storageDir)) return false;
        File[] files = storageDir.listFiles();
//        Arrays.sort(files, new DownloadFragment.CompratorByLastModified());
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    String fileName = file.getName();
                    if (fileName.contains(applyCD)) {
                        if (fileName.contains(type)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void showCalendarDialog() {
        if (mLat > 0 && mLon > 0) {
            if (CommUtil.checkIsNull(mCalendarDialog)) {
                mCalendarDialog = new CalendarDialogFragment();
                mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                    @Override
                    public void onCalendarClick(Date date) {
//                    if (mLat>0&&mLon>0){
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateString = formatter.format(date);
                        if (isAdded()) {
                            mLoadView.smoothToShow();
                        }
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
                        objectMap.put("agencyID", mTaskListBean.getAgencyID());
                        objectMap.put("caseID", mTaskListBean.getCaseID());
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
                                                mTaskListBean.setIsStart("F");
                                                mTaskListBean.setIsCheckPoint("T");
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
//                    }else {
//                        ToastUtils.showShort(R.string.no_get_location);
//                    }
                    }
                });
            }
            if (mCalendarDialog.isVisible()) {

            } else {
                mCalendarDialog.show(getFragmentManager(), "CalendarDialog");
            }
        } else {
            ToastUtils.showShort(R.string.no_get_location);
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

    private void showDotDialog() {
        if (mLat > 0 && mLon > 0 && pointLat > 0 && pointLon > 0) {
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
                                            mLoadView.smoothToShow();
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
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    private void upLoadImage(int type, File file) {
        String notifyCust = type == 0 ? "1" : "0";
        RequestBody userId = RequestBody.create(null, MtApplication.mSPUtils.getInt(Api.USERID) + "");
        RequestBody LAT = RequestBody.create(null, pointLat + "");
        RequestBody LON = RequestBody.create(null, pointLon + "");
        RequestBody agencyID = RequestBody.create(null, mTaskListBean.getAgencyID() + "");
        RequestBody caseID = RequestBody.create(null, mTaskListBean.getCaseID() + "");
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
                            mTaskListBean.setIsDone("T");
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
    }

    private void closeDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {

        } else {
            if (mDotDialog.isVisible()) {
                mDotDialog.dismiss();
            }
        }
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

    class ExpandableContactsSection extends StatelessSection {

        int ivId;
        String title;
        List<TaskDetailData.TaskSection.TaskInfo> list;
        boolean expanded = true;

        public ExpandableContactsSection(int ivId, String title, List<TaskDetailData.TaskSection.TaskInfo> list) {
            super(R.layout.section_ex4_header, R.layout.section_ex4_item);
            this.ivId = ivId;
            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? list.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            TaskDetailData.TaskSection.TaskInfo taskInfo = list.get(position);
            if (CommUtil.checkIsNull(taskInfo.getCenter())) {
                itemHolder.llTime.setVisibility(View.GONE);
            } else {
                itemHolder.llTime.setVisibility(View.VISIBLE);
            }
            itemHolder.tvExplain.setText(taskInfo.getLeft());
            itemHolder.tvExplain.setTextColor(getResources().getColor(taskInfo.getLeftColor()));

            itemHolder.tvTime.setText(taskInfo.getCenter());
            if (taskInfo.getCenterColor() > 0) {
                itemHolder.tvTime.setTextColor(getResources().getColor(taskInfo.getCenterColor()));
            } else {
                itemHolder.tvTime.setTextColor(getResources().getColor(R.color.mtce9c5e));
            }

            itemHolder.tvContent.setText(taskInfo.getRight());
            itemHolder.tvContent.setTextColor(getResources().getColor(taskInfo.getRightColor()));
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            if (expanded) {
                headerHolder.imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp);
            } else {
                headerHolder.imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_18dp);
            }
            headerHolder.ivId.setImageResource(ivId);
            headerHolder.tvTitle.setText(title);
            headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView ivId;
        private final TextView tvTitle;
        private final ImageView imgArrow;

        public HeaderViewHolder(View view) {
            super(view);
            rootView = view;
            ivId = (ImageView) view.findViewById(R.id.iv_id);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            imgArrow = (ImageView) view.findViewById(R.id.imgArrow);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvExplain;
        private final LinearLayout llTime;
        private final TextView tvTime;
        private final TextView tvContent;

        public ItemViewHolder(View view) {
            super(view);
            rootView = view;
            tvExplain = (TextView) rootView.findViewById(R.id.tv_task_detail_explain);
            llTime = (LinearLayout) rootView.findViewById(R.id.ll_task_detail_time);
            tvTime = (TextView) rootView.findViewById(R.id.tv_task_detail_time);
            tvContent = (TextView) rootView.findViewById(R.id.tv_task_detail_content);
        }
    }

    class ImeiSection extends StatelessSection {

        private String mImei;

        public ImeiSection(String imei) {
            super(R.layout.detail_imei);
            mImei = imei;
        }

        @Override
        public int getContentItemsTotal() {
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ImeiItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImeiItemViewHolder imeiItemViewHolder = (ImeiItemViewHolder) holder;
            imeiItemViewHolder.tvImei.setText("IMEI:" + mImei);
        }
    }

    class ImeiItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvImei;

        public ImeiItemViewHolder(View view) {
            super(view);
            rootView = view;
            tvImei = (TextView) rootView.findViewById(R.id.tv_imei);
        }
    }

    private String createDownFile(String typeName) throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "PDF_" + timeStamp + "_";
//        String imageFileName = mTaskListBean.getApplyCD()+typeName;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File file=new File(storageDir,imageFileName+".pdf");
////        File image = new File(
////                imageFileName,  /* prefix */
////                ".pdf",         /* suffix */
////                storageDir      /* directory */
////        );
//        Logger.d(file.getAbsolutePath());
        return storageDir.getAbsolutePath();
    }

    public Intent getPdfFileIntent(File pdfFile) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri pdfUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pdfUri = FileProvider.getUriForFile(mActivity, "com.cango.palmcartreasure.fileprovider", pdfFile);

        } else {
            pdfUri = Uri.fromFile(pdfFile);
        }
        intent.setDataAndType(pdfUri, "application/pdf");
        return intent;
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String error);
    }
}
