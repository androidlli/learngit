package com.cango.palmcartreasure.trailer.message;

/**
 * Created by cango on 2017/5/8.
 */

public class MessagePresenter implements MessageContract.Presenter{
    private MessageContract.View mMessageView;
    public MessagePresenter(MessageContract.View messageView) {
        mMessageView=messageView;
    }

    @Override
    public void start() {

    }

    @Override
    public void openDetailTask(int id) {

    }

    @Override
    public void loadTasks(boolean showRefreshLoadingUI, int pageCount, int pageSize) {

    }
}
