package com.cango.palmcartreasure.trailer.main;

import android.os.Bundle;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseLazyFragment;

import butterknife.BindView;

/**
 * Created by cango on 2017/4/6.
 */

public class VpItemLazyFragment extends BaseLazyFragment {
    private static final String ID="id";
    private static final String NAME="name";

    @BindView(R.id.tv_vp_item_id)
    TextView tvId;
    @BindView(R.id.tv_vp_item_name)
    TextView tvName;
    public static VpItemLazyFragment newInstance(String id,String name) {
        VpItemLazyFragment fragment = new VpItemLazyFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ID, id);
        arguments.putString(NAME,name);
        fragment.setArguments(arguments);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.trailer_vp_item;
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        String id = (String) arguments.get(ID);
        String name= (String) arguments.get(NAME);
        tvId.setText(id);
        tvName.setText(name);
    }
}
