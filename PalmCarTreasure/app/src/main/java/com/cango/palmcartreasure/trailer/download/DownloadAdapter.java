package com.cango.palmcartreasure.trailer.download;

import android.content.Context;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public class DownloadAdapter extends BaseAdapter<String> {
    public DownloadAdapter(Context context, List<String> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.download_item;
    }

    @Override
    protected void convert(BaseHolder holder, String data) {

    }
}
