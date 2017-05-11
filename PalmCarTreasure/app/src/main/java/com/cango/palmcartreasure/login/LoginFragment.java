package com.cango.palmcartreasure.login;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 */
public class LoginFragment extends BaseFragment implements LoginContract.View ,EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_READ_PHONE_STATE = 100;
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
                doLogin();
                break;
            case R.id.btn_login_register:
                mActivity.mSwipeBackHelper.forwardAndFinish(RegisterActivity.class);
                //test
//                if (Build.VERSION.SDK_INT >= 25) {
//                    ShortcutManager shortcutManager = getActivity().getSystemService(ShortcutManager.class);
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.setAction(Intent.ACTION_VIEW);
//                    ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(getActivity(), "id1")
//                            .setShortLabel(getString(R.string.shortcut_short_prompt))
//                            .setLongLabel(getString(R.string.shortcut_long_prompt))
//                            .setDisabledMessage(getString(R.string.shortcut_display_prompt))
//                            .setIcon(Icon.createWithResource(getActivity(), R.mipmap.ic_launcher_round))
//                            .setIntent(intent)
//                            .build();
//                    shortcutManager.setDynamicShortcuts(Arrays.asList(shortcutInfo));
//                    Toast.makeText(getActivity(), "setDynamicShortcuts ", Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
                break;
        }
    }

    private LoginContract.Presenter mPresenter;
    private LoginActivity mActivity;
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLoactionListener;
    private float mLat, mLon;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        mActivity= (LoginActivity) getActivity();
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    @Override
    protected void initData() {
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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
                    } else {
                        Logger.d("errorCode = " + aMapLocation.getErrorCode() + " errorInfo = " + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLoactionListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(5000);
        mLocationClient.setLocationOption(option);
        mLocationClient.startLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_READ_PHONE_STATE)
    private void doLogin() {
        String[] perms={Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            if (mLocationClient.isStarted()){
                AMapLocation lastKnownLocation = mLocationClient.getLastKnownLocation();
                BigDecimal latBD = new BigDecimal(String.valueOf(lastKnownLocation.getLatitude()));
                mLat = latBD.floatValue();
                BigDecimal lonBD = new BigDecimal(String.valueOf(lastKnownLocation.getLongitude()));
                mLon = lonBD.floatValue();
            }else {
                mLat=0f;
                mLon=0f;
            }
            String imei=PhoneUtils.getIMEI(getActivity());
            mPresenter.login(etUserName.getText().toString(), etPassword.getText().toString(),
                    imei,mLat,mLon,imei, Api.DEVICE_TYPE);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_phone_state), REQUEST_READ_PHONE_STATE, perms);
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoginIndicator(boolean active) {
        if (active)
            mIndicatorView.show();
        else
            mIndicatorView.hide();
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void showLoginSuccess() {

    }

    @Override
    public void openOtherUi() {
        showLoginIndicator(false);
        if (MtApplication.mSPUtils.getInt(Api.USERROLEID)==Api.ADMIN_CODE){
            mActivity.mSwipeBackHelper.forwardAndFinish(AdminActivity.class);
        }else {
            mActivity.mSwipeBackHelper.forwardAndFinish(TrailerActivity.class);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            if (requestCode==REQUEST_READ_PHONE_STATE) {
                new AppSettingsDialog.Builder(this)
                        .setRequestCode(REQUEST_READ_PHONE_STATE)
                        .setTitle("权限获取失败")
                        .setRationale(R.string.setting_read_phone_state)
                        .build().show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_READ_PHONE_STATE) {
            // Do something after user returned from app settings screen, like showing a Toast.
          doLogin();
        }
    }
}
