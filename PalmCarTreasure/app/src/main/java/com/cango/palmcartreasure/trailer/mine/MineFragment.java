package com.cango.palmcartreasure.trailer.mine;


import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.trailer.admin.AdminActivity;
import com.cango.palmcartreasure.trailer.download.DownloadActivity;
import com.cango.palmcartreasure.trailer.message.MessageActivity;
import com.cango.palmcartreasure.trailer.personal.PersonalActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksActivity;
import com.cango.palmcartreasure.util.BarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends BaseFragment implements MineContract.View {

    @BindView(R.id.toolbar_mine)
    Toolbar mToolbar;

    @OnClick({R.id.rl_personal_data,R.id.rl_message,R.id.rl_clear_cache,R.id.btn_exit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_personal_data:
                mActivity.mSwipeBackHelper.forward(PersonalActivity.class);
                break;
            case R.id.rl_message:
                mActivity.mSwipeBackHelper.forward(MessageActivity.class);
                break;
            case R.id.rl_clear_cache:
                //test
                mActivity.mSwipeBackHelper.forward(DownloadActivity.class);
                break;
            case R.id.btn_exit:
                //测试使用
                mActivity.mSwipeBackHelper.forward(AdminActivity.class);
                break;
        }
    }

    private MineActivity mActivity;
    private MineContract.Presenter mPresenter;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity = (MineActivity) getActivity();
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void showMineDataIndicator(boolean active) {

    }

    @Override
    public void showMineDataError() {

    }

    @Override
    public void showMineData(List<String> mineData) {

    }

    @Override
    public void showNoMineData() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
