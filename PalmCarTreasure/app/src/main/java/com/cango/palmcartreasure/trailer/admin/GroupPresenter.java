package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.AdminService;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.Member;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/5/11.
 */

public class GroupPresenter implements GroupContract.Presenter {
    private GroupContract.View mView;
    private AdminService mService;

    public GroupPresenter(GroupContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(AdminService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadMembers(String type, boolean showRefreshLoadingUI, int pageCount, int pageSize) {
        mService.getGroupList(MtApplication.mSPUtils.getInt(Api.USERID), "F")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupList>() {
                    @Override
                    protected void _onNext(GroupList o) {
                        if (mView.isActive()) {
                            int code = o.getCode();
                            if (code == 0) {
                                List<GroupList.DataBean.GroupListBean.UserListBean> userList = o.getData().getGroupList().get(0).getUserList();
                                List<Member> currentMembers = new ArrayList<>();
                                for (GroupList.DataBean.GroupListBean.UserListBean bean : userList) {
                                    Member member = new Member();
                                    member.setId(bean.getUserid());
                                    member.setName(bean.getUserName());
                                    member.setGroupLeader(false);
                                    member.setSelected(false);
                                    currentMembers.add(member);
                                }
                                mView.showMembers(currentMembers);
                            } else if (code == -1) {
                                mView.showMembersError();
                            } else {

                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive())
                            mView.showMembersError();
                    }
                });
    }
}
