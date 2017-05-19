package com.cango.palmcartreasure.trailer.task;

import android.content.Context;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.TypeTaskData;

import java.util.List;

/**
 * Created by cango on 2017/4/10.
 */

public class TrailerTaskAdapter extends BaseAdapter<TypeTaskData.DataBean.TaskListBean> {
    public TrailerTaskAdapter(Context context, List<TypeTaskData.DataBean.TaskListBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        //如果是管理员的话就返回 管理员item界面,如果不是就返回普通的
        return R.layout.trailer_task_item_normal;
    }

    @Override
    protected void convert(BaseHolder holder, TypeTaskData.DataBean.TaskListBean data) {
        TextView tvApplyCD = holder.getView(R.id.tv_task_item_id);
        TextView tvDistance = holder.getView(R.id.tv_distance);
        TextView tvShortName = holder.getView(R.id.tv_task_item_type);
        TextView tvCustomerName = holder.getView(R.id.tv_task_item_name);
        TextView tvCarPlateNO = holder.getView(R.id.tv_task_item_plate);

        tvApplyCD.setText(data.getApplyCD());
        tvDistance.setText(data.getDistance());
        tvShortName.setText(data.getShortName());
        tvCustomerName.setText(data.getCustomerName());
        tvCarPlateNO.setText(data.getCarPlateNO());

    }
}
