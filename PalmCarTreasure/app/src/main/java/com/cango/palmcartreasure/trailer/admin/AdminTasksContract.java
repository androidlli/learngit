package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.trailer.task.TaskContract;

import java.util.List;

/**
 * Created by cango on 2017/4/27.
 */

public interface AdminTasksContract {
    interface View extends BaseView<AdminTasksContract.Presenter> {
        void showAdminTasksIndicator(boolean active);

        void showAdminTasksError();

        /**
         * 展示管理员所有的分组列表
         * @param
         */
        void showAdminTasks(List<GroupTaskCount.DataBean.TaskCountListBean> tasks);

        /**
         * 展示具体的group的所有任务
         */
        void showAdminGroupTasks(List<GroupTaskQuery.DataBean.TaskListBean> tasks);

        /**
         * 展示管理员所有未分配的任务
         */
        void showAdminUnabsorbedTasks(List<TaskManageList.DataBean.TaskListBean> tasks);

        void showGiveUpTasksAndNotifyUi(boolean isSuccess,String message);

        void showNoAdminTasks();

        void showAdminTaskDetailUi(int taskId);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadAdminTasks(String type, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);
        void loadGroupTasks(int[] groupIds, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);
        void giveUpTasks(TaskAbandonRequest[] requests);
    }
}
