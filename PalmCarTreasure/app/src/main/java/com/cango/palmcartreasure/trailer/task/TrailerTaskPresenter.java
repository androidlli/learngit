package com.cango.palmcartreasure.trailer.task;

import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/7.
 */

public class TrailerTaskPresenter implements TaskContract.Presenter {
    private TaskContract.View mTaskView;
    private TrailerTaskService mTaskService;

    public TrailerTaskPresenter(TaskContract.View taskView) {
        mTaskView = taskView;
        mTaskView.setPresenter(this);
        mTaskService = NetManager.getInstance().create(TrailerTaskService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void openDetailTask(int id) {
        mTaskView.showTaskDetailUi(id);
    }

    @Override
    public void loadTasks(String type, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize) {
        if (showRefreshLoadingUI) {
            mTaskView.showTasksIndicator(showRefreshLoadingUI);
        } else {
        }
        Observable<TypeTaskData> taskDataObservable = null;
        if (type.equals(TrailerTasksFragment.NEW_TASK)) {
            taskDataObservable = mTaskService.getNewTaskList("", lat, lon, "");
        } else if (type.equals(TrailerTasksFragment.INPROGRESS_TASK)) {
            taskDataObservable = mTaskService.getTaskInprogressList("", lat, lon, "");
        } else if (type.equals(TrailerTasksFragment.DONE_TASK)) {
            taskDataObservable = mTaskService.getTaskDoneList("", lat, lon, "");
        } else {
        }
        taskDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TypeTaskData>() {
                    @Override
                    protected void _onNext(TypeTaskData o) {
                        if (o.isSuccess()) {
                            if (!CommUtil.checkIsNull(o.getData())) {
                                if (mTaskView.isActive())
                                    mTaskView.showTasks(o.getData());
                            }
                        } else {
                            if (mTaskView.isActive())
                                mTaskView.showTasksError();
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mTaskView.isActive()) {
                            mTaskView.showNoTasks();
                        }
                    }
                });
    }
}
