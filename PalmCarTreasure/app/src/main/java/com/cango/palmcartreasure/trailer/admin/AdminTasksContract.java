package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
<<<<<<< HEAD
import com.cango.palmcartreasure.model.TypeTaskData;
=======
>>>>>>> 3426a54d57be1c35f5f9803960ceab4e1f563794

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
         *
         * @param
         */
        void showAdminTasks(List<GroupTaskCount.DataBean.TaskCountListBean> tasks);

        /**
         * 展示具体的group的所有任务
         */
        void showAdminGroupTasks(List<GroupTaskQuery.DataBean.TaskListBean> tasks);

        /**
         * 展示抽回结果
         *
         * @param isSuccess
         * @param message
         */
        void showGroupTaskDraw(boolean isSuccess, String message);

        /**
         * 展示管理员所有未分配的任务
         */
        void showAdminUnabsorbedTasks(List<TaskManageList.DataBean.TaskListBean> tasks);

        void showGiveUpTasksAndNotifyUi(boolean isSuccess, String message);

        void showNoAdminTasks();

        void showAdminTaskDetailUi(TypeTaskData.DataBean.TaskListBean taskListBean);

        void showLoadingView(boolean isShow);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadAdminTasks(String type, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);

        void loadGroupTasks(int[] groupIds, float lat, float lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);

        //抽回任务
        void groupTaskDraw(boolean showRefreshLoadingUI, List<GroupTaskQuery.DataBean.TaskListBean> taskListBeanList);

        //放弃任务
        void giveUpTasks(TaskAbandonRequest[] requests);

        void openDetailTask(TypeTaskData.DataBean.TaskListBean taskListBean);
    }
}
