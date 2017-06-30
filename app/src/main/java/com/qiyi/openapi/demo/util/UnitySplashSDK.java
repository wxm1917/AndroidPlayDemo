package com.qiyi.openapi.demo.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.ImageView;

import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.activity.ARVideoActivity;
import com.unity3d.player.UnityPlayer;

/**
 * Created by Bruce on 2017/6/29.
 * 修改Unity启动画面
 */

public class UnitySplashSDK {
    // 这是启动画面的图片，这里可以使任意一个View，可以根据自己的需要去自定义
    private ImageView bgView = null;

    private UnityPlayer mUnityPlayer = null;

    private static UnitySplashSDK mInstance;

    public static UnitySplashSDK getInstance() {

        if (null == mInstance) {

            synchronized (UnitySplashSDK.class) {

                if (null == mInstance) {

                    mInstance = new UnitySplashSDK();

                }

            }

        }

        return mInstance;

    }

    /**
     * 在unity的Activity的onCreate中调用
     * @param activity
     */
    public void onCreate(Activity activity) {
        mUnityPlayer = ((ARVideoActivity) activity).getUnityPlayer();
        onShowSplash();
    }

    /**
     * splash界面
     */
    public void onShowSplash() {
        if (bgView != null){
            return;
        }

        try {
            // 设置启动内容，这里的内容可以自定义去修改
            bgView = new ImageView(UnityPlayer.currentActivity);
            bgView.setBackgroundColor(Color.parseColor("#E91E63"));
            bgView.setImageResource(R.drawable.splash);
            bgView.setScaleType(ImageView.ScaleType.CENTER);

            Resources r = mUnityPlayer.currentActivity.getResources();
            mUnityPlayer.addView(bgView, r.getDisplayMetrics().widthPixels, r.getDisplayMetrics().heightPixels);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在unity的Activity中暴露方法给unity，让unity在准备好内容后调用隐藏启动画面
     */
    public void onHideSplash() {
        try {
            if (bgView == null){
                return;
            }

            UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    mUnityPlayer.removeView(bgView);
                    bgView = null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
