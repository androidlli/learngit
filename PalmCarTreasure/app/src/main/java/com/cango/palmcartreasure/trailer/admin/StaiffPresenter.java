package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.AdminService;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/5/2.
 */

public class StaiffPresenter implements StaiffContract.Presenter {
    private StaiffContract.View mStaiffView;
    private AdminService mService;

    public StaiffPresenter(StaiffContract.View staiffView) {
        mStaiffView = staiffView;
        mStaiffView.setPresenter(this);
        mService= NetManager.getInstance().create(AdminService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadStaiff(String type, boolean showRefreshLoadingUI, int pageCount, int pageSize) {
        if (showRefreshLoadingUI){
            if (mStaiffView.isActive()){
                mStaiffView.showStaiffIndicator(showRefreshLoadingUI);
            }
        }
        mService.getGroupList(MtApplication.mSPUtils.getInt(Api.USERID),"T")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupList>() {
                    @Override
                    protected void _onNext(GroupList o) {
                        if (mStaiffView.isActive()) {
                            int code = o.getCode();
                            if (code == 0) {
                                mStaiffView.showStaiff(o.getData().getGroupList());
                            } else if (code == -1) {
                                mStaiffView.showStaiffError();
                            } else {

                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mStaiffView.isActive())
                            mStaiffView.showStaiffError();
                    }
                });
    }
}
