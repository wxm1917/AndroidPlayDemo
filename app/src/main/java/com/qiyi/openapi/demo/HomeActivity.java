package com.qiyi.openapi.demo;

import android.support.v4.app.FragmentTransaction;

import com.qiyi.openapi.demo.activity.BaseActivity;
import com.qiyi.openapi.demo.fragment.RecommendFragment;

/**
 * Created by zhouxiaming on 2017/5/9.
 */

public class HomeActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        super.initView();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, RecommendFragment.newInstance()).commitAllowingStateLoss();
    }
}
