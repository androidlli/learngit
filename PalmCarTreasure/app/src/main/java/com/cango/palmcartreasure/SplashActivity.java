package com.cango.palmcartreasure;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.login.LoginActivity;
import com.cango.palmcartreasure.trailer.admin.AdminActivity;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView ivSplash = (ImageView) findViewById(R.id.iv_splash);
        Glide.with(this).load(R.drawable.botanic_gardens).into(ivSplash);
        int anInt = MtApplication.mSPUtils.getInt(Api.USERROLEID);
        if (anInt!=-1){
            if (anInt==Api.ADMIN_CODE){
                startDelay(AdminActivity.class);
            }else {
                startDelay(TrailerActivity.class);
            }
        }else {
            startDelay(LoginActivity.class);
        }
    }
    private void startDelay(final Class cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, cls);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
