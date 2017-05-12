package com.cango.palmcartreasure;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.cango.palmcartreasure.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by cango on 2017/3/31.
 */

public class MtApplication extends Application {
    private static Context mContext;
    public static SPUtils mSPUtils;
    public static List<AppCompatActivity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mSPUtils = new SPUtils("cango_vps");
        BGASwipeBackManager.getInstance().init(this);
//        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void addActivity(AppCompatActivity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
    }

    public static void clearActivitys() {
        if (activityList == null||activityList.size()==0) {

        } else {
            for (AppCompatActivity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        }
    }
    public static void clearExceptLastActivitys() {
        if (activityList == null||activityList.size()==0) {

        } else {
            for (int i=0;i<activityList.size()-1;i++){
                if (activityList.get(i)!=null){
                    activityList.get(i).finish();
                }
            }
        }
    }
}
