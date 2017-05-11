package com.cango.palmcartreasure.util;

/**
 * Created by cango on 2017/3/15.
 */

public class CommUtil {
    public static <T> T checkNotNull(T reference,  Object errorMessage) {
        if(reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }
    public static <T> boolean checkIsNull(T reference){
        if (reference==null)
            return true;
        else
            return false;
    }

}
