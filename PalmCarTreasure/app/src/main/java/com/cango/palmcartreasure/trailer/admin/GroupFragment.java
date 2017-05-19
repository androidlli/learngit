package com.cango.palmcartreasure.trailer.admin;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.MemberItemDecoration;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.Member;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupFragment extends BaseFragment implements GroupContract.View, GroupAdapter.OnSelectCountListener {

    public static final String ADD = "add";
    public static final String UPDATE = "update";

    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar_group)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_drawer_line)
    ImageView ivLine;
    @BindView(R.id.recyclerView_member)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_group_name)
    EditText etGroupName;
    @BindView(R.id.tv_open_group_leader)
    TextView tvLeader;
    @BindView(R.id.tv_member1)
    TextView tvMember1;
    @BindView(R.id.tv_member2)
    TextView tvMember2;
    @BindView(R.id.tv_member3)
    TextView tvMember3;
    @BindView(R.id.tv_member4)
    TextView tvMember4;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    @OnClick({R.id.iv_open_member_list, R.id.tv_confirm, R.id.tv_open_group_leader, R.id.tv_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_open_member_list:
                mAdapter.setType(1);
                notifyCurrentDatas();
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.tv_open_group_leader:
                mAdapter.setType(0);
                //因为如果在侧拉界面把组长选中点掉，那么组长下标是-1，那么我这里就该把状态true
                int leaderPosition = getGroupLeaderPosition();
                if (leaderPosition == -1) {
                    if (memberLeader != null) {
                        memberLeader.setGroupLeader(true);
                    }
                } else {
                    if (datas.get(leaderPosition) == memberLeader) {
                    } else {
                        datas.get(leaderPosition).setGroupLeader(false);
                    }
                    memberLeader.setGroupLeader(true);
                }

                if (datas.contains(memberLeader) && datas.contains(currentMembers)) {

                } else {
                    if (!datas.contains(memberLeader)) {
                        if (memberLeader.getId() > 0) {
                            //防止新建分组的时候member是null
                            datas.add(memberLeader);
                        }
                    }
                    if (!datas.containsAll(currentMembers)) {
                        if (currentMembers.size() == 0) {

                        } else {
                            datas.addAll(currentMembers);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.tv_confirm:
                if (mAdapter.getType() == 0) {
                    int position = getGroupLeaderPosition();
                    if (position == -1) {

                    } else {
                        memberLeader = datas.get(position);
                        //防止选择的组长是这个组的组员那么就把这个组的组员去掉
                        int memberLeaderId = memberLeader.getId();
                        if (currentMembers != null) {
                            int deleteGroupPosition = -1;
                            for (int i = 0; i < currentMembers.size(); i++) {
                                if (currentMembers.get(i).getId() == memberLeaderId) {
                                    deleteGroupPosition = i;
                                    break;
                                }
                            }
                            if (deleteGroupPosition != -1) {
                                currentMembers.remove(deleteGroupPosition);
                                onSelectCount(currentMembers, currentMembers.size());
                            }
                        }
                        tvLeader.setText(memberLeader.getName());
                    }
                    mDrawerLayout.closeDrawers();
                } else {
                    checkSelectCount();
                }
                break;
            case R.id.tv_ensure:
                confirmGroupMDF();
                break;
        }
    }

    private String mType;
    private List<Member> datas = new ArrayList<>();
    private int groupId;
    /**
     * 当前的编辑的组名
     */
    private String groupName;
    /**
     * 当前选中的组员
     */
    private List<Member> currentMembers = new ArrayList<>();
    /**
     * 当前的选中的组长
     */
    private Member memberLeader = new Member();
    private GroupActivity mActivity;
    private GroupContract.Presenter mPresenter;
    private GroupAdapter mAdapter;

    public static GroupFragment newInstance(String type) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public static GroupFragment newInstance(String type, int groupId, String groupName, Member memberLeader, List<Member> currentMembers) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putInt("groupId", groupId);
        args.putString("groupName", groupName);
        args.putParcelable("memberLeader", memberLeader);
        args.putParcelableArrayList("currentMembers", (ArrayList<? extends Parcelable>) currentMembers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initView() {

        if (ADD.equals(mType)) {
            tvTitle.setText(getString(R.string.new_group));
        } else if (UPDATE.equals(mType)) {
            tvTitle.setText(R.string.update_group);
        } else {

        }

        mActivity = (GroupActivity) getActivity();
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

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        datas = getDatas();
        mAdapter = new GroupAdapter(mActivity, datas, false);
        mAdapter.setOnSelectCountListener(this);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<Member>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, Member data, int position) {
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new MemberItemDecoration(3, SizeUtil.dp2px(mActivity, 8), true));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mPresenter.start();
        mPresenter.loadMembers(mType, false, 0, 0);
    }

    @Override
    protected void initData() {
        mType = getArguments().getString("type");
        if (ADD.equals(mType)) {

        } else if (UPDATE.equals(mType)) {
            groupId = getArguments().getInt("groupId");
            groupName = getArguments().getString("groupName");
            memberLeader = getArguments().getParcelable("memberLeader");
            currentMembers = getArguments().getParcelableArrayList("currentMembers");
        } else {

        }
    }

    @Override
    public void onSelectCount(List<Member> members, int selectSize) {
        currentMembers = members;
        mDrawerLayout.closeDrawers();
        if (selectSize == 0) {
            tvMember1.setVisibility(View.INVISIBLE);
            tvMember2.setVisibility(View.INVISIBLE);
            tvMember3.setVisibility(View.INVISIBLE);
            tvMember4.setVisibility(View.INVISIBLE);
        } else if (selectSize == 1) {
            tvMember1.setText(members.get(0).getName());
            tvMember1.setVisibility(View.VISIBLE);
            tvMember2.setVisibility(View.INVISIBLE);
            tvMember3.setVisibility(View.INVISIBLE);
            tvMember4.setVisibility(View.INVISIBLE);
        } else if (selectSize == 2) {
            tvMember1.setText(members.get(0).getName());
            tvMember2.setText(members.get(1).getName());
            tvMember1.setVisibility(View.VISIBLE);
            tvMember2.setVisibility(View.VISIBLE);
            tvMember3.setVisibility(View.INVISIBLE);
            tvMember4.setVisibility(View.INVISIBLE);
        } else if (selectSize == 3) {
            tvMember1.setText(members.get(0).getName());
            tvMember2.setText(members.get(1).getName());
            tvMember3.setText(members.get(2).getName());
            tvMember1.setVisibility(View.VISIBLE);
            tvMember2.setVisibility(View.VISIBLE);
            tvMember3.setVisibility(View.VISIBLE);
            tvMember4.setVisibility(View.INVISIBLE);
        } else {
            tvMember1.setText(members.get(0).getName());
            tvMember2.setText(members.get(1).getName());
            tvMember3.setText(members.get(2).getName());
            tvMember4.setText(members.get(3).getName());
            tvMember1.setVisibility(View.VISIBLE);
            tvMember2.setVisibility(View.VISIBLE);
            tvMember3.setVisibility(View.VISIBLE);
            tvMember4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(GroupContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMemberIndicator(boolean active) {
        if (active)
            mLoadView.show();
        else
            mLoadView.hide();
    }

    @Override
    public void showMembersError() {
        mAdapter.setLoadFailedView(R.layout.load_failed_layout);
    }

    @Override
    public void showMembers(List<Member> members) {
        if (mType.equals(UPDATE)) {
//            members.add(memberLeader);
//            members.addAll(currentMembers);
            etGroupName.setText(groupName);
            etGroupName.setSelection(groupName.length());
            tvLeader.setText(memberLeader.getName());
            onSelectCount(currentMembers, currentMembers.size());
        }
        mAdapter.setNewData(members);
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showNoMembers() {
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showMemberDetailUi(int staiffId) {

    }

    @Override
    public void showGroupMDFResult(boolean isSuccess, String message) {
        if (!CommUtil.checkIsNull(message))
            ToastUtils.showShort(message);
        if (isSuccess){
            mActivity.setResult(Activity.RESULT_OK);
            mActivity.mSwipeBackHelper.swipeBackward();
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 选择组员的时候把组长数据删除
     */
    private void notifyCurrentDatas() {
        if (currentMembers != null && currentMembers.size() > 0) {
            if (datas.containsAll(currentMembers)) {

            } else {
                datas.addAll(currentMembers);
            }
        } else {

        }
        if (datas.contains(memberLeader)) {
            datas.remove(memberLeader);
        }
        for (Member bean : datas) {
            bean.setSelected(false);
        }
        if (currentMembers != null) {
            for (Member bean : currentMembers) {
                bean.setSelected(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 针对于组员管理
     */
    private void checkSelectCount() {
        int selectedSize = 0;
        for (Member member : datas) {
            if (member.isSelected()) {
                if (member.isGroupLeader()) {

                } else {
                    selectedSize++;
                }
            }
        }
//        if (selectedSize > 0) {
        List<Member> members = new ArrayList<>();
        for (Member member : datas) {
            if (member.isSelected()) {
                if (member.isGroupLeader()) {

                } else {
                    members.add(member);
                }
            }
        }
        onSelectCount(members, selectedSize);
//        }
    }

    private int getGroupLeaderPosition() {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isGroupLeader()) {
                return i;
            }
        }
        return -1;
    }

    private void confirmGroupMDF() {
        String groupName = etGroupName.getText().toString().trim();
        if (CommUtil.checkIsNull(groupName) || CommUtil.checkIsNull(memberLeader) || CommUtil.checkIsNull(currentMembers)) {
            ToastUtils.showLong(R.string.please_input_all_message);
        } else {
            if (currentMembers.size() > 0) {
                List<GroupList.DataBean.GroupListBean> groupListBeanList = new ArrayList<>();
                GroupList.DataBean.GroupListBean groupListBean = new GroupList.DataBean.GroupListBean();
                List<GroupList.DataBean.GroupListBean.UserListBean> userListBeanList = new ArrayList<>();
                GroupList.DataBean.GroupListBean.UserListBean userListBean;
                if (ADD.equals(mType)) {
                    groupListBean.setAction("30");
                } else if (UPDATE.equals(mType)) {
                    groupListBean.setAction("10");
                } else {
                }
                groupListBean.setGroupid(groupId);
                groupListBean.setGroupName(groupName);
                groupListBean.setGroupLeaderID(memberLeader.getId());
                //把组长也添加到组员列表里面
                userListBean = new GroupList.DataBean.GroupListBean.UserListBean();
                userListBean.setUserid(memberLeader.getId());
                userListBean.setGroupid(groupId);
                userListBeanList.add(userListBean);
                //添加组员
                for (int i = 0; i < currentMembers.size(); i++) {
                    userListBean = new GroupList.DataBean.GroupListBean.UserListBean();
                    userListBean.setUserid(currentMembers.get(i).getId());
                    userListBean.setGroupid(groupId);
                    userListBeanList.add(userListBean);
                }
                groupListBean.setUserList(userListBeanList);
                groupListBeanList.add(groupListBean);

                mPresenter.groupMDF(true, groupListBeanList);
            }
        }
    }

}
