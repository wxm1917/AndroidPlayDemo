package com.qiyi.openapi.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qiyi.apilib.utils.LogUtils;
import com.qiyi.apilib.utils.StringUtils;
import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.view.YouTubePlayEffect;
import com.qiyi.video.playcore.ErrorCode;
import com.qiyi.video.playcore.IQYPlayerHandlerCallBack;
import com.qiyi.video.playcore.QiyiVideoView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhouxiaming on 2017/5/9.
 */

public class PlayerActivity extends BaseActivity implements YouTubePlayEffect.Callback {
    private static final int PERMISSION_REQUEST_CODE = 7171;
    private static final String TAG = PlayerActivity.class.getSimpleName() + "test";

    private static final int HANDLER_MSG_UPDATE_PROGRESS = 1;
    private static final int HANDLER_DEPLAY_UPDATE_PROGRESS = 1000; // 1s
    // 隐藏播放器进度条和播放按钮等控制组件
    private static final int HANDLER_MSG_HIDE_VIDEO_CONTROL_VIEW = 2;
    private static final int HANDLER_DELAY_HIDE_VIDEO_CONTROL_VIEW = 5 * 1000;

    private QiyiVideoView mVideoView;
    private SeekBar mSeekBar;
    private ImageButton mPlayPauseBtn;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    // youtube效果组件
    private YouTubePlayEffect mYouTubePlayEffect;
    // youtube效果组件是否显示的标志
    private boolean mYouTubePlayEffectShowFlag;
    // 播放器进度条和播放按钮等控制组件是否显示的标志
    private boolean mVideoViewControlViewFlag;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        String aid = getIntent().getStringExtra("aid");
        String tid = getIntent().getStringExtra("tid");
        if (StringUtils.isEmpty(tid)) {
            finish();
            return;
        }
        mVideoView = (QiyiVideoView) findViewById(R.id.id_videoview);
        //mVideoView.setPlayData("667737400");
        mVideoView.setPlayData(tid);
        //设置回调，监听播放器状态
        setPlayerCallback();

        mCurrentTime = (TextView) findViewById(R.id.id_current_time);
        mTotalTime = (TextView) findViewById(R.id.id_total_time);

        mPlayPauseBtn = (ImageButton) findViewById(R.id.id_playPause);
        mPlayPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYouTubePlayEffect.show();

                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
                } else {
                    mVideoView.start();
                    mPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
                }
            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.id_progress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.e(TAG, "onProgressChanged, progress = " + progress + ", fromUser = " + fromUser);
                if(fromUser) {
                    mProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBar.setProgress(mProgress);
                mVideoView.seekTo(mProgress);
            }
        });

        mYouTubePlayEffect = (YouTubePlayEffect) findViewById(R.id.youtube_effect);
        mYouTubePlayEffect.setCallback(this);

    }

    private void setPlayerCallback() {
        mVideoView.setPlayerCallBack(mCallBack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYouTubePlayEffectShowFlag = false;
        mVideoViewControlViewFlag = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e(TAG, "onStart");
        if (null != mVideoView) {
            mVideoView.start();
        }
        mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
        mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_HIDE_VIDEO_CONTROL_VIEW, HANDLER_DELAY_HIDE_VIDEO_CONTROL_VIEW);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop");
        if (null != mVideoView) {
            mVideoView.pause();
        }
        mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");
        mMainHandler.removeCallbacksAndMessages(null);
        mVideoView.release();
        mVideoView = null;
    }

    /**
     * Query and update the play progress every 1 second.
     */
    private Handler mMainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            LogUtils.e(TAG, "handleMessage, msg.what = " + msg.what);
            switch (msg.what) {
                case HANDLER_MSG_UPDATE_PROGRESS:
                    if (!mYouTubePlayEffectShowFlag) {
                        mYouTubePlayEffect.show();
                        mYouTubePlayEffectShowFlag = true;
                    }

                    int duration = mVideoView.getDuration();
                    int progress = mVideoView.getCurrentPosition();
                    LogUtils.e(TAG, "HANDLER_MSG_UPDATE_PROGRESS, duration = " + duration + ", currentPosition = " + progress);
                    if (duration > 0) {
                        mSeekBar.setMax(duration);
                        mSeekBar.setProgress(progress);

                        mTotalTime.setText(ms2hms(duration));
                        mCurrentTime.setText(ms2hms(progress));
                    }
                    mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
                    break;
                case HANDLER_MSG_HIDE_VIDEO_CONTROL_VIEW:
                    mSeekBar.setVisibility(View.GONE);
                    mPlayPauseBtn.setVisibility(View.GONE);
                    mVideoViewControlViewFlag = false;
                default:
                    break;
            }
        }
    };

    /**
     * Convert ms to hh:mm:ss
     * @param millis
     * @return
     */
    private String ms2hms(int millis) {
        String result = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return result;
    }

    IQYPlayerHandlerCallBack mCallBack = new IQYPlayerHandlerCallBack() {
        /**
         * SeekTo 成功，可以通过该回调获取当前准确时间点。
         */
        @Override
        public void OnSeekSuccess(long l) {
            LogUtils.e(TAG, "OnSeekSuccess: " + l);
        }

        /**
         * 是否因数据加载而暂停播放
         */
        @Override
        public void OnWaiting(boolean b) {
            LogUtils.e(TAG, "OnWaiting: " + b);
        }

        /**
         * 播放内核发生错误
         */
        @Override
        public void OnError(ErrorCode errorCode) {
            LogUtils.e(TAG, "OnError: " + errorCode);
            mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
        }

        /**
         * 播放器状态码 {@link com.iqiyi.player.nativemediaplayer.MediaPlayerState}
         * 0	空闲状态
         * 1	已经初始化
         * 2	调用PrepareMovie，但还没有进入播放
         * 4    可以获取视频信息（比如时长等）
         * 8    广告播放中
         * 16   正片播放中
         * 32	一个影片播放结束
         * 64	错误
         * 128	播放结束（没有连播）
         */
        @Override
        public void OnPlayerStateChanged(int i) {
            LogUtils.e(TAG, "OnPlayerStateChanged: " + i);
        }
    };

    @Override
    public void onDisappear(int direct) {
        mMainHandler.removeCallbacksAndMessages(null);
        mVideoView.release();
        mVideoView = null;
    }

    @Override
    public void onClick() {
        if (mVideoViewControlViewFlag) {
            mSeekBar.setVisibility(View.GONE);
            mPlayPauseBtn.setVisibility(View.GONE);
            mVideoViewControlViewFlag = false;
        } else {
            mSeekBar.setVisibility(View.VISIBLE);
            mPlayPauseBtn.setVisibility(View.VISIBLE);
            mVideoViewControlViewFlag = true;
        }
    }
}
