package com.cango.palmcartreasure.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;

/**
 * Created by cango on 2017/4/18.
 */

public class PhoneUtils {
    /**
     * 获取IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return IMEI码
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Logger.d(tm.getDeviceId());
        return tm != null ? tm.getDeviceId() : null;
    }
}
