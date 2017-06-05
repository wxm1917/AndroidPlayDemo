package com.qiyi.openapi.demo.fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.qiyi.apilib.utils.LogUtils;
import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.activity.BaseActivity;

/**
 * Created by zhouxiaming on 17/5/5.
 */
public abstract class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";
    protected View mRootView;
    protected String mParam1;
    protected String mParam2;
    protected Activity mActivity;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    protected ProgressBar mLoadingView;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResourceId(), container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long begin = System.currentTimeMillis();
        loadData();
        long end = System.currentTimeMillis();
        LogUtils.i(TAG, "loaddata time : " + (end - begin));
    }

    protected abstract int getLayoutResourceId();
    protected void initView() {
        View mProgressBar = mRootView.findViewById(R.id.loading_bar);
        if (mProgressBar != null) {
            mLoadingView = (ProgressBar) mProgressBar;
        }

    }
    protected void loadData() {

    }

    protected void showLoadingBar() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoadingBar() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }



    protected Handler getWorkHandler() {
        if (mWorkThread == null) {
            mWorkThread = new HandlerThread(String.format("Work Thread: %s", TAG));
            mWorkThread.start();
            mWorkHandler = new Handler(mWorkThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    handleWorkThreadMessage(msg);
                }
            };
        }
        return mWorkHandler;
    }

    protected void handleWorkThreadMessage(Message msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacksAndMessages(null);
            mWorkHandler = null;
        }
        if (mWorkThread != null) {
            mWorkThread.quit();
            mWorkThread = null;
        }
    }

    protected void setActionBar(Toolbar paramToolbar)
    {
        if (mActivity instanceof BaseActivity) {
            BaseActivity localMainActivity = (BaseActivity)mActivity;
            localMainActivity.setSupportActionBar(paramToolbar);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public boolean onBackPress() {
        return true;
    }

    public void showWithAnimation() {

    }

    public void hideWithAnimation() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume  ========  ");
    }


    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
