package com.rename.qiyuan.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.rename.qiyuan.main.R;

public class NotificationFragment extends BaseFragment {

    private Toolbar mToolbar;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) mRootView.findViewById(R.id.fragment_toolbar);
        mToolbar.setTitle(R.string.notification_title);
        setActionBar(mToolbar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
