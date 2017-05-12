package com.cango.palmcartreasure.trailer.main;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.base.BaseLazyFragment;
import com.cango.palmcartreasure.customview.CalendarDialogFragment;
import com.cango.palmcartreasure.customview.DotTrailerDialogFragment;
import com.cango.palmcartreasure.trailer.map.TrailerMapActivity;
import com.cango.palmcartreasure.trailer.map.TrailerMapFragment;
import com.cango.palmcartreasure.trailer.mine.MineActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.orhanobut.logger.Logger;
import com.rd.Orientation;
import com.rd.PageIndicatorView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;

public class TrailerFragment extends BaseFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.toolbar_trailer)
    Toolbar mToolbarTrailer;
    @BindView(R.id.vp_trailer)
    ViewPager mViewPager;
    @BindView(R.id.ll_for_task)
    LinearLayout llForTask;
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

    @OnClick({R.id.iv_start_task, R.id.ll_tasks, R.id.ll_mine, R.id.ll_trailer_navigation, R.id.ll_dot_trailer, R.id.ll_send_car_library})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_start_task:
                showCalendarDialog();
                break;
            case R.id.ll_mine:
                mActivity.mSwipeBackHelper.forward(MineActivity.class);
                break;
            case R.id.ll_trailer_navigation:
                Intent trailNavIntent = new Intent(mActivity, TrailerMapActivity.class);
                trailNavIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.TRAILER_NAV);
                mActivity.mSwipeBackHelper.forward(trailNavIntent);
                break;
            case R.id.ll_dot_trailer:
                openCamera();
                break;
            case R.id.ll_send_car_library:
                Intent sendCarLibraryIntent = new Intent(mActivity, TrailerMapActivity.class);
                sendCarLibraryIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.SEND_CAR_LIBRARY);
                mActivity.mSwipeBackHelper.forward(sendCarLibraryIntent);
                break;
            default:
                break;
        }
    }

    private String mParam1;
    private String mParam2;
    private String mCurrentPhotoPath;
    private TrailerActivity mActivity;
    private List<BaseLazyFragment> mFragments;
    private MainPageAdapter mPageAdapter;
    private CalendarDialogFragment mCalendarDialog;
    private DotTrailerDialogFragment mDotDialog;

    public TrailerFragment() {
    }

    public static TrailerFragment newInstance() {
        TrailerFragment fragment = new TrailerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MtApplication.clearExceptLastActivitys();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        Logger.d("actionBarHeight = " + actionBarHeight);
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbarTrailer.setLayoutParams(layoutParams);
            mToolbarTrailer.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity = (TrailerActivity) getActivity();
        mPageAdapter = new MainPageAdapter(getChildFragmentManager());
        mPageAdapter.setData(mFragments);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size() - 1);
        mPageIndicatorView.setViewPager(mViewPager);
        mPageIndicatorView.setOrientation(Orientation.HORIZONTAL);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Logger.d(position);
                if (position % 2 == 0) {
                    llStartTask.setVisibility(View.GONE);
                } else {
                    llStartTask.setVisibility(View.VISIBLE);
                }
                ObjectAnimator.ofFloat(llCenter, "rotationX", 0.0F, 360F)
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new QBadgeView(getActivity())
                .bindTarget(llForTask)
                .setBadgeNumber(5)
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setGravityOffset(SizeUtil.px2dp(getContext(), 28), true);

        setOnClickListener();
    }

    private void setOnClickListener() {
        llForTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tasksIntent = new Intent(mActivity, TrailerTasksActivity.class);
                tasksIntent.putExtra("type", TrailerTasksFragment.INPROGRESS_TASK);
                mActivity.mSwipeBackHelper.forward(tasksIntent);
            }
        });
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        //得到banner数量和内容信息，生成n个banner fragment
        for (int i = 0; i < 5; i++) {
            mFragments.add(VpItemLazyFragment.newInstance(i + "", i + "name"));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("onActivityResult: requestCode = " + requestCode + "  resultCode = " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            showDotDialog();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            deleteImageFile();
        }
    }

    private void showDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {
            mDotDialog = new DotTrailerDialogFragment();
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

    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {
            mCalendarDialog = new CalendarDialogFragment();
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
                    Logger.d("photoURI: " + photoURI.toString());
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
}
