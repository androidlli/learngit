package com.cango.palmcartreasure.register;

/**
 * Created by cango on 2017/4/26.
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View mRegisterView;
    public RegisterPresenter(RegisterContract.View registerView){
        mRegisterView=registerView;
    }
    @Override
    public void start() {

    }
}
