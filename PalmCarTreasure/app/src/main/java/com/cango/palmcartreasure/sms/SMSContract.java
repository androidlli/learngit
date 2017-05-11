package com.cango.palmcartreasure.sms;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;

/**
 * Created by cango on 2017/4/26.
 */

public interface SMSContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
    }
    interface Presenter extends BasePresenter {
    }
}
