package com.qiyi.openapi.demo.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.activity.ARVideoActivity;
import com.unity3d.player.UnityPlayer;

/**
 * Created by Bruce on 2017/6/29.
 * 修改Unity启动画面
 */

public class UnitySplashSDK {
    private LinearLayout mSplashLayout = null;
    private ImageView mSplashView = null;
    private AnimationDrawable mSplashAnimation = null;
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
        if (mSplashView != null){
            return;
        }

        try {
            Resources r = mUnityPlayer.currentActivity.getResources();

            // 父Layout
            mSplashLayout = new LinearLayout(UnityPlayer.currentActivity);
            mSplashLayout.setBackgroundColor(Color.parseColor("#000000"));
            mSplashLayout.setGravity(Gravity.CENTER);

            // Splash动画
            mSplashView = new ImageView(UnityPlayer.currentActivity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            mSplashView.setLayoutParams(params);
            mSplashView.setBackgroundDrawable(r.getDrawable(R.drawable.splash_ar_video));
            mSplashView.setScaleType(ImageView.ScaleType.CENTER);

            // 添加View
            mUnityPlayer.addView(mSplashLayout, r.getDisplayMetrics().widthPixels, r.getDisplayMetrics().heightPixels);
            mSplashLayout.addView(mSplashView);
            // 开启动画
            mSplashAnimation = (AnimationDrawable) mSplashView.getBackground();
            mSplashAnimation.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在unity的Activity中暴露方法给unity，让unity在准备好内容后调用隐藏启动画面
     */
    public void onHideSplash() {
        try {
            if (mSplashLayout == null){
                return;
            }

            UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    mSplashAnimation.stop();
                    mUnityPlayer.removeView(mSplashLayout);
                    mSplashLayout = null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
