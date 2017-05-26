package com.cango.palmcartreasure.trailer.mine;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.PersonMain;

import java.util.List;

/**
 * Created by cango on 2017/4/21.
 */

public interface MineContract {
    interface View extends BaseView<Presenter> {
        void showMineDataIndicator(boolean active);

        void showMineDataError();

        void showMineData(PersonMain.DataBean dataBean);

        void showLogoutMessage(boolean isSuccess, String message);

        void showNoMineData();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadMineData(boolean showLoadingUI);

        void logout(boolean showLoadingUI, double lat, double lon);

        void logoutTest(boolean showLoadingUI,double lat,double lon);
    }
}
