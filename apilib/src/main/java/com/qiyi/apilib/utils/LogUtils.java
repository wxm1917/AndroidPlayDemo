package com.qiyi.apilib.utils;

import android.util.Log;

import com.qiyi.apilib.ApiConstant;


/**
 * Created by zhouxiaming on 2015/5/5.
 */

public class LogUtils {
    private static String TAG = "LogUtils";
    public static boolean isDebug = ApiConstant.IS_DEBUG;
    public static void e(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.e(tag == null? TAG : tag, msg);
        }
    }

    public static void i(String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.i(tag == null? TAG : tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.w(tag == null? TAG : tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (ApiConstant.IS_DEBUG) {
            Log.d(tag == null? TAG : tag, msg);
        }
    }
}
