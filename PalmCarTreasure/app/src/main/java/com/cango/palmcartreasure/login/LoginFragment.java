package com.cango.palmcartreasure.login;


import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.register.RegisterActivity;
import com.cango.palmcartreasure.trailer.admin.AdminActivity;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.PhoneUtils;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.message.PushAgent;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_READ_PHONE_STATE_AND_LOCATION = 100;
    @BindView(R.id.toolbar_login)
    Toolbar mToolbar;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mIndicatorView;
    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;

    @OnClick({R.id.btn_login_enter, R.id.btn_login_register})
    public void loginOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_enter:
                isDoLogin = true;
                openPermissions();
                break;
            case R.id.btn_login_register:
//                mActivity.mSwipeBackHelper.forwardAndFinish(RegisterActivity.class);
                break;
            default:
                break;
        }
    }

    private LoginContract.Presenter mPresenter;
    private LoginActivity mActivity;
    private boolean isDoLogin;
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private boolean isFromLogout;
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
                        mLat = aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
//                        BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
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
                    Logger.d("errorCode = " + aMapLocation.getErrorCode() + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public static LoginFragment newInstance(boolean isFromLogout) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFromLogout", isFromLogout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_login;
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
        openPermissions();
    }

    @Override
    protected void initData() {
        mActivity = (LoginActivity) getActivity();
        if (!CommUtil.checkIsNull(getArguments())) {
            isFromLogout = getArguments().getBoolean("isFromLogout", false);
            Logger.d(isFromLogout);
        }
        initLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFromLogout) {
            MtApplication.clearExceptLastActivitys();
        }
    }

    private void doLogin() {
        if (mLat > 0 && mLon > 0) {
            String imei = PhoneUtils.getIMEI(getActivity());
            String registrationId = null;
            PushAgent mPushAgent = PushAgent.getInstance(mActivity.getApplicationContext());
            if (mPushAgent!=null){
                registrationId = mPushAgent.getRegistrationId();
            }
            mPresenter.login(etUserName.getText().toString(), etPassword.getText().toString(),
                    imei, mLat, mLon, registrationId, Api.DEVICE_TYPE);
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoginIndicator(boolean active) {
        if (active)
            mIndicatorView.smoothToShow();
        else
            mIndicatorView.smoothToHide();
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void showLoginSuccess(boolean isSuccess, String message) {
        if (!CommUtil.checkIsNull(message))
            ToastUtils.showShort(message);
        if (isSuccess) {
            openOtherUi();
        } else {

        }
    }

    @Override
    public void openOtherUi() {
        showLoginIndicator(false);
        if (MtApplication.mSPUtils.getInt(Api.USERROLEID) == Api.ADMIN_CODE) {
            mActivity.mSwipeBackHelper.forwardAndFinish(AdminActivity.class);
        } else {
            mActivity.mSwipeBackHelper.forwardAndFinish(TrailerActivity.class);
        }
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

    @AfterPermissionGranted(REQUEST_READ_PHONE_STATE_AND_LOCATION)
    private void openPermissions() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mLocationClient.startLocation();
            if (isDoLogin) {
                isDoLogin = false;
                doLogin();
            } else {

            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_phone_state), REQUEST_READ_PHONE_STATE_AND_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d("onPermissionsGranted");
        mLocationClient.startLocation();
        if (isDoLogin) {
            isDoLogin = false;
            doLogin();
        } else {

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.d("onPermissionsDenied");
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_READ_PHONE_STATE_AND_LOCATION) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_READ_PHONE_STATE_AND_LOCATION)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_read_phone_state)
                    .build().show();
        }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult");
        if (requestCode == REQUEST_READ_PHONE_STATE_AND_LOCATION) {
            // Do something after user returned from app settings screen, like showing a Toast.
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
