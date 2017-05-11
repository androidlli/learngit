package com.cango.palmcartreasure.trailer.personal;

/**
 * Created by cango on 2017/4/26.
 */

public class PersonalPresenter implements PersonalContract.Presenter {
    private PersonalContract.View mPersonalView;
    public PersonalPresenter(PersonalContract.View view){
        mPersonalView=view;
    }
    @Override
    public void start() {

    }

    @Override
    public void loadPersonalData(boolean showLoadingUI) {

    }
}
