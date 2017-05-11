package com.cango.palmcartreasure.baseAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cango.palmcartreasure.util.CommUtil;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by cango on 2017/3/20.
 */

public class BaseHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    /**
     * 构造方法
     *
     * @param itemView
     */
    public BaseHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        AutoUtils.autoSize(itemView);
        mViews = new SparseArray<>();
    }

    public static BaseHolder create(View itemView) {
        return new BaseHolder(itemView);
    }

    public static BaseHolder create(Context context,int layoutId,ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseHolder(view);
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        boolean checkIsNotNull = CommUtil.checkIsNull(view);
        if (checkIsNotNull){
            view = mConvertView.findViewById(viewId);
        }
        mViews.put(viewId,view);
        return (T) view;
    }
}
