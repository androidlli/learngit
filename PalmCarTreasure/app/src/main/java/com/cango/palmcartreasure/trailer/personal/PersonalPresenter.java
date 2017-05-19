package com.cango.palmcartreasure.trailer.personal;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.LoginService;
import com.cango.palmcartreasure.model.PersonalInfo;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/26.
 */

public class PersonalPresenter implements PersonalContract.Presenter {
    private PersonalContract.View mView;
    private LoginService mService;
    public PersonalPresenter(PersonalContract.View view){
        mView=view;
        mView.setPresenter(this);
        mService= NetManager.getInstance().create(LoginService.class);
    }
    @Override
    public void start() {

    }

    @Override
    public void loadPersonalData(boolean showLoadingUI) {
        if (mView.isActive()){
            mView.showPersonalDataIndicator(showLoadingUI);
        }
        mService.getPersonalInfo(MtApplication.mSPUtils.getInt(Api.USERID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<PersonalInfo>() {
                    @Override
                    protected void _onNext(PersonalInfo o) {
                        if (mView.isActive()){
                            mView.showPersonalDataIndicator(false);
                            int code = o.getCode();
                            if (code==0){
                                mView.showPersonalData(o.getData());
                            }else {
                                mView.showPersonalDataError(o.getMsg());
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()){
                            mView.showPersonalDataIndicator(false);
                        }
                    }
                });
    }
}
