package com.rename.qiyuan.main;

import android.support.v4.app.FragmentTransaction;

import com.rename.qiyuan.main.R;
import com.rename.qiyuan.main.activity.BaseActivity;
import com.rename.qiyuan.main.fragment.RecommendFragment;

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
