package com.cango.palmcartreasure.trailer.mine;


<<<<<<< HEAD
import android.Manifest;
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

<<<<<<< HEAD
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.login.LoginActivity;
<<<<<<< HEAD
import com.cango.palmcartreasure.model.PersonMain;
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
import com.cango.palmcartreasure.trailer.download.DownloadActivity;
import com.cango.palmcartreasure.trailer.message.MessageActivity;
import com.cango.palmcartreasure.trailer.personal.PersonalActivity;
import com.cango.palmcartreasure.util.BarUtil;
<<<<<<< HEAD
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
=======
import com.cango.palmcartreasure.util.ToastUtils;
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
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
import q.rorbin.badgeview.QBadgeView;

public class MineFragment extends BaseFragment implements MineContract.View ,EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 121;
    @BindView(R.id.toolbar_mine)
    Toolbar mToolbar;
<<<<<<< HEAD
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
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    @OnClick({R.id.rl_personal_data, R.id.rl_message, R.id.rl_clear_cache, R.id.btn_exit})
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
                mActivity.mSwipeBackHelper.forward(DownloadActivity.class);
                break;
            case R.id.btn_exit:
                //测试使用
//                mActivity.mSwipeBackHelper.forward(AdminActivity.class);
//                mPresenter.logout(true, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT), MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON));
<<<<<<< HEAD
                doLogout();
=======
                mPresenter.logoutTest(true,MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON));
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                break;
        }
    }

    //定位相关
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLoactionListener;
    private float mLat, mLon;
    private MineActivity mActivity;
    private MineContract.Presenter mPresenter;

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

<<<<<<< HEAD
        mPresenter.loadMineData(true);
=======
        mLoadView.hide();
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
    }

    @Override
    protected void initData() {
        mActivity = (MineActivity) getActivity();
        initLocation();
    }
    private void initLocation() {
        mLocationClient=new AMapLocationClient(mActivity.getApplicationContext());
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
                        if (!CommUtil.checkIsNull(aMapLocation.getLatitude())){
                            BigDecimal latBD = new BigDecimal(String.valueOf(aMapLocation.getLatitude()));
                            mLat = latBD.floatValue();
                        }
                        if (!CommUtil.checkIsNull(aMapLocation.getLongitude())){
                            BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
                            mLon = lonBD.floatValue();
                        }
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
        mLocationClient.startLocation();
        double latitude = mLocationClient.getLastKnownLocation().getLatitude();
        double longitude = mLocationClient.getLastKnownLocation().getLongitude();
        if (!CommUtil.checkIsNull(latitude)){
            BigDecimal latBD = new BigDecimal(String.valueOf(latitude));
            mLat = latBD.floatValue();
        }
        if (!CommUtil.checkIsNull(longitude)){
            BigDecimal lonBD = new BigDecimal(String.valueOf(longitude));
            mLon = lonBD.floatValue();
        }
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMineDataIndicator(boolean active) {
        if (active)
<<<<<<< HEAD
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
=======
            mLoadView.show();
        else
            mLoadView.hide();
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
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
            new QBadgeView(getActivity())
                    .bindTarget(rlMessage)
                    .setBadgeNumber(dataBean.getUnreadMessage())
                    .setBadgeGravity(Gravity.CENTER | Gravity.END);
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
    public void showLogoutMessage(boolean isSuccess, String message) {
        ToastUtils.showShort(message);
        if (isSuccess){
            Intent loginIntent=new Intent(mActivity,LoginActivity.class);
            loginIntent.putExtra("isFromLogout",true);
            mActivity.mSwipeBackHelper.forward(loginIntent);
        }else {

        }
    }

    @Override
    public void showNoMineData() {

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
    private void doLogout() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            if (mLat > 0 && mLon > 0) {
                mPresenter.logoutTest(true, mLat, mLon);
            }else {
                ToastUtils.showLong("位置获取失败！");
            }
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
            doLogout();
        }
    }
}
