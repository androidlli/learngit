package com.cango.palmcartreasure.trailer.taskdetail;


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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TaskDetailFragment extends BaseFragment implements TaskDetailContract.View {
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final String TYPE = "type";
    public static final String TASKLISTBEAN = "taskListBean";
    @BindView(R.id.toolbar_detail)
    Toolbar mToolbar;
    @BindView(R.id.tv_applyno)
    TextView tvApplyNo;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.recyclerView_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
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
                onRefresh(0);
                break;
            case R.id.ll_home_visit:
                onRefresh(1);
                break;
            case R.id.ll_case_info:
                onRefresh(2);
                break;
            case R.id.ll_customer_info:
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
        tvApplyNo.setText(mTaskListBean.getApplyCD() + "(" + mTaskListBean.getCustomerName() + ")");
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity = (TaskDetailActivity) getActivity();
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

        final ImageView fabIconNew = new ImageView(mActivity);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(mActivity)
                .setContentView(fabIconNew)
                .build();
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
                    if (mTaskListBean.getIsStart().equals("T"))
                        showCalendarDialog();
                    else {
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
                trailNavIntent.putExtra(TrailerMapFragment.TASKLISTBEAN,mTaskListBean);
                mActivity.mSwipeBackHelper.forward(trailNavIntent);
            }
        });
        subActionButton3 = rLSubBuilder.setContentView(rlIcon3).setLayoutParams(subParams).build();
        subActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommUtil.checkIsNull(mTaskListBean)) {
                    if (mTaskListBean.getIsCheckPoint().equals("T"))
                        openCamera();
                    else {
                        ToastUtils.showLong("请先进行开始任务！");
                    }
                }
            }
        });
        subActionButton4 = rLSubBuilder.setContentView(rlIcon4).setLayoutParams(subParams).build();
        subActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommUtil.checkIsNull(mTaskListBean)) {
                    if (mTaskListBean.getIsDone().equals("T")){
                        Intent sendCarLibraryIntent = new Intent(mActivity, TrailerMapActivity.class);
                        sendCarLibraryIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.SEND_CAR_LIBRARY);
                        sendCarLibraryIntent.putExtra(TrailerMapFragment.TASKLISTBEAN,mTaskListBean);
                        mActivity.mSwipeBackHelper.forward(sendCarLibraryIntent);
                    }
                    else {
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
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });

        downloadPW = getPopupWindow(getActivity(), R.layout.task_download);

        mPresenter.start();
        mPresenter.loadTaskDetail(mType, true, mTaskListBean.getAgencyID(), mTaskListBean.getCaseID());
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE);
            mTaskListBean = getArguments().getParcelable(TASKLISTBEAN);
        }
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
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.removeAllSections();
//        mAdapter.addSection(new ExpandableContactsSection())
        List<TaskDetailData.TaskSection> taskSectionList = taskDetailData.getTaskSectionList();
        if (mType != 2) {
            for (TaskDetailData.TaskSection section : taskSectionList) {
                mAdapter.addSection(new ExpandableContactsSection(section.getIvId(), section.getTitle(), section.getTaskInfoList()));
            }
        } else {
            //案件信息加入imei
            int size1 = taskSectionList.get(0).getTaskInfoList().size();
            TaskDetailData.TaskSection.TaskInfo remove1 = taskSectionList.get(0).getTaskInfoList().remove(size1 - 2);
            int size2 = taskSectionList.get(0).getTaskInfoList().size();
            TaskDetailData.TaskSection.TaskInfo remove2 = taskSectionList.get(0).getTaskInfoList().remove(size2 - 1);
            for (int i = 0; i < taskSectionList.size(); i++) {
                mAdapter.addSection(new ExpandableContactsSection(taskSectionList.get(i).getIvId(),
                        taskSectionList.get(i).getTitle(), taskSectionList.get(i).getTaskInfoList()));
            }
            mAdapter.addSection(new ImeiSection(remove1.getRight()));
            mAdapter.addSection(new ImeiSection(remove2.getRight()));
        }
        mAdapter.notifyDataSetChanged();
    }

    public void onRefresh(int type) {
        mType = type;
        mPresenter.loadTaskDetail(mType, true, mTaskListBean.getAgencyID(), mTaskListBean.getCaseID());
    }

    @Override
    public void showNoTaskDetail() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        llSorry.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {
            mCalendarDialog = new CalendarDialogFragment();
            mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date) {
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
                    objectMap.put("LAT", MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT));
                    objectMap.put("LON", MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON));
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

    private void showDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {
            mDotDialog = new DotTrailerDialogFragment();
            mDotDialog.setmListener(new DotTrailerDialogFragment.OnStatusListener() {
                @Override
                public void onStatusClick(int type) {
                    if (isAdded()) {
                        mLoadView.smoothToShow();
                    }
                    String notifyCust = type == 0 ? "1" : "0";
                    RequestBody userId = RequestBody.create(null, MtApplication.mSPUtils.getInt(Api.USERID) + "");
                    RequestBody LAT = RequestBody.create(null, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT) + "");
                    RequestBody LON = RequestBody.create(null, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON) + "");
                    RequestBody agencyID = RequestBody.create(null, mTaskListBean.getAgencyID() + "");
                    RequestBody caseID = RequestBody.create(null, mTaskListBean.getCaseID() + "");
                    RequestBody notifyCustImm = RequestBody.create(null, notifyCust);
                    File file = new File(mCurrentPhotoPath);
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), photoBody);
                    mService.checkPiontSubmit(userId, LAT, LON, agencyID, caseID, notifyCustImm, part)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new RxSubscriber<TaskAbandon>() {
                                @Override
                                protected void _onNext(TaskAbandon o) {
                                    if (isAdded()) {
                                        mLoadView.smoothToHide();
                                    }
                                    //拖车大点是否成功，如果成功的话，从新刷新vp
                                    int code = o.getCode();
                                    if (!CommUtil.checkIsNull(o.getMsg())){
                                        ToastUtils.showLong(o.getMsg());
                                    }
                                    if (code==0){
                                        mTaskListBean.setIsDone("T");
                                    }else {

                                    }
                                }

                                @Override
                                protected void _onError() {
                                    if (isAdded()) {
                                        mLoadView.smoothToHide();
                                    }
                                }
                            });
                }
            });
        }
        if (mDotDialog.isVisible()) {

        } else {
            mDotDialog.show(getFragmentManager(), "DotDialog");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            showDotDialog();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            deleteImageFile();
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

        public HeaderViewHolder(View view) {
            super(view);
            rootView = view;
            ivId = (ImageView) view.findViewById(R.id.iv_id);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
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
}
