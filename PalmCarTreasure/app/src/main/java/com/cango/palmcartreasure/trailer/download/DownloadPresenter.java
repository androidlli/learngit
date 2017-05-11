package com.cango.palmcartreasure.trailer.download;

/**
 * Created by cango on 2017/5/8.
 */

public class DownloadPresenter implements DownloadContract.Presenter {
    private DownloadContract.View mDownloadView;
    public DownloadPresenter(DownloadContract.View downloadView) {
        mDownloadView=downloadView;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadDtas(boolean showRefreshLoadingUI, int pageCount, int pageSize) {

    }
}
