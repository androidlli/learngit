package com.cango.palmcartreasure.trailer.mine;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;

import java.util.List;

/**
 * Created by cango on 2017/4/21.
 */

public interface MineContract {
    interface View extends BaseView<Presenter>{
        void showMineDataIndicator(boolean active);
        void showMineDataError();
        void showMineData(List<String> mineData);
        void showNoMineData();
        boolean isActive();
    }
    interface Presenter extends BasePresenter{
        void loadMineData(boolean showLoadingUI);
    }
}
