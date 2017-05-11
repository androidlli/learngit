package com.cango.palmcartreasure.register;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;

/**
 * Created by cango on 2017/4/26.
 */

public class RegisterContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
    }
    interface Presenter extends BasePresenter {
    }
}
