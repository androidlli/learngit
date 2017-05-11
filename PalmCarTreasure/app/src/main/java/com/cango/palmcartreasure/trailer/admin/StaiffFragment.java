package com.cango.palmcartreasure.trailer.admin;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.MtItemDecoration;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.Member;
import com.cango.palmcartreasure.util.SizeUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StaiffFragment extends BaseFragment implements StaiffContract.View {

    public static final String TYPE = "type";
    public static final String SHOW_GROUP = "show_group";
    public static final String PUT_TASKS_GROUP = "put_tasks_group";

    @BindView(R.id.toolbar_staiff)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_right)
    TextView tvRight;
    @BindView(R.id.recyclerView_staiff)
    RecyclerView mRecyclerView;

    @OnClick({R.id.tv_toolbar_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_toolbar_right:
                String text = tvRight.getText().toString();
                if (getString(R.string.new_group).equals(text)){
                    //添加分组 对应show_group
                    Intent addIntent=new Intent(getActivity(),GroupActivity.class);
                    addIntent.putExtra("type",GroupFragment.ADD);
                    mActivity.mSwipeBackHelper.forward(addIntent);
                }else if (getString(R.string.confirm).equals(text)){
                   //确定  针对于给他进行分配任务的
                }else {

                }
                break;
        }
    }

    private String mType;
    private StaiffActivity mActivity;
    private StaiffContract.Presenter mPresenter;
    private StaiffAdapter mAdapter;
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;

    public static StaiffFragment newInstance(String type) {
        StaiffFragment fragment = new StaiffFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_staiff;
    }

    @Override
    protected void initView() {
        mActivity = (StaiffActivity) getActivity();
//        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
//        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
//        if (Build.VERSION.SDK_INT >= 21) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
//            mToolbar.setLayoutParams(layoutParams);
//            mToolbar.setPadding(0, statusBarHeight, 0, 0);
//        }
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.admin_return);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (mType.equals(SHOW_GROUP)){
            tvRight.setText(getString(R.string.new_group));
        }else if (mType.equals(PUT_TASKS_GROUP)){
            tvRight.setText(getString(R.string.confirm));
        }else {
        }

        mAdapter = new StaiffAdapter(mActivity, null, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<GroupList.DataBean.GroupListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, GroupList.DataBean.GroupListBean data, int position) {
                //确定    对应put_tasks_group
                Member memberLeader=new Member();
                memberLeader.setId(data.getGroupLeaderID());
                memberLeader.setName(data.getGroupLeader());
                memberLeader.setGroupLeader(true);
                memberLeader.setSelected(true);
                List<Member> currentMembers=new ArrayList<>();
                for (GroupList.DataBean.GroupListBean.UserListBean bean:data.getUserList()) {
                    if (memberLeader.getId()==bean.getUserid()){

                    }else {
                        Member member=new Member();
                        member.setId(bean.getUserid());
                        member.setName(bean.getUserName());
                        member.setGroupLeader(false);
                        member.setSelected(true);
                        currentMembers.add(member);
                    }
                }

                Intent updateIntent=new Intent(getActivity(),GroupActivity.class);
                updateIntent.putExtra("type",GroupFragment.UPDATE);
                updateIntent.putExtra("groupName",data.getGroupName());
                updateIntent.putExtra("memberLeader",memberLeader);
                Logger.d(memberLeader.getName());
                updateIntent.putParcelableArrayListExtra("currentMembers", (ArrayList<? extends Parcelable>) currentMembers);
                mActivity.mSwipeBackHelper.forward(updateIntent);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new MtItemDecoration(2, SizeUtil.dp2px(mActivity, 12), true));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.start();
        mPresenter.loadStaiff("", false, 0, 0);
    }

    @Override
    protected void initData() {
        mType = getArguments().getString(TYPE);
    }

    @Override
    public void setPresenter(StaiffContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStaiffIndicator(boolean active) {

    }

    @Override
    public void showStaiffError() {
        if (isLoadMore) {

        } else {
        }
        mAdapter.setLoadFailedView(R.layout.load_failed_layout);
    }

    @Override
    public void showStaiff(List<GroupList.DataBean.GroupListBean> staiffs) {
        mAdapter.setNewData(staiffs);
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showNoStaiff() {
        if (isLoadMore) {

        } else {
        }
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showStaiffDetailUi(int staiffId) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
