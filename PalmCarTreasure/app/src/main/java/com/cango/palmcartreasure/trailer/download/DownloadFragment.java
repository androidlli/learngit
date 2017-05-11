package com.cango.palmcartreasure.trailer.download;


import android.os.Build;
import android.os.Bundle;
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
import com.cango.palmcartreasure.trailer.message.MessageActivity;
import com.cango.palmcartreasure.trailer.message.MessageAdapter;
import com.cango.palmcartreasure.util.BarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DownloadFragment extends BaseFragment implements DownloadContract.View {

    @BindView(R.id.toolbar_download)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView_download)
    RecyclerView mRecyclerView;

    private DownloadActivity mActivity;
    private DownloadAdapter mAdapter;

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

        mAdapter = new DownloadAdapter(mActivity, getTestDatas(), false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<String>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, String data, int position) {
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
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

    public List<String> getTestDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(i + "");
        }
        return datas;
    }
}
