package com.cango.palmcartreasure.sms;

/**
 * Created by cango on 2017/4/26.
 */

public class SMSPresenter implements SMSContract.Presenter {
    private SMSContract.View mSMSView;
    public SMSPresenter(SMSContract.View SMSView){
        mSMSView=SMSView;
    }
    @Override
    public void start() {

    }
}
