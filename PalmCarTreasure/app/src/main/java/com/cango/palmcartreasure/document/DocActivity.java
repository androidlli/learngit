package com.cango.palmcartreasure.document;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.sms.SMSFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;

import butterknife.BindView;

public class DocActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        DocFragment docFragment = (DocFragment) getSupportFragmentManager().findFragmentById(R.id.fl_doc_contains);
        if (CommUtil.checkIsNull(docFragment)) {
            docFragment = DocFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_doc_contains, docFragment);
            transaction.commit();
        }
    }
}
