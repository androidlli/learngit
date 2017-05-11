package com.cango.palmcartreasure.trailer.mine;

/**
 * Created by cango on 2017/4/21.
 */

public class MinePresenter implements MineContract.Presenter {
    MineContract.View mMineView;

    public MinePresenter(MineContract.View mineView) {
        mMineView = mineView;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadMineData(boolean showLoadingUI) {

    }
}
