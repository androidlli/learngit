package com.cango.palmcartreasure.login;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;

/**
 * Created by cango on 2017/3/27.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter>{
        void showLoginIndicator(boolean active);
        void showLoginError();
        void showLoginSuccess();
        void openOtherUi();
        boolean isActive();
    }
    interface Presenter extends BasePresenter{
        void login(String userName,String password,String imei,float lat,float lon,String deviceToken,String deviceType);
    }
}
