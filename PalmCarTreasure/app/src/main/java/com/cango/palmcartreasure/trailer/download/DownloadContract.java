package com.cango.palmcartreasure.trailer.download;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public interface DownloadContract {
    interface View extends BaseView<DownloadContract.Presenter> {
        void showDatasIndicator(boolean active);

        void showDatasError();

        void showDatas(List<String> datas);

        void showNoDatas();

        void showDataDetailUi(int id);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadDtas(boolean showRefreshLoadingUI, int pageCount, int pageSize);
    }
}
