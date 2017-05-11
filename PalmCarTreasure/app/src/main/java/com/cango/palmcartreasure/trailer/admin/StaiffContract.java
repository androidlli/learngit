package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.GroupList;

import java.util.List;

/**
 * Created by cango on 2017/5/2.
 */

public interface StaiffContract {
    interface View extends BaseView<StaiffContract.Presenter> {
        void showStaiffIndicator(boolean active);

        void showStaiffError();

        void showStaiff(List<GroupList.DataBean.GroupListBean> staiffs);

        void showNoStaiff();

        void showStaiffDetailUi(int staiffId);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadStaiff(String type, boolean showRefreshLoadingUI, int pageCount, int pageSize);
    }
}
