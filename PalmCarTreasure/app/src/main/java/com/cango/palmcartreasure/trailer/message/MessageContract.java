package com.cango.palmcartreasure.trailer.message;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.MessageList;
import com.cango.palmcartreasure.model.TypeTaskData;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public interface MessageContract {
    interface View extends BaseView<MessageContract.Presenter> {
        void showMessagesIndicator(boolean active);

        void showMessagesError();

        void showMessages(List<MessageList.DataBean.MessageListBean> dataBeanList);

        void showMessageDetailUi(int id);

        void showMessageSuccess(boolean isSuccess,String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void openDetailTask(int id);

        void loadMessages(boolean showRefreshLoadingUI, int status,int pageCount, int pageSize);

        void postMessageRead(int messageID);
    }
}
