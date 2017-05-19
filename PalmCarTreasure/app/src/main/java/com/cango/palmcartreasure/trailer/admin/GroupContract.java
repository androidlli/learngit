package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.Member;

import java.util.List;

/**
 * Created by cango on 2017/5/2.
 */

public interface GroupContract {
    interface View extends BaseView<GroupContract.Presenter> {
        void showMemberIndicator(boolean active);

        void showMembersError();

        void showMembers(List<Member> members);

        void showNoMembers();

        void showMemberDetailUi(int staiffId);

        void showGroupMDFResult(boolean isSuccess,String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadMembers(String type, boolean showRefreshLoadingUI, int pageCount, int pageSize);

        void groupMDF(boolean showRefreshLoadingUI, List<GroupList.DataBean.GroupListBean> groupListBeanList);
    }
}
