package com.qiyi.openapi.demo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.qiyi.openapi.demo.R;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected View mRootView;
    protected ProgressBar mLoadingView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(getLayoutResourceId(), null, false);
        setContentView(mRootView);
        initView();
        loadData();
    }

    protected void initView() {
        View mProgressBar = mRootView.findViewById(R.id.loading_bar);
        if (mProgressBar != null) {
            mLoadingView = (ProgressBar) mProgressBar;
        }
    }

    protected void loadData() {

    }

    protected abstract int getLayoutResourceId();

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

}
