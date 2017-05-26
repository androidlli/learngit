package com.cango.palmcartreasure.trailer.taskdetail;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.TaskDetailData;

import java.io.File;

/**
 * Created by cango on 2017/4/17.
 */

public interface TaskDetailContract {
    interface View extends BaseView<Presenter> {
        void showTaskDetailIndicator(boolean active);

        void showTasksDetailError();

        void showTaskDetail(TaskDetailData taskDetailData);

        void showDownLoadResult(boolean isSussess,File file,String msg);

        void showNoTaskDetail();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        //type  0:电催信息 1:家访信息 2：案件信息 3：客户信息
        void loadTaskDetail(int type, boolean showLoadingUI, int agencyID, int caseID);

        //downLoad  docType 1:委托函 2：拖车任务书
        void downLoadFile(int type, boolean showLoadingUI, int userId, int agencyID, int caseID, String docType, String parentDir,
                          TaskDetailFragment.OnDownloadListener listener);
    }
}
