package com.qiyi.openapi.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiyi.openapi.demo.activity.VideoRecorderActivity;
import com.qiyi.openapi.demo.fragment.ARFragment;
import com.qiyi.openapi.demo.fragment.NotificationFragment;
import com.qiyi.openapi.demo.fragment.RecommendFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment[] mFragments;
    private RecommendFragment mRecommendFragment;
    private ARFragment mARFragment;
    private NotificationFragment mNotificationFragment;

    private ImageView[] mImageViews;
    private TextView[] mTextViews;

    private int mIndex;
    private int mCurrentTabIndex; // 当前Fragment的index


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabView();
    }

    private void initTabView() {
        // 初始化Fragment
        mRecommendFragment = RecommendFragment.newInstance();
        mARFragment = new ARFragment(); // ARFragment实际上并没有使用
        mNotificationFragment = NotificationFragment.newInstance();
        mFragments = new Fragment[]{ mRecommendFragment, mARFragment, mNotificationFragment };

        // 初始化ImageView
        mImageViews = new ImageView[3];
        mImageViews[0] = (ImageView) findViewById(R.id.ib_recommend);
        mImageViews[1] = (ImageView) findViewById(R.id.ib_ar);
        mImageViews[2] = (ImageView) findViewById(R.id.ib_notification);
        mImageViews[0].setSelected(true);

        // 初始化TextView
        mTextViews = new TextView[3];
        mTextViews[0] = (TextView) findViewById(R.id.tv_recommend);
        mTextViews[1] = (TextView) findViewById(R.id.tv_ar);
        mTextViews[2] = (TextView) findViewById(R.id.tv_notification);

        // 添加所有Fragment并显示第一个Fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mRecommendFragment)
                .add(R.id.fragment_container, mARFragment)
                .add(R.id.fragment_container, mNotificationFragment)
                .hide(mARFragment).hide(mNotificationFragment)
                .show(mRecommendFragment).commit();
    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.re_recommend:
                mIndex = 0;
                break;
            case R.id.re_ar:
                mIndex = 1;
                Intent videoRecorderIntent = new Intent(MainActivity.this, VideoRecorderActivity.class);
                startActivity(videoRecorderIntent);
                return;
            case R.id.re_notification:
                mIndex = 2;
                break;
        }
        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[mIndex].isAdded()) {
                trx.add(R.id.fragment_container, mFragments[mIndex]);
            }
            trx.show(mFragments[mIndex]).commit();
        }
        mImageViews[mCurrentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mImageViews[mIndex].setSelected(true);
        mTextViews[mCurrentTabIndex].setTextColor(0xFF999999);
        mTextViews[mIndex].setTextColor(0xFF45C01A);
        mCurrentTabIndex = mIndex;
    }

}
