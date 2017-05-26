package com.cango.palmcartreasure.trailer.download;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DownloadFragment extends BaseFragment implements DownloadContract.View, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_STORAGE_GROUP = 122;
    @BindView(R.id.toolbar_download)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView_download)
    RecyclerView mRecyclerView;

    private DownloadActivity mActivity;
    private DownloadAdapter mAdapter;
    private List<File> fileList = new ArrayList<>();

    public static DownloadFragment newInstance() {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_download;
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
        mActivity = (DownloadActivity) getActivity();
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
        mAdapter = new DownloadAdapter(mActivity, fileList, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<File>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, File data, int position) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    uri = FileProvider.getUriForFile(mActivity, "com.cango.palmcartreasure.fileprovider", data);
//                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                    for (ResolveInfo resolveInfo : resInfoList) {
//                        String packageName = resolveInfo.activityInfo.packageName;
//                        mContext.grantUriPermission(packageName, pdfUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    }
                } else {
                    uri=Uri.fromFile(data);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/pdf");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        openPermissions();
//        RxGetFiles();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(DownloadContract.Presenter presenter) {

    }

    @Override
    public void showDatasIndicator(boolean active) {

    }

    @Override
    public void showDatasError() {

    }

    @Override
    public void showDatas(List<String> datas) {

    }

    @Override
    public void showNoDatas() {

    }

    @Override
    public void showDataDetailUi(int id) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public List<File> getFiles() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        return FileUtils.listFilesInDirWithFilter(storageDir, ".pdf", false);
        String suffix=".pdf";
        if (storageDir == null || !FileUtils.isDir(storageDir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = storageDir.listFiles();
        Arrays.sort(files, new CompratorByLastModified());
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    private void RxGetFiles(){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (storageDir.isDirectory()){
            Observable.from(storageDir.listFiles())
                    .filter(new Func1<File, Boolean>() {
                        @Override
                        public Boolean call(File file) {
                            return file.getName().endsWith(".pdf");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            fileList.add(file);
                        }
                    });
        }
    }

    //根据文件修改时间进行比较的内部类
    static class CompratorByLastModified implements Comparator<File> {

        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            List<File> files = getFiles();
            if (files != null)
                fileList.addAll(files);
            mAdapter.notifyDataSetChanged();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        List<File> files = getFiles();
        if (files != null)
            fileList.addAll(files);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_group_and_storage)
                    .build().show();
        }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STORAGE_GROUP) {
            openPermissions();
        }
    }
}
