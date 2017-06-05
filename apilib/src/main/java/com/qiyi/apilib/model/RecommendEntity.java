package com.qiyi.apilib.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐页的实体类
 * Created by zhouxiaming on 2017/5/8.
 */

public class RecommendEntity extends BaseEntity {
    @SerializedName("data")
    public List<RecommendItem> recommendItemList = new ArrayList<RecommendItem>();

    /**
     * 推荐页每个频道数据
     */
    public class RecommendItem extends BaseEntity {
        @SerializedName("title")
        public String title;

        @SerializedName("channel_id")
        public String channelId;

        @SerializedName("channel_name")
        public String channelName;

        @SerializedName("video_list")
        public List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
    }
}
