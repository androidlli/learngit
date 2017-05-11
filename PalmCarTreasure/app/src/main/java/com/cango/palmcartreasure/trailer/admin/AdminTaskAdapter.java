package com.cango.palmcartreasure.trailer.admin;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.GroupTaskCount;

import java.util.List;

/**
 * 查询管理员所有的组的adapter
 * Created by cango on 2017/4/10.
 */

public class AdminTaskAdapter extends BaseAdapter<GroupTaskCount.DataBean.TaskCountListBean> {
    private MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener;

    public AdminTaskAdapter(Context context, List<GroupTaskCount.DataBean.TaskCountListBean> datas, boolean isOpenLoadMore,MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener) {
        super(context, datas, isOpenLoadMore);
        this.mtOnCheckedChangeThenTypeListener=mtOnCheckedChangeThenTypeListener;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.admin_group_item;
    }

    @Override
    protected void convert(BaseHolder holder, final GroupTaskCount.DataBean.TaskCountListBean data) {
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

        TextView tvGroupName = holder.getView(R.id.tv_group_name);
        TextView tvLeader = holder.getView(R.id.tv_group_leader);
        TextView tvNewTaskCount = holder.getView(R.id.tv_newTaskCount);
        TextView tvProgressCount = holder.getView(R.id.tv_taskInProgressCount);
        TextView tvDone = holder.getView(R.id.tv_taskDoneCount);
        tvGroupName.setText(data.getGroupName());
        tvLeader.setText(data.getGroupLeader());
        tvNewTaskCount.setText(data.getNewTaskCount()+"");
        tvProgressCount.setText(data.getTaskInProgressCount()+"");
        tvDone.setText(data.getTaskDoneCount()+"");
    }

    public interface MtOnCheckedChangeThenTypeListener{
        void mtOnCheckedChangedThenType();
    }
}
