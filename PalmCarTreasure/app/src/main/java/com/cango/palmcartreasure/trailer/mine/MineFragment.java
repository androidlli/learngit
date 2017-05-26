package com.cango.palmcartreasure.trailer.mine;


import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.login.LoginActivity;
import com.cango.palmcartreasure.model.MessageEvent;
import com.cango.palmcartreasure.model.PersonMain;
import com.cango.palmcartreasure.trailer.download.DownloadActivity;
import com.cango.palmcartreasure.trailer.message.MessageActivity;
import com.cango.palmcartreasure.trailer.personal.PersonalActivity;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MineFragment extends BaseFragment implements MineContract.View, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 121;
    @BindView(R.id.toolbar_mine)
    Toolbar mToolbar;
    @BindView(R.id.tv_mine_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_alltaskcount)
    TextView tvALLTaskCount;
    @BindView(R.id.tv_undonetaskcount)
    TextView tvUnDoneTaskCount;
    @BindView(R.id.tv_donetaskcount)
    TextView tvDoneTaskCount;
    @BindView(R.id.rl_message)
    RelativeLayout rlMessage;
    @BindView(R.id.iv_message_right)
    ImageView ivMsgRight;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    private Badge badge;

    @OnClick({R.id.rl_personal_data, R.id.rl_message, R.id.rl_clear_cache, R.id.btn_exit, R.id.ll_tasks,R.id.rl_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_personal_data:
                mActivity.mSwipeBackHelper.forward(PersonalActivity.class);
                break;
            case R.id.rl_message:
                mActivity.mSwipeBackHelper.forward(MessageActivity.class);
                break;
            case R.id.rl_clear_cache:
                //test

                break;
            case R.id.rl_download:
                mActivity.mSwipeBackHelper.forward(DownloadActivity.class);
                break;
            case R.id.btn_exit:
                isDoLogout = true;
                openPermissions();
                break;
            case R.id.ll_tasks:
                mActivity.mSwipeBackHelper.swipeBackward();
                break;
        }
    }

    private MineActivity mActivity;
    private MineContract.Presenter mPresenter;
    //定位相关
    private AMapLocationClient mLocationClient;
    private boolean isDoLogout;
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

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        ivMine.setImageResource(R.drawable.my_on);
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
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mPresenter.loadMineData(true);
        openPermissions();
    }

    @Override
    protected void initData() {
        mActivity = (MineActivity) getActivity();
        initLocation();
    }

    //message已经读取的话如果回到界面需要刷新的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        Logger.d("onMessageEvent");
        mPresenter.loadMineData(true);
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

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMineDataIndicator(boolean active) {
        if (active)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showMineDataError() {

    }

    @Override
    public void showMineData(PersonMain.DataBean dataBean) {
        if (!CommUtil.checkIsNull(dataBean)) {
            tvNickName.setText(dataBean.getNickName());
            tvALLTaskCount.setText(dataBean.getAllTaskCount() + "");
            tvUnDoneTaskCount.setText(dataBean.getUndoneTaskCount() + "");
            tvDoneTaskCount.setText(dataBean.getDoneTaskCount() + "");
            if (badge!=null)
                badge.hide(true);
            badge = null;
            if (dataBean.getUnreadMessage() == 0) {
                ivMsgRight.setVisibility(View.VISIBLE);
            } else {
                ivMsgRight.setVisibility(View.GONE);
                badge = new QBadgeView(getActivity())
                        .bindTarget(rlMessage)
                        .setShowShadow(false)
                        .setBadgeNumber(dataBean.getUnreadMessage())
                        .setBadgeGravity(Gravity.CENTER | Gravity.END);
            }
        }
    }

    @Override
    public void showLogoutMessage(boolean isSuccess, String message) {
        ToastUtils.showShort(message);
        if (isSuccess) {
            Intent loginIntent = new Intent(mActivity, LoginActivity.class);
            loginIntent.putExtra("isFromLogout", true);
            mActivity.mSwipeBackHelper.forward(loginIntent);
        } else {

        }
    }

    @Override
    public void showNoMineData() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void doLogout() {
        if (mLat > 0 && mLon > 0) {
            mPresenter.logoutTest(true, mLat, mLon);
        } else {
            ToastUtils.showShort(R.string.no_get_location);
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
            if (isDoLogout) {
                isDoLogout = false;
                doLogout();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mLocationClient.startLocation();
        if (isDoLogout) {
            isDoLogout = false;
            doLogout();
        } else {

        }
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
