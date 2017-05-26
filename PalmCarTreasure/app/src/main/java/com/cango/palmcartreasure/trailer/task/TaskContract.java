package com.cango.palmcartreasure.trailer.task;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.TypeTaskData;

import java.util.List;

/**
 * Created by cango on 2017/4/7.
 */

public interface TaskContract {
    interface View extends BaseView<Presenter> {
        void showTasksIndicator(boolean active);

        void showTasksError();

        void showTasks(List<TypeTaskData.DataBean.TaskListBean> tasks);

        void showNoTasks();

        void showTaskDetailUi(TypeTaskData.DataBean.TaskListBean taskListBean);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void openDetailTask(TypeTaskData.DataBean.TaskListBean taskListBean);

        void loadTasks(String type,double lat,double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);

        void taskQuery(boolean showRefreshLoadingUI,String type,String applyCD,String customerName,int timeFlag,int pageIndex,int pageSize);
    }
}
