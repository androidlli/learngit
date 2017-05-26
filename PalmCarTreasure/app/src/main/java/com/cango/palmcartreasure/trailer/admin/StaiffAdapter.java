package com.cango.palmcartreasure.trailer.admin;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.GroupList;

import java.util.List;
import java.util.Random;

/**
 * Created by cango on 2017/5/2.
 */

public class StaiffAdapter extends BaseAdapter<GroupList.DataBean.GroupListBean> {
    private Random random;

    public StaiffAdapter(Context context, List<GroupList.DataBean.GroupListBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        random=new Random();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.staiff_item;
    }

    @Override
    protected void convert(BaseHolder holder, GroupList.DataBean.GroupListBean data) {
        LinearLayout llBG = holder.getView(R.id.ll_bg);
        TextView tvGroupName = holder.getView(R.id.tv_staiff_group_name);
        TextView tvCrewSize = holder.getView(R.id.tv_crew_size);
        TextView tvLeadName=holder.getView(R.id.tv_lead_name);
        TextView tvCrew1 = holder.getView(R.id.tv_crew1);
        TextView tvCrew2 = holder.getView(R.id.tv_crew2);
        TextView tvCrew3 = holder.getView(R.id.tv_crew3);
        TextView tvCrew4 = holder.getView(R.id.tv_crew4);
        TextView tvCrew5 = holder.getView(R.id.tv_crew5);
        TextView tvCrew6 = holder.getView(R.id.tv_crew6);

        if (data.isSelected()){
            llBG.setBackgroundResource(R.drawable.button_border_red_bg);
        }else{
            llBG.setBackgroundResource(R.color.windowBackground);
        }

        tvGroupName.setText(data.getGroupName());
        tvCrewSize.setText(data.getUserList().size()+"");
        tvLeadName.setText(data.getGroupLeader());
        List<GroupList.DataBean.GroupListBean.UserListBean> userList = data.getUserList();
        //把组长隐藏掉
        if (userList.size()==1){
            GroupList.DataBean.GroupListBean.UserListBean bean1 = userList.get(0);
            tvCrew1.setVisibility(View.INVISIBLE);
            tvCrew2.setVisibility(View.INVISIBLE);
            tvCrew3.setVisibility(View.INVISIBLE);
            tvCrew4.setVisibility(View.INVISIBLE);
            tvCrew5.setVisibility(View.INVISIBLE);
            tvCrew6.setVisibility(View.INVISIBLE);
        }else if (userList.size()==2){
            GroupList.DataBean.GroupListBean.UserListBean bean1 = userList.get(0);
            GroupList.DataBean.GroupListBean.UserListBean bean2 = userList.get(1);
            tvCrew1.setVisibility(View.VISIBLE);
            tvCrew1.setText(bean2.getUserName());
            tvCrew2.setVisibility(View.INVISIBLE);
            tvCrew3.setVisibility(View.INVISIBLE);
            tvCrew4.setVisibility(View.INVISIBLE);
            tvCrew5.setVisibility(View.INVISIBLE);
            tvCrew6.setVisibility(View.INVISIBLE);
        }else if (userList.size()==3){
            GroupList.DataBean.GroupListBean.UserListBean bean1 = userList.get(0);
            GroupList.DataBean.GroupListBean.UserListBean bean2 = userList.get(1);
            GroupList.DataBean.GroupListBean.UserListBean bean3 = userList.get(2);
            tvCrew1.setVisibility(View.VISIBLE);
            tvCrew1.setText(bean2.getUserName());
            tvCrew2.setVisibility(View.VISIBLE);
            tvCrew2.setText(bean3.getUserName());
            tvCrew3.setVisibility(View.INVISIBLE);
            tvCrew4.setVisibility(View.INVISIBLE);
            tvCrew5.setVisibility(View.INVISIBLE);
            tvCrew6.setVisibility(View.INVISIBLE);
        }else if (userList.size()==4){
            GroupList.DataBean.GroupListBean.UserListBean bean1 = userList.get(0);
            GroupList.DataBean.GroupListBean.UserListBean bean2 = userList.get(1);
            GroupList.DataBean.GroupListBean.UserListBean bean3 = userList.get(2);
            GroupList.DataBean.GroupListBean.UserListBean bean4 = userList.get(3);
            tvCrew1.setVisibility(View.VISIBLE);
            tvCrew1.setText(bean2.getUserName());
            tvCrew2.setVisibility(View.VISIBLE);
            tvCrew2.setText(bean3.getUserName());
            tvCrew3.setVisibility(View.VISIBLE);
            tvCrew3.setText(bean4.getUserName());
            tvCrew4.setVisibility(View.INVISIBLE);
            tvCrew5.setVisibility(View.INVISIBLE);
            tvCrew6.setVisibility(View.INVISIBLE);
        }else if (userList.size()==5){
            GroupList.DataBean.GroupListBean.UserListBean bean1 = userList.get(0);
            GroupList.DataBean.GroupListBean.UserListBean bean2 = userList.get(1);
            GroupList.DataBean.GroupListBean.UserListBean bean3 = userList.get(2);
            GroupList.DataBean.GroupListBean.UserListBean bean4 = userList.get(3);
            GroupList.DataBean.GroupListBean.UserListBean bean5 = userList.get(4);
            tvCrew1.setVisibility(View.VISIBLE);
            tvCrew1.setText(bean2.getUserName());
            tvCrew2.setVisibility(View.VISIBLE);
            tvCrew2.setText(bean3.getUserName());
            tvCrew3.setVisibility(View.VISIBLE);
            tvCrew3.setText(bean4.getUserName());
            tvCrew4.setVisibility(View.VISIBLE);
            tvCrew4.setText(bean5.getUserName());
            tvCrew5.setVisibility(View.INVISIBLE);
            tvCrew6.setVisibility(View.INVISIBLE);
        }else {
            tvCrew1.setVisibility(View.INVISIBLE);
            tvCrew2.setVisibility(View.INVISIBLE);
            tvCrew3.setVisibility(View.INVISIBLE);
            tvCrew4.setVisibility(View.INVISIBLE);
            tvCrew5.setVisibility(View.INVISIBLE);
            tvCrew6.setVisibility(View.INVISIBLE);
        }

//        int nextInt = random.nextInt(4);
//        if (nextInt==0){
//            tvGroupName.setBackgroundResource(R.drawable.admin_task_group_name_bg);
//        }else if (nextInt==1){
//            tvGroupName.setBackgroundResource(R.drawable.admin_task_group_lanse);
//        }else if (nextInt==2){
//            tvGroupName.setBackgroundResource(R.drawable.admin_task_group_zise);
//        }else{
//            tvGroupName.setBackgroundResource(R.drawable.admin_task_group_chengse);
//        }
    }
}
