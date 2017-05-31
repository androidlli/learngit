package com.cango.palmcartreasure;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.cango.palmcartreasure.model.TrailerEvent;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.trailer.message.MessageActivity;
import com.cango.palmcartreasure.trailer.message.MessageFragment;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SPUtils;
import com.orhanobut.logger.Logger;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

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
        PushAgent mPushAgent = PushAgent.getInstance(this);
//        Logger.init("LLI").logLevel(LogLevel.NONE);
        mContext = getApplicationContext();
        mSPUtils = new SPUtils("cango_vps");
        BGASwipeBackManager.getInstance().init(this);
//        AutoLayoutConifg.getInstance().useDeviceSize();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Logger.d(msg.custom);
                //推送消息关联消息列表
                AppCompatActivity lastActvity = getLastActvity();
                if (lastActvity != null) {
                    if (lastActvity instanceof MessageActivity) {
                        MessageFragment fragment = (MessageFragment) lastActvity.getSupportFragmentManager().findFragmentById(R.id.fl_message_contains);
                        if (!CommUtil.checkIsNull(fragment))
                            fragment.onRefresh();
                    }else {
                        Intent startTaobao = new Intent(context, MessageActivity.class);
                        startTaobao.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(startTaobao);
                    }
                } else {
                    Intent startTaobao = new Intent(context, MessageActivity.class);
                    startTaobao.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startTaobao);
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                Logger.d(uMessage.text);
                AppCompatActivity lastActvity = getLastActvity();
                if (lastActvity != null) {
                    if (lastActvity instanceof MessageActivity) {
                        MessageFragment fragment = (MessageFragment) lastActvity.getSupportFragmentManager().findFragmentById(R.id.fl_message_contains);
                        if (!CommUtil.checkIsNull(fragment))
                            fragment.onRefresh();
                    }
                }

                //推送消息关联主页
                boolean hasTrailerActivity = isHasTrailerActivity();
                if (hasTrailerActivity){
                    EventBus.getDefault().post(new TrailerEvent("ok"));
                }

                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.d("deviceToken" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
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
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (AppCompatActivity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        }
    }

    public static void clearLastActivity(){
        if (activityList == null || activityList.size() == 0) {

        } else {
          activityList.remove(activityList.size()-1);
        }
    }
    public static void clearExceptLastActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (int i = 0; i < activityList.size() - 1; i++) {
                if (activityList.get(i) != null) {
                    activityList.get(i).finish();
                }
            }
            AppCompatActivity activity = activityList.get(activityList.size() - 1);
            activityList.clear();
            activityList.add(activity);
        }
    }

    private static AppCompatActivity getLastActvity() {
        if (activityList == null || activityList.size() == 0) {
            return null;
        } else {
            return activityList.get(activityList.size() - 1);
        }
    }
    private static boolean isHasTrailerActivity(){
        boolean isHas = false;
        if (activityList == null || activityList.size() == 0) {
            isHas=false;
        } else {
            for (int i = 0; i < activityList.size(); i++) {
                AppCompatActivity activity = activityList.get(i);
                if (activity instanceof TrailerActivity){
                    isHas=true;
                    break;
                }
            }
        }
        return isHas;
    }
}
