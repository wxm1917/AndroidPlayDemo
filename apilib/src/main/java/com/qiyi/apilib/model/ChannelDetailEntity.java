package com.qiyi.apilib.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道详情页面，包括Video信息， TAG信息等
 * Created by zhouxiaming on 2017/5/5.
 */

public class ChannelDetailEntity extends BaseEntity {
    @SerializedName("data")
    public DataEntity dataEntity;

    /**
     * 详情数据对象
     */
    public class DataEntity extends BaseEntity {

        /**
         * 频道ID
         */
        @SerializedName("channelId")
        public String channelId;

        /**
         * 频道名称
         */
        @SerializedName("channelName")
        public String channelName;

        /**
         * Video总数
         */
        @SerializedName("total")
        public String total;

        /**
         * 频道的TAG信息列表
         */
        @SerializedName("tag_list")
        public List<Tag> tagList = new ArrayList<Tag>();

        /**
         * 视频信息列表
         */
        @SerializedName("video_list")
        public List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();

    }

    /**
     * 频道的TAG信息，包括地区，类型，规格，年份等信息
     */
    public class Tag extends BaseEntity {
        @SerializedName("id")
        public String tagId;
        @SerializedName("name")
        public String tagName;
        @SerializedName("name_tw")
        public String tagNameTW; //繁体
        @SerializedName("children")
        public List<TagChildren> tagChildrens = new ArrayList<TagChildren>();
    }

    public class TagChildren extends BaseEntity {
        @SerializedName("id")
        public String tagId;
        @SerializedName("name")
        public String tagName;
        @SerializedName("name_tw")
        public String tagNameTW; //繁体
    }
}
