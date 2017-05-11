package com.cango.palmcartreasure.trailer.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.baseAdapter.OnLoadMoreListener;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailActivity;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by cango on 2017/4/7.
 */

public class TrailerTasksFragment extends BaseFragment implements TaskContract.View, SwipeRefreshLayout.OnRefreshListener {
    public static final String NEW_TASK = "newTask";
    public static final String INPROGRESS_TASK = "inprogressTask";
    public static final String DONE_TASK = "doneTask";
    private static final String TYPE = "type";
    @BindView(R.id.toolbar_task)
    Toolbar mToolbar;
    @BindView(R.id.iv_task_down)
    ImageView ivDown;
    @BindView(R.id.swipeRefreshLayout_task)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView_task)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;

    @OnClick({R.id.ll_task_search, R.id.ll_select_task})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_task_search:
                rlShadow.setVisibility(View.VISIBLE);
                mSearchPW.update();
                mSearchPW.showAsDropDown(mToolbar);
                break;
            case R.id.ll_select_task:
                ivDown.setImageResource(R.drawable.upward);
                rlShadow.setVisibility(View.VISIBLE);
                mSelectPW.update();
                mSelectPW.showAsDropDown(mToolbar);
                break;
            default:
                break;
        }
    }

    //定位相关
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLoactionListener;
    private float mLat, mLon;
    private boolean isLocationGet;
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;
    /**
     * newTask;inprogressTask;doneTask
     */
    private String mType;
    private TaskContract.Presenter mPresenter;
    private TrailerTasksActivity mActivity;
    private TrailerTaskAdapter mAdapter;
    private PopupWindow mSearchPW, mSelectPW;

    public static TrailerTasksFragment getInstance(String type) {
        TrailerTasksFragment trailerTasksFragment = new TrailerTasksFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        trailerTasksFragment.setArguments(bundle);
        return trailerTasksFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer_task;
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
        mActivity = (TrailerTasksActivity) getActivity();
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

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new TrailerTaskAdapter(mActivity, new ArrayList<TypeTaskData.DataBean>(), true);
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                if (!CommUtil.checkIsNull(mType))
                    mPresenter.loadTasks(mType, mLat, mLon, false, mPageCount, PAGE_SIZE);
            }
        });
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<TypeTaskData.DataBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, TypeTaskData.DataBean data, int position) {
                mPresenter.openDetailTask(-1);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSearchPW = getPopupWindow(getActivity(), R.layout.task_search_popup);
        mSelectPW = getPopupWindow(getActivity(), R.layout.task_selectstatu_popup);
        //开启presenter
        mPresenter.start();

    }

    @Override
    protected void initData() {
        if (!CommUtil.checkIsNull(getArguments())) {
            mType = getArguments().getString(TYPE);
        }
        initLocation();
        mLocationClient.startLocation();
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mLoactionListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (CommUtil.checkIsNull(aMapLocation)) {
                } else {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        String dateString = df.format(date);
//                        Logger.d(dateString + ": Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
                        BigDecimal latBigDecimal = new BigDecimal(String.valueOf(aMapLocation.getLatitude()));
                        mLat = latBigDecimal.floatValue();
                        BigDecimal lonBigDecimal = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
                        mLon = lonBigDecimal.floatValue();
                        if (mLat != 0f && mLon != 0f && !isLocationGet) {
                            if (!CommUtil.checkIsNull(mType)) {
                                mPresenter.loadTasks(mType, mLat, mLon, true, 1, PAGE_SIZE);
                                isLocationGet = true;
                            }
                        }
                    } else {
                        Logger.d("errorCode = " + aMapLocation.getErrorCode() + " errorInfo = " + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLoactionListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(1000);
        mLocationClient.setLocationOption(option);
    }

    @Override
    public void setPresenter(TaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTasksIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTasksError() {
        if (isLoadMore) {

        } else {
            showTasksIndicator(false);
        }
        mAdapter.setLoadFailedView(R.layout.load_failed_layout);
    }

    @Override
    public void showNoTasks() {
        if (isLoadMore) {

        } else {
            showTasksIndicator(false);
        }
        mAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showTaskDetailUi(int taskId) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailFragment.ID, taskId);
        mActivity.mSwipeBackHelper.forward(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showTasks(final List<TypeTaskData.DataBean> tasks) {
        if (isLoadMore) {
            if (tasks.size() == 0) {
                mAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                mTempPageCount++;
                mAdapter.setLoadMoreData(tasks);
            }
        } else {
            showTasksIndicator(false);
            mAdapter.setNewData(tasks);
        }
        if (tasks.size() < PAGE_SIZE) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        if (!CommUtil.checkIsNull(mType))
            mPresenter.loadTasks(mType, mLat, mLon, true, mPageCount, PAGE_SIZE);
    }

    private void changeTypeThenRefresh(String type) {
        mType = type;
        onRefresh();
    }

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        if (layoutId==R.layout.task_selectstatu_popup){
            popupView.findViewById(R.id.tv_proceed_task).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.mSwipeBackHelper.forward(TaskDetailActivity.class);
                }
            });
        }
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        // TODO: 2016/5/17 设置动画
        // TODO: 2016/5/17 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        popupWindow.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (layoutId==R.layout.task_selectstatu_popup){
                    ivDown.setImageResource(R.drawable.down);
                }
                rlShadow.setVisibility(View.GONE);
            }
        });
        // TODO：更新popupwindow的状态
        popupWindow.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        return popupWindow;
    }
}
