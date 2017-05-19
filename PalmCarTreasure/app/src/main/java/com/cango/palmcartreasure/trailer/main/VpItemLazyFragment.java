package com.cango.palmcartreasure.trailer.main;

import android.os.Bundle;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseLazyFragment;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * Created by cango on 2017/4/6.
 */

public class VpItemLazyFragment extends BaseLazyFragment {

    @BindView(R.id.tv_vp_item_plate)
    TextView tvPlate;
    @BindView(R.id.tv_vp_item_name)
    TextView tvName;
    @BindView(R.id.tv_vp_item_monthpayments)
    TextView tvMents;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_vp_item_tag)
    TextView tvTag;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    public static VpItemLazyFragment newInstance(TypeTaskData.DataBean.TaskListBean taskListBean) {
        VpItemLazyFragment fragment = new VpItemLazyFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("taskListBean",taskListBean);
        fragment.setArguments(arguments);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.trailer_vp_item;
    }

    @Override
    public void initData() {
        Logger.d(11111111);
        TypeTaskData.DataBean.TaskListBean mTaskListBean= (TypeTaskData.DataBean.TaskListBean) getArguments().get("taskListBean");
        tvPlate.setText(mTaskListBean.getCustomerName());
        tvName.setText(mTaskListBean.getApplyCD());
        tvMents.setText(mTaskListBean.getCarPlateNO());
        tvDistance.setText(mTaskListBean.getDistance());
        tvTag.setText(mTaskListBean.getShortName());
        tvStatus.setText(mTaskListBean.getFlowStauts());
    }
}
