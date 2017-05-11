package com.cango.palmcartreasure.trailer.message;

import android.content.Context;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public class MessageAdapter extends BaseAdapter<String> {
    public MessageAdapter(Context context, List<String> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.message_item;
    }

    @Override
    protected void convert(BaseHolder holder, String data) {

    }
}
