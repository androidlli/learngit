package com.cango.palmcartreasure.trailer.download;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.trailer.message.MessageFragment;
import com.cango.palmcartreasure.trailer.message.MessagePresenter;
import com.cango.palmcartreasure.util.CommUtil;

public class DownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        DownloadFragment downloadFragment = (DownloadFragment) getSupportFragmentManager().findFragmentById(R.id.fl_download_contains);
        if (CommUtil.checkIsNull(downloadFragment)) {
            downloadFragment = DownloadFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_download_contains, downloadFragment);
            transaction.commit();
        }
        DownloadPresenter downloadPresenter=new DownloadPresenter(downloadFragment);
    }

}
