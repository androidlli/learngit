package com.cango.palmcartreasure.trailer.admin;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;

import java.util.List;

/**
 * 查询具体的组的任务
 * Created by cango on 2017/4/10.
 */

public class AdminGroupAdapter extends BaseAdapter<GroupTaskQuery.DataBean.TaskListBean> {
    private MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener;

    public AdminGroupAdapter(Context context, List<GroupTaskQuery.DataBean.TaskListBean> datas, boolean isOpenLoadMore, MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener) {
        super(context, datas, isOpenLoadMore);
        this.mtOnCheckedChangeThenTypeListener=mtOnCheckedChangeThenTypeListener;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.admin_task_item;
    }

    @Override
    protected void convert(BaseHolder holder, final GroupTaskQuery.DataBean.TaskListBean data) {
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
        TextView tvGroupName= holder.getView(R.id.tv_group_name);
        TextView tvShortName = holder.getView(R.id.tv_task_item_type);
        TextView tvCustomerName = holder.getView(R.id.tv_task_item_name);
        TextView tvCarPlateNO = holder.getView(R.id.tv_task_item_plate);
        TextView tvFeerate = holder.getView(R.id.tv_feerate);
        TextView tvPrompt = holder.getView(R.id.tv_prompt);
        TextView tvRedueDays=holder.getView(R.id.tv_redueDays);
        TextView tvAgencyAmount = holder.getView(R.id.tv_agencyAmount);

        tvApplyCD.setText(data.getApplyCD());
        tvGroupName.setText(data.getGroupName());
        tvShortName.setText(data.getShortName());
        tvCustomerName.setText(data.getCustomerName());
        tvCarPlateNO.setText(data.getCarPlateNO());

        double feerate = data.getFeerate();
        tvFeerate.setText(data.getFeerate()+"");
        if (feerate==0){
            tvFeerate.setVisibility(View.INVISIBLE);
            tvPrompt.setVisibility(View.INVISIBLE);
        }else {
            tvFeerate.setVisibility(View.VISIBLE);
            tvPrompt.setVisibility(View.VISIBLE);
        }

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

    public interface MtOnCheckedChangeThenTypeListener{
        void mtOnCheckedChangedThenType();
    }
}
