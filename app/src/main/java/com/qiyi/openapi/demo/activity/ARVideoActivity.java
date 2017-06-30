package com.qiyi.openapi.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.fragment.ARFragment;
import com.qiyi.openapi.demo.util.UnitySplashSDK;
import com.unity3d.player.UnityPlayer;

public class ARVideoActivity extends Activity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private RelativeLayout mCloseLayout;
    private ImageView mCloseImageView;

    private static ARVideoActivity mActivity;

    /**
     * 初始化视图
     */
    private void initView(){
        // 获取显示unity视图的父控件
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.id_layout_unity);
        // 获取unity视图
        mUnityPlayer = new UnityPlayer(this);
        View view = mUnityPlayer.getView();
        // 将unity视图添加到Android视图中
        parent.addView(view);
        UnitySplashSDK.getInstance().onCreate(mActivity);

        mCloseLayout = (RelativeLayout) findViewById(R.id.id_btn_close);
        mCloseLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                finish();
            }
        });

        mCloseImageView = (ImageView) findViewById(R.id.id_iv_close);
    }

    /**
     * 暴露给unity调用的方法
     */
    public void hideSplash(){
        UnitySplashSDK.getInstance().onHideSplash();
    }

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mActivity = this;

//        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
//
//        mUnityPlayer = new UnityPlayer(this);
//        setContentView(mUnityPlayer);
//        mUnityPlayer.requestFocus();

        setContentView(R.layout.activity_unity_player);
        getWindow().setFormat(PixelFormat.RGBX_8888);
        initView();
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
//        mCloseImageView.setVisibility(View.VISIBLE);
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            mUnityPlayer.quit();
            finish();
            return true;
        }
        return mUnityPlayer.injectEvent(event);
//        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    public UnityPlayer getUnityPlayer(){
        return mUnityPlayer;
    }
}
