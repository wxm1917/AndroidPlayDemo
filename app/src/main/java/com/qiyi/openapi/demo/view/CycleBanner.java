package com.qiyi.openapi.demo.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.qiyi.apilib.ApiLib;
import com.qiyi.apilib.model.VideoInfo;
import com.qiyi.openapi.demo.QYPlayerUtils;
import com.qiyi.openapi.demo.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by AdamLi on 2016/5/30.
 */
public class CycleBanner {
    ViewPager mViewPager;
    LinearLayout mIndicatorContainer;
    LayoutInflater mLayoutInflater;
    CyclePagerAdapter mPagerAdapter;
    Stack<View> stackView = new Stack<View>();
    List<VideoInfo> mData = new ArrayList<>();
    List<ImageView> mIndicators = new ArrayList<>();
    private int mCount = 0;

    WeakReference<Context> mWeakContext;

    ScheduledExecutorService mScheduledExecutorService;
    int mCurrentPos;

    boolean mCanAutoCycle = true;

    public CycleBanner(Context context, View rootView) {
        mWeakContext = new WeakReference<>(context);
        mLayoutInflater = LayoutInflater.from(context);
        mViewPager = (ViewPager) rootView.findViewById(R.id.recommend_focus_vp);
        calculateViewPageSize();
        mIndicatorContainer = (LinearLayout) rootView.findViewById(R.id.recommend_focus_ll);
        mPagerAdapter = new CyclePagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    /**
     * 根据焦点图的大小来计算ViewPage的大小，目前焦点图大小是 640*316
     */
    private void calculateViewPageSize() {
        int screenWidth = ApiLib.CONTEXT.getResources().getDisplayMetrics().widthPixels - ApiLib.CONTEXT.getResources().getDimensionPixelSize(R.dimen.video_card_margin_margin_horizontal) * 2 * 2;
        int viewPagerHeight = (int)((float)316.0f / (float) 640 * screenWidth);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mViewPager.getLayoutParams();
        layoutParams.height = viewPagerHeight;
        mViewPager.setLayoutParams(layoutParams);

    }


    public void setData(List<VideoInfo> data) {
        if (null == data || 0 == data.size()) {
            return;
        }
        stop();
        mData.clear();
        mData.addAll(data);
        int count = data.size();
        if(count != mCount) {
            mCount = count;
            initIndicators();
            mViewPager.setCurrentItem(200 * mCount);
        }
        mPagerAdapter.notifyDataSetChanged();
        start();
    }

    private void initIndicators() {
        Context context = mWeakContext.get();
        if (context != null) {
            mIndicators.clear();
            mIndicatorContainer.removeAllViews();
            for (int i = 0; i < mCount; i++) {
                ImageView imageView = new ImageView(context);
                if (0 == i) {
                    imageView.setBackgroundResource(R.drawable.circle_dot_focus);
                } else {
                    imageView.setBackgroundResource(R.drawable.circle_dot_normal);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(dip2px(context, 5), 0, dip2px(context, 5), 0);
                mIndicators.add(imageView);
                mIndicatorContainer.addView(imageView, layoutParams);
            }
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class CyclePagerAdapter extends PagerAdapter implements View.OnClickListener{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            stackView.push((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int realPosition = position % mCount;
            String imgUrl = mData.get(realPosition).img;
            View focusView = null;
            ImageView focusImageView = null;
            if (!stackView.isEmpty()) {
                focusView = stackView.pop();
            }

            if (focusView == null) {
                focusView = mLayoutInflater.inflate(R.layout.focus_viewpager_page_item, container, false);
            }

            focusImageView = (ImageView) focusView.findViewById(R.id.focus_image);
            focusView.setTag(R.id.tag_key, mData.get(realPosition));
            Glide.clear(focusImageView); //清除缓存
            Glide.with(ApiLib.CONTEXT).load(imgUrl).animate(R.anim.alpha_on).into(focusImageView);
            focusView.setOnClickListener(this);
            container.addView(focusView);
            return focusView;
        }

        @Override
        public void onClick(View v) {
            Context context = mWeakContext.get();
            if(null == context || ((Activity)context).isFinishing()){
                stop();
                return;
            }
            VideoInfo videoInfo = (VideoInfo)v.getTag(R.id.tag_key);
            QYPlayerUtils.jumpToPlayerActivity(context, videoInfo.aId, videoInfo.tId);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPos = position;
            setIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    mCanAutoCycle = true;
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    mCanAutoCycle = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    mCanAutoCycle = true;
                    break;
            }
        }
    };

    private void setIndicator(int pos){
        int realPos = pos %mCount;
        for(int i=0;i<mCount;i++){
            if(realPos == i){
                mIndicators.get(i).setBackgroundResource(R.drawable.circle_dot_focus);
            }else{
                mIndicators.get(i).setBackgroundResource(R.drawable.circle_dot_normal);
            }
        }
    }

    private void start(){
        if(null == mScheduledExecutorService || mScheduledExecutorService.isShutdown()){
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if(mCanAutoCycle) {
                        mCurrentPos = mCurrentPos + 1;
                        mUIHandler.sendEmptyMessage(0);
                    }
                }
            }, 3, 3, TimeUnit.SECONDS);
        }
    }

    private void stop(){
        if(mScheduledExecutorService != null){
            mScheduledExecutorService.shutdown();
        }
    }

    private Handler mUIHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            Context context = mWeakContext.get();
            if(null == context || ((Activity)context).isFinishing()){
                stop();
                return;
            }
            mViewPager.setCurrentItem(mCurrentPos);
        }
    };
}
