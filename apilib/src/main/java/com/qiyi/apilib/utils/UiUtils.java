package com.qiyi.apilib.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public class UiUtils {
    /**
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getScreenWidthAndHeight(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int[] screen = new int[2];
        screen[0] = metrics.widthPixels;
        screen[1] = metrics.heightPixels;
        return screen;
    }


}
