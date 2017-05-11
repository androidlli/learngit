package com.cango.palmcartreasure.trailer.message;


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
import com.cango.palmcartreasure.util.BarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageFragment extends BaseFragment implements MessageContract.View {

    @BindView(R.id.toolbar_message)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView_message)
    RecyclerView mRecyclerView;

    private MessageActivity mActivity;
    private MessageAdapter mAdapter;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_message;
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
        mActivity = (MessageActivity) getActivity();
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

        mAdapter = new MessageAdapter(mActivity, getTestDatas(), false);
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
    public void setPresenter(MessageContract.Presenter presenter) {

    }

    @Override
    public void showMessagesIndicator(boolean active) {

    }

    @Override
    public void showMessagesError() {

    }

    @Override
    public void showMessages(List<String> messages) {

    }

    @Override
    public void showNoMessages() {

    }

    @Override
    public void showMessageDetailUi(int id) {

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
