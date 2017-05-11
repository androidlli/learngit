package com.cango.palmcartreasure.trailer.taskdetail;

/**
 * Created by cango on 2017/4/17.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {
    private TaskDetailContract.View mDetailView;
    public TaskDetailPresenter(TaskDetailContract.View detailView) {
        mDetailView=detailView;
        mDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadTaskDetail(boolean showLoadingUI, int id) {

    }
}
