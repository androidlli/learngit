package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.AdminService;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;

import org.w3c.dom.CDATASection;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/28.
 */

public class AdminTaskPresenter implements AdminTasksContract.Presenter {
    private AdminTasksContract.View mAdminView;
    private AdminService mService;

    public AdminTaskPresenter(AdminTasksContract.View adminView) {
        mAdminView = adminView;
        mAdminView.setPresenter(this);
        mService = NetManager.getInstance().create(AdminService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void loadAdminTasks(String type, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showAdminTasksIndicator(showRefreshLoadingUI);
        } else {

        }
        if (type.equals(AdminTasksFragment.GROUP)) {
            mService.getGroupTaskCount(MtApplication.mSPUtils.getInt(Api.USERID),
                    MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
                    MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<GroupTaskCount>() {
                        @Override
                        protected void _onNext(GroupTaskCount o) {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                int code = o.getCode();
                                if (code == 0) {
                                    mAdminView.showAdminTasks(o.getData().getTaskCountList());
                                } else if (code == -1) {
                                    mAdminView.showAdminTasksError();
                                } else {

                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mAdminView.isActive())
                                mAdminView.showAdminTasksIndicator(false);
                        }
                    });
        } else if (type.equals(AdminTasksFragment.TASK)) {

        } else if (type.equals(AdminTasksFragment.ADMIN_UNABSORBED)) {
            mService.getTaskManageList(MtApplication.mSPUtils.getInt(Api.USERID),
                    MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
                    MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON),pageCount,pageSize)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskManageList>() {
                        @Override
                        protected void _onNext(TaskManageList o) {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                int code = o.getCode();
                                if (code == 0) {
                                    mAdminView.showAdminUnabsorbedTasks(o.getData().getTaskList());
                                } else if (code == -1) {
                                    mAdminView.showAdminTasksError();
                                } else {

                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mAdminView.isActive())
                                mAdminView.showAdminTasksIndicator(false);
                        }
                    });
        }

    }

    @Override
    public void loadGroupTasks(int[] userIds, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showAdminTasksIndicator(showRefreshLoadingUI);
        } else {

        }
        mService.getGroupTaskQuery(MtApplication.mSPUtils.getInt(Api.USERID),userIds, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
                MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON),pageCount,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupTaskQuery>() {
                    @Override
                    protected void _onNext(GroupTaskQuery o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                mAdminView.showAdminGroupTasks(o.getData().getTaskList());
                            } else if (code == -1) {
                                mAdminView.showAdminTasksError();
                            } else {

                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive())
                            mAdminView.showAdminTasksIndicator(false);
                    }
                });
    }

    @Override
    public void giveUpTasks(TaskAbandonRequest[] requests) {
        mService.TaskAbandon(MtApplication.mSPUtils.getInt(Api.USERID),requests)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            int code = o.getCode();
                            if (code==0||code==-1){
                                boolean isSuccess=code==0?true:false;
                                mAdminView.showGiveUpTasksAndNotifyUi(isSuccess,o.getMsg());
                            }
                        }
                    }

                    @Override
                    protected void _onError() {

                    }
                });
    }
}
