package com.cango.palmcartreasure.trailer.admin;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.TaskManageList;

import java.util.List;

/**
 * 查询所有未分配的任务
 * Created by cango on 2017/4/10.
 */

public class UnabsorbedTaskAdapter extends BaseAdapter<TaskManageList.DataBean.TaskListBean> {
    private MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener;

    public UnabsorbedTaskAdapter(Context context, List<TaskManageList.DataBean.TaskListBean> datas, boolean isOpenLoadMore, MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener) {
        super(context, datas, isOpenLoadMore);
        this.mtOnCheckedChangeThenTypeListener = mtOnCheckedChangeThenTypeListener;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.unabsorbed_task_item;
    }

    @Override
    protected void convert(BaseHolder holder, final TaskManageList.DataBean.TaskListBean data) {
        CheckBox cbSelect = holder.getView(R.id.checkbox_group_item);
        cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setChecked(isChecked);
                if (isChecked){
                    //如果所有的item都被选中，或者选中状态只有几个或一个
                    mtOnCheckedChangeThenTypeListener.mtOnCheckedChangedThenType();
                }
            }
        });
        cbSelect.setChecked(data.isChecked());

        TextView tvApplyCD = holder.getView(R.id.tv_applyCD);
        TextView tvDistance = holder.getView(R.id.tv_distance);
        TextView tvShortName = holder.getView(R.id.tv_task_item_type);
        TextView tvCustomerName = holder.getView(R.id.tv_task_item_name);
        TextView tvCarPlateNO = holder.getView(R.id.tv_task_item_plate);
        TextView tvFeerate = holder.getView(R.id.tv_feerate);
        TextView tvRedueDays=holder.getView(R.id.tv_redueDays);
        TextView tvAgencyAmount = holder.getView(R.id.tv_agencyAmount);
        tvApplyCD.setText(data.getApplyCD());
        tvDistance.setText(data.getDistance());
        tvShortName.setText(data.getShortName());
        tvCustomerName.setText(data.getCustomerName());
        tvCarPlateNO.setText(data.getCarPlateNO());
        tvFeerate.setText(data.getFeerate()+"");
        if (data.getRedueDays()==0){
            tvRedueDays.setText("");
        }else {
            tvRedueDays.setText(data.getRedueDays()+"天");
        }
        if (data.getRedueAmount()==0){
            tvAgencyAmount.setText("");
        }else {
            tvAgencyAmount.setText(data.getRedueAmount()+"元");
        }

    }

    public interface MtOnCheckedChangeThenTypeListener {
        void mtOnCheckedChangedThenType();
    }
}
