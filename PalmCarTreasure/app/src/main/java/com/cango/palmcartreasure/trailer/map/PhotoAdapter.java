package com.cango.palmcartreasure.trailer.map;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.SelectPhoto;

import java.io.File;
import java.util.List;

/**
 * Created by cango on 2017/5/9.
 */

public class PhotoAdapter extends BaseAdapter<SelectPhoto> {
    public PhotoAdapter(Context context, List<SelectPhoto> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.upload_item;
    }

    @Override
    protected void convert(BaseHolder holder, SelectPhoto data) {
        ImageView ivItem = holder.getView(R.id.iv_upload_item);
        if (data.isShowSelect()){
            ivItem.setImageResource(R.drawable.select_photo);
        }else {
            File photoFile=new File(data.getPhotoUrl());
            Glide.with(mContext)
                    .load(photoFile)
                    .into(ivItem);
        }
    }
}
