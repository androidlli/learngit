package com.cango.palmcartreasure.trailer.message;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.MessageList;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public class MessageAdapter extends BaseAdapter<MessageList.DataBean.MessageListBean> {
    public MessageAdapter(Context context, List<MessageList.DataBean.MessageListBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.message_item;
    }

    @Override
    protected void convert(BaseHolder holder, MessageList.DataBean.MessageListBean data) {
        ImageView ivRed=holder.getView(R.id.iv_red);
        TextView tvDate = holder.getView(R.id.tv_message_item_date);
        TextView tvContent = holder.getView(R.id.tv_message_item_content);
        ImageView ivLine = holder.getView(R.id.iv_line);

        tvDate.setText(data.getSendTime());
        tvContent.setText(data.getMessageContent());

        //1-已发送 / 2-已读
        String status = data.getStatus();
        if ("1".equals(status)){
            ivRed.setVisibility(View.VISIBLE);
        }else {
            ivRed.setVisibility(View.GONE);
        }
//        if (data.isLastLine()){
//            ivLine.setVisibility(View.GONE);
//        }else {
//            ivLine.setVisibility(View.VISIBLE);
//        }
    }
}
