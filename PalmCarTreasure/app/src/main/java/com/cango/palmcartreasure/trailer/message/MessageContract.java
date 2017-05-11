package com.cango.palmcartreasure.trailer.message;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.TypeTaskData;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public interface MessageContract {
    interface View extends BaseView<MessageContract.Presenter> {
        void showMessagesIndicator(boolean active);

        void showMessagesError();

        void showMessages(List<String> messages);

        void showNoMessages();

        void showMessageDetailUi(int id);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void openDetailTask(int id);

        void loadTasks(boolean showRefreshLoadingUI, int pageCount, int pageSize);
    }
}
