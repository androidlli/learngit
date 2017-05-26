package com.cango.palmcartreasure.trailer.mine;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.LoginService;
import com.cango.palmcartreasure.model.PersonMain;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/21.
 */

public class MinePresenter implements MineContract.Presenter {
    MineContract.View mMineView;
    private LoginService mService;

    public MinePresenter(MineContract.View mineView) {
        mMineView = mineView;
        mMineView.setPresenter(this);
        mService = NetManager.getInstance().create(LoginService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadMineData(boolean showLoadingUI) {
        if (mMineView.isActive()){
            mMineView.showMineDataIndicator(showLoadingUI);
        }
        mService.getPersonMain(MtApplication.mSPUtils.getInt(Api.USERID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<PersonMain>() {
                    @Override
                    protected void _onNext(PersonMain o) {
                        if (mMineView.isActive()){
                            mMineView.showMineDataIndicator(false);
                            int code = o.getCode();
                            if (code==0){
                                mMineView.showMineData(o.getData());
                            }else {
                                mMineView.showMineDataError();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mMineView.isActive()){
                            mMineView.showMineDataIndicator(false);
                            mMineView.showMineDataError();
                        }
                    }
                });
    }

    @Override
    public void logoutTest(boolean showLoadingUI,double lat,double lon){
        if (mMineView.isActive()) {
            mMineView.showMineDataIndicator(showLoadingUI);
        }
        Map<String,Object> stringMap=new HashMap<>();
        stringMap.put("userid",MtApplication.mSPUtils.getInt(Api.USERID));
        stringMap.put("LAT",lat);
        stringMap.put("LON",lon);
        mService.logoutTest(stringMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mMineView.isActive()) {
                            mMineView.showMineDataIndicator(false);
                            int code = o.getCode();
                            if (code==0){
                                MtApplication.mSPUtils.clear();
                                MtApplication.mSPUtils.put(Api.ISSHOWSTARTOVER,true);
                                if (!CommUtil.checkIsNull(o.getMsg()))
                                    mMineView.showLogoutMessage(true,o.getMsg());
                            }else {
                                if (!CommUtil.checkIsNull(o.getMsg()))
                                    mMineView.showLogoutMessage(false,o.getMsg());
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mMineView.isActive()){
                            mMineView.showMineDataIndicator(false);
                            mMineView.showMineDataError();
                        }
                    }
                });
    }

    @Override
    public void logout(boolean showLoadingUI, double lat, double lon) {
        if (mMineView.isActive()) {
            mMineView.showMineDataIndicator(showLoadingUI);
        }
        mService.logout(MtApplication.mSPUtils.getInt(Api.USERID), lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mMineView.isActive()) {
                            mMineView.showMineDataIndicator(false);
                            int code = o.getCode();
                            if (code==0){
                                MtApplication.mSPUtils.clear();
                                if (CommUtil.checkIsNull(o.getMsg()))
                                    mMineView.showLogoutMessage(true,o.getMsg());
                            }else {
                                if (CommUtil.checkIsNull(o.getMsg()))
                                    mMineView.showLogoutMessage(false,o.getMsg());
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mMineView.isActive()){
                            mMineView.showMineDataIndicator(false);
                            mMineView.showMineDataError();
                        }
                    }
                });
    }
}
