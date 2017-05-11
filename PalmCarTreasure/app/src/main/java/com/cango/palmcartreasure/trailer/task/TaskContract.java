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

        void showTasks(List<TypeTaskData.DataBean> tasks);

        void showNoTasks();

        void showTaskDetailUi(int taskId);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void openDetailTask(int id);

        void loadTasks(String type,float lat,float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);

    }
}
