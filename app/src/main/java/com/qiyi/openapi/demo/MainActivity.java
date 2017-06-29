package com.qiyi.openapi.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiyi.apilib.utils.LogUtils;
import com.qiyi.openapi.demo.activity.BaseActivity;
import com.qiyi.openapi.demo.activity.VideoRecorderActivity;
import com.qiyi.openapi.demo.common.Constants;
import com.qiyi.openapi.demo.fragment.ARFragment;
import com.qiyi.openapi.demo.fragment.NotificationFragment;
import com.qiyi.openapi.demo.fragment.RecommendFragment;
import com.qiyi.openapi.demo.util.PermissionUtils;
import com.qiyi.openapi.demo.util.Utils;
import com.qiyi.openapi.demo.view.YesOrNoDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Fragment[] mFragments;
    private RecommendFragment mRecommendFragment;
    private ARFragment mARFragment;
    private NotificationFragment mNotificationFragment;

    private ImageView[] mImageViews;
    private TextView[] mTextViews;

    private int mIndex;
    private int mCurrentTabIndex; // 当前Fragment的index

    private List<String> permissionList = new ArrayList<String>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        initTabView();
    }

    @Override
    protected void loadData() {
        super.loadData();
//        Constants.SCRRENWIDTH = Utils.getInstance(MainActivity.this).getWidthPixels();
//        Constants.SCREENHEIGHT = Utils.getInstance(MainActivity.this).getHeightPixels();
//        permissionList.add(Manifest.permission.CAMERA);
//        permissionList.add(Manifest.permission.RECORD_AUDIO);
//        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        PermissionUtils.checkAndRequestPermissions(permissionList, MainActivity.this);
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

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
//                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    Intent videoRecorderIntent = new Intent(MainActivity.this, VideoRecorderActivity.class);
                    startActivity(videoRecorderIntent);
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.re_recommend:
                mIndex = 0;
                break;
            case R.id.re_ar:
                PermissionUtils.requestPermission(MainActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
//                mIndex = 1;
//                Intent videoRecorderIntent = new Intent(MainActivity.this, VideoRecorderActivity.class);
//                startActivity(videoRecorderIntent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults, mPermissionGrant);
    }
}
