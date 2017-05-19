package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.AdminService;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
=======
import java.util.List;
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794

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
                                    if (o.getData() == null) {
                                        mAdminView.showAdminTasksError();
                                    } else {
//                                        mAdminView.showAdminTasks(o.getData().getTaskCountList());
                                        if (CommUtil.checkIsNull(o.getData())) {
                                            mAdminView.showAdminTasksError();
                                        } else {
                                            if (!CommUtil.checkIsNull(o.getData().getTaskCountList()) && o.getData().getTaskCountList().size() > 0)
                                                mAdminView.showAdminTasks(o.getData().getTaskCountList());
                                            else {
                                                mAdminView.showNoAdminTasks();
                                            }
                                        }
                                    }
                                } else
                                    mAdminView.showAdminTasksError();
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                mAdminView.showAdminTasksError();
                            }
                        }
                    });
        } else if (type.equals(AdminTasksFragment.TASK)) {

        } else if (type.equals(AdminTasksFragment.ADMIN_UNABSORBED)) {
            mService.getTaskManageList(MtApplication.mSPUtils.getInt(Api.USERID),
                    MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
                    MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON), pageCount, pageSize)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskManageList>() {
                        @Override
                        protected void _onNext(TaskManageList o) {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                int code = o.getCode();
                                if (code == 0) {
//                                    mAdminView.showAdminUnabsorbedTasks(o.getData().getTaskList());
                                    if (o.getData() == null) {
                                        mAdminView.showAdminTasksError();
                                    } else {
                                        if (CommUtil.checkIsNull(o.getData())) {
                                            mAdminView.showAdminTasksError();
                                        } else {
                                            if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                                mAdminView.showAdminUnabsorbedTasks(o.getData().getTaskList());
                                            else {
                                                mAdminView.showNoAdminTasks();
                                            }
                                        }
                                    }
                                } else
                                    mAdminView.showAdminTasksError();
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                mAdminView.showAdminTasksError();
                            }
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
<<<<<<< HEAD
//        mService.getGroupTaskQuery(MtApplication.mSPUtils.getInt(Api.USERID), userIds, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
//                MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON), pageCount, pageSize)
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("groupIDList", userIds);
        objectMap.put("LAT", lat);
        objectMap.put("LON", lon);
        objectMap.put("pageIndex", pageCount);
        objectMap.put("pageSize", pageSize);
        mService.getGroupTaskQuery(objectMap)
=======
        mService.getGroupTaskQuery(MtApplication.mSPUtils.getInt(Api.USERID), userIds, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
                MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON), pageCount, pageSize)
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupTaskQuery>() {
                    @Override
                    protected void _onNext(GroupTaskQuery o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mAdminView.showAdminTasksError();
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                        mAdminView.showAdminGroupTasks(o.getData().getTaskList());
                                    else {
                                        mAdminView.showNoAdminTasks();
                                    }
                                }
                            } else
                                mAdminView.showAdminTasksError();
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            mAdminView.showAdminTasksError();
                        }
                    }
                });
    }

    @Override
    public void groupTaskDraw(final boolean showRefreshLoadingUI, List<GroupTaskQuery.DataBean.TaskListBean> taskListBeanList) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showLoadingView(showRefreshLoadingUI);
        }
//        mService.groupTaskDraw(MtApplication.mSPUtils.getInt(Api.USERID), taskListBeanList)
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("taskList", taskListBeanList);
        mService.groupTaskDraw(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                            int code = o.getCode();
                            boolean isSuccess = code == 0;
                            mAdminView.showGroupTaskDraw(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                        }
                    }
                });
    }

    @Override
    public void groupTaskDraw(boolean showRefreshLoadingUI, List<GroupTaskQuery.DataBean.TaskListBean> taskListBeanList) {
//        if (showRefreshLoadingUI) {
//            if (mAdminView.isActive())
//                mAdminView.showAdminTasksIndicator(showRefreshLoadingUI);
//        }
        mService.groupTaskDraw(MtApplication.mSPUtils.getInt(Api.USERID), taskListBeanList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            int code = o.getCode();
                            boolean isSuccess = code == 0 ? true : false;
                            mAdminView.showGroupTaskDraw(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()){
//                            mAdminView.showAdminTasksIndicator(false);
                        }
                    }
                });
    }

    @Override
    public void giveUpTasks(TaskAbandonRequest[] requests) {
<<<<<<< HEAD
        if (mAdminView.isActive()) {
            mAdminView.showLoadingView(true);
        }
//        mService.TaskAbandon(MtApplication.mSPUtils.getInt(Api.USERID), requests)
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("taskList", requests);
        mService.TaskAbandon(objectMap)
=======
        mService.TaskAbandon(MtApplication.mSPUtils.getInt(Api.USERID), requests)
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                            int code = o.getCode();
                            if (code == 0 || code == -1) {
<<<<<<< HEAD
                                boolean isSuccess = code == 0;
=======
                                boolean isSuccess = code == 0 ? true : false;
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794
                                mAdminView.showGiveUpTasksAndNotifyUi(isSuccess, o.getMsg());
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                        }
                    }
                });
    }

    @Override
    public void openDetailTask(TypeTaskData.DataBean.TaskListBean taskListBean) {
        mAdminView.showAdminTaskDetailUi(taskListBean);
    }
}
