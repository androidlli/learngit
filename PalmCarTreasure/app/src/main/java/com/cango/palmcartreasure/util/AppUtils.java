package com.cango.palmcartreasure.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by cango on 2017/4/25.
 */

public class AppUtils {

    public static boolean isHasBaiduMap(Context context){
        return isInstallApp(context,"com.baidu.BaiduMap");
    }
    public static boolean isHasGaodeMap(Context context){
        return isInstallApp(context,"com.autonavi.minimap");
    }
    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return !isSpace(packageName) && launchIntentForPackage != null;
    }
    private static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
