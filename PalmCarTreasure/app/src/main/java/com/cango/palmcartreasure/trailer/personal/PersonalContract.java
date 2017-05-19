package com.cango.palmcartreasure.trailer.personal;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.PersonalInfo;

import java.util.List;

/**
 * Created by cango on 2017/4/26.
 */

public interface PersonalContract {
    interface View extends BaseView<Presenter>{
        void showPersonalDataIndicator(boolean active);
        void showPersonalDataError(String message);
        void showPersonalData(PersonalInfo.DataBean dataBean);
        boolean isActive();
    }
    interface Presenter extends BasePresenter{
        void loadPersonalData(boolean showLoadingUI);
    }
}
