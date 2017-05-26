package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.AdminService;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.Member;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (mView.isActive())
            mView.showMemberIndicator(showRefreshLoadingUI);

        mService.getGroupList(MtApplication.mSPUtils.getInt(Api.USERID), "F")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupList>() {
                    @Override
                    protected void _onNext(GroupList o) {
                        if (mView.isActive()) {
                            mView.showMemberIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                List<Member> currentMembers = new ArrayList<>();
                                if (o.getData().getGroupList()!=null&&o.getData().getGroupList().size()>0){
                                    List<GroupList.DataBean.GroupListBean.UserListBean> userList = o.getData().getGroupList().get(0).getUserList();
                                    for (GroupList.DataBean.GroupListBean.UserListBean bean : userList) {
                                        Member member = new Member();
                                        member.setId(bean.getUserid());
                                        member.setName(bean.getUserName());
                                        member.setGroupLeader(false);
                                        member.setSelected(false);
                                        currentMembers.add(member);
                                    }
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
                        if (mView.isActive()){
                            mView.showMemberIndicator(false);
                            mView.showMembersError();
                        }
                    }
                });
    }

    @Override
    public void groupMDF(boolean showRefreshLoadingUI, List<GroupList.DataBean.GroupListBean> groupListBeanList) {
        if (mView.isActive())
            mView.showMemberIndicator(showRefreshLoadingUI);

        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("userid",MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("groupList",groupListBeanList);
        mService.groupMDFTest(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mView.isActive()){
                            mView.showMemberIndicator(false);
                            int code = o.getCode();
                            boolean isSuccess= code == 0;
                           mView.showGroupMDFResult(isSuccess,o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        mView.showMemberIndicator(false);
                        mView.showMembersError();
                    }
                });

//        mService.groupMDF(MtApplication.mSPUtils.getInt(Api.USERID), groupListBeanList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<TaskAbandon>() {
//                    @Override
//                    protected void _onNext(TaskAbandon o) {
//                        if (mView.isActive()){
//                            mView.showMemberIndicator(false);
//                            int code = o.getCode();
//                            if (CommUtil.checkIsNull(o.getMsg())){
//                                mView.showGroupMDFResult(o.getMsg());
//                            }
//                            if (code==0){
//
//                            }else if (code==-1){
//                                mView.showMembersError();
//                            }else {
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    protected void _onError() {
//                        mView.showMemberIndicator(false);
//                        mView.showMembersError();
//                    }
//                });
    }
}
