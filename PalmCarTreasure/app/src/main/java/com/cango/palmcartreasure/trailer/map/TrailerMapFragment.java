package com.cango.palmcartreasure.trailer.map;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.customview.DotTrailerDialogFragment;
import com.cango.palmcartreasure.customview.UploadDialogFragment;
import com.cango.palmcartreasure.util.AppUtils;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class TrailerMapFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    public static final String TYPE = "type";
    public static final String TRAILER_NAV = "trailer_navigation";
    public static final String SEND_CAR_LIBRARY = "send_car_library";
    public static final String HISTORY = "history";
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 101;

    @BindView(R.id.toolbar_trailer_map)
    Toolbar mToolbar;
    @BindView(R.id.tv_map_title)
    TextView mTitle;
    @BindView(R.id.rl_map_top)
    RelativeLayout rlMapTop;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;
    @BindView(R.id.iv_map_location)
    ImageView ivLocation;
    @BindView(R.id.rl_map_bottom)
    RelativeLayout rlMapBottom;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_one_speed)
    TextView tvOne;
    @BindView(R.id.tv_one_point_five_speed)
    TextView tvOneFive;
    @BindView(R.id.tv_two_speed)
    TextView tvTwo;
    private LatLng carGPSLatLng;
    private float mLat, mLon;

    /**
     * 拖车导航还是拖车入库
     */
    private String mType;
    private UploadDialogFragment upLoadDialog;

    @OnClick({R.id.iv_map_nav, R.id.iv_map_location,R.id.tv_one_speed,R.id.tv_one_point_five_speed,R.id.tv_two_speed,
    R.id.btn_map_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_map_nav:
                rlShadow.setVisibility(View.VISIBLE);
                selectMapPW.showAtLocation(rlShadow, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.iv_map_location:
                mLocationClient.startLocation();
                if (mMap != null) {
                    setLocationBluePoint();
                }
                break;
            case R.id.tv_one_speed:
                tvOne.setBackgroundResource(R.drawable.trailer_map_run_speed_bg);
                tvOneFive.setBackgroundColor(Color.WHITE);
                tvTwo.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_one_point_five_speed:
                tvOne.setBackgroundColor(Color.WHITE);
                tvOneFive.setBackgroundResource(R.drawable.trailer_map_run_speed_bg);
                tvTwo.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_two_speed:
                tvOne.setBackgroundColor(Color.WHITE);
                tvOneFive.setBackgroundColor(Color.WHITE);
                tvTwo.setBackgroundResource(R.drawable.trailer_map_run_speed_bg);
                break;
            case R.id.btn_map_send:
                showUpLoadDialog();
                break;
            default:
                break;
        }
    }

    //地图相关
    private AMap mMap;
    //定位相关
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLoactionListener;
    private TrailerMapActivity mActivity;
    private PopupWindow selectMapPW;

    public static TrailerMapFragment newInstance(String type) {
        TrailerMapFragment fragment = new TrailerMapFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer_map;
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
        mActivity = (TrailerMapActivity) getActivity();
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
        if (TRAILER_NAV.equals(mType)) {
            mTitle.setText(R.string.traialer_navigation);
            rlMapTop.setVisibility(View.GONE);
            llDate.setVisibility(View.GONE);
        } else if (SEND_CAR_LIBRARY.equals(mType)) {
            mTitle.setText(R.string.send_car_library);
            rlMapTop.setVisibility(View.VISIBLE);
        } else {
            mTitle.setText(R.string.vehicle_localization);
            rlMapTop.setVisibility(View.GONE);
            rlMapBottom.setVisibility(View.GONE);
            ivLocation.setVisibility(View.GONE);
            llDate.setVisibility(View.VISIBLE);
        }
        openPermissions();
        selectMapPW = getPopupWindow(mActivity, R.layout.map_nav_bottom);
        carGPSLatLng = new LatLng(31.245203, 121.503447);
        BigDecimal latBD = new BigDecimal(String.valueOf(carGPSLatLng.latitude));
        mLat = latBD.floatValue();
        BigDecimal lonBD = new BigDecimal(String.valueOf(carGPSLatLng.longitude));
        mLon = lonBD.floatValue();
        setUpMapIfNeeded();
    }

    @Override
    protected void initData() {
        mType = getArguments().getString(TYPE);
        Logger.d(mType);
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
                        Logger.d(dateString + ": Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
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
//        mLocationClient.startLocation();
    }

    private void showUpLoadDialog() {
        if (CommUtil.checkIsNull(upLoadDialog)) {
            upLoadDialog = new UploadDialogFragment();
        }
        if (upLoadDialog.isVisible()) {

        } else {
            upLoadDialog.show(getFragmentManager(), "DotDialog");
        }
    }

    private void closeUpLoadDialog() {
        if (CommUtil.checkIsNull(upLoadDialog)) {

        } else {
            if (upLoadDialog.isVisible()) {
                upLoadDialog.dismiss();
            }
        }
    }

    /**
     * 获取Amap 对象
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fg_map);
            mMap = supportMapFragment.getMap();
            UiSettings uiSettings = mMap.getUiSettings();
            //设置不显示+-符号
            uiSettings.setZoomControlsEnabled(false);
//            setLocationBluePoint();
            setCarGPSLocationPoint();
        }
    }

    private void setCarGPSLocationPoint() {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(carGPSLatLng, 15, 0, 0)));
        mMap.clear();
        MarkerOptions carMarker = new MarkerOptions()
                .position(carGPSLatLng)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark));
        mMap.addMarker(carMarker);
    }

    private void setLocationBluePoint() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                .interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {

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

        }
    }

    public PopupWindow getPopupWindow(Context context, int layoutId) {
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        TextView tvBaiduMap = (TextView) popupView.findViewById(R.id.tv_baidu_map);
        TextView tvGaodeMap = (TextView) popupView.findViewById(R.id.tv_gaode_map);
        if (!AppUtils.isHasBaiduMap(context)) {
            tvBaiduMap.setVisibility(View.GONE);
        }
        if (!AppUtils.isHasGaodeMap(context)) {
            tvGaodeMap.setVisibility(View.GONE);
        }
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                rlShadow.setVisibility(View.GONE);
            }
        });
        tvBaiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBaiduMap(mLat, mLon);
            }
        });
        tvGaodeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGaodeMap(mLat, mLon);
            }
        });
        // TODO：更新popupwindow的状态
        popupWindow.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        return popupWindow;
    }

    private void startBaiduMap(float lat, float lon) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("baidumap://map/direction?origin=&destination=latlng:" + lat + "," + lon + "|name:车辆所在&mode=driving&sy=&index=&target=");
        intent.setData(uri);
        //启动该页面即可
        startActivity(intent);
    }

    private void startGaodeMap(float lat, float lon) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("androidamap://route?sourceApplication=cango&slat=&slon=&sname=&dlat=+" + lat + "&dlon=" + lon + "&dname=车辆所在&dev=0&t=1");
        intent.setData(uri);
        //启动该页面即可
        startActivity(intent);
    }
}
