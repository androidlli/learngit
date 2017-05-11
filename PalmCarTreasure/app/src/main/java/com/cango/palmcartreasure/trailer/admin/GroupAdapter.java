package com.cango.palmcartreasure.trailer.admin;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.Member;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cango on 2017/5/4.
 */

public class GroupAdapter extends BaseAdapter<Member> {
    /**
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private int type;

    public void setOnSelectCountListener(OnSelectCountListener onSelectCountListener) {
        this.onSelectCountListener = onSelectCountListener;
    }

    private OnSelectCountListener onSelectCountListener;

    public GroupAdapter(Context context, List<Member> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.group_member_item;
    }

    @Override
    protected void convert(BaseHolder holder, final Member data) {
        final TextView tvName = holder.getView(R.id.tv_item);
        tvName.setText(data.getName());

        if (type == 0) {
            //选择组长只能选择一个
            if (data.isGroupLeader()) {
                tvName.setBackgroundResource(R.drawable.member_item_selected_bg);
//                tvName.setBackgroundResource(R.drawable.button_press_bg);
            } else {
                tvName.setBackgroundResource(R.drawable.member_item_normal_bg);
            }
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.isGroupLeader()) {
                        data.setGroupLeader(false);
                        tvName.setBackgroundResource(R.drawable.member_item_normal_bg);
                    } else {
                        int position = getGroupLeaderPosition();
                        if (position == -1) {

                        } else {
                            mDatas.get(position).setGroupLeader(false);
                        }
                        data.setGroupLeader(true);
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            if (data.isSelected()) {
                tvName.setBackgroundResource(R.drawable.member_item_selected_bg);
            } else {
                tvName.setBackgroundResource(R.drawable.member_item_normal_bg);
            }
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectSize = getSelectSize();
                    if (data.isSelected()) {
                        tvName.setBackgroundResource(R.drawable.member_item_normal_bg);
                        data.setSelected(!data.isSelected());

                    } else {
                        if (selectSize == 3) {
                            tvName.setBackgroundResource(R.drawable.member_item_selected_bg);
                            data.setSelected(!data.isSelected());
                            checkSelectCount();
                        } else if (selectSize < 3) {
                            tvName.setBackgroundResource(R.drawable.member_item_selected_bg);
                            data.setSelected(!data.isSelected());
                        } else {

                        }
                    }
                }
            });
        }

    }

    private int getGroupLeaderPosition() {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).isGroupLeader()) {
                return i;
            }
        }
        return -1;
    }

    private int getSelectSize() {
        int selectedSize = 0;
        for (Member member : mDatas) {
            if (member.isSelected()) {
                selectedSize++;
            }
        }
        return selectedSize;
    }

    private void checkSelectCount() {
        int selectedSize = 0;
        for (Member member : mDatas) {
            if (member.isSelected()) {
                selectedSize++;
            }
        }
        if (selectedSize > 0) {
            List<Member> members = new ArrayList<>();
            for (Member member : mDatas) {
                if (member.isSelected()) {
                    members.add(member);
                }
            }
            onSelectCountListener.onSelectCount(members, selectedSize);
        }
    }

    public interface OnSelectCountListener {
        void onSelectCount(List<Member> members, int size);
    }
}
