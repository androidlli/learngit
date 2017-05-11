package com.cango.palmcartreasure.trailer.taskdetail;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;

import java.util.List;

/**
 * Created by cango on 2017/4/17.
 */

public interface TaskDetailContract {
    interface View extends BaseView<Presenter> {
        void showTaskDetailIndicator(boolean active);
        void showTasksDetailError();
        void showTaskDetail(List<String> tasks);
        void showNoTaskDetail();
        boolean isActive();
    }
    interface Presenter extends BasePresenter {
        void loadTaskDetail(boolean showLoadingUI,int id);

    }
}
