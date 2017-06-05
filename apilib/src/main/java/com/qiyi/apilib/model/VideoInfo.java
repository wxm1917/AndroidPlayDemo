package com.qiyi.apilib.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public class VideoInfo extends BaseEntity {
    //视频ID
    @SerializedName("id")
    public String id;

    //视频名称
    @SerializedName("title")
    public String title;

    //视频短名称
    @SerializedName("short_title")
    public String shortTitle;

    //视频封面
    @SerializedName("img")
    public String img;

    //视频评分
    @SerializedName("sns_score")
    public String snsScore;

    //播放次数:76443334
    @SerializedName("play_count")
    public String playCount;

    //播放次数:7644.3万
    @SerializedName("play_count_text")
    public String playCountText;

    //播放器播放的时候需要
    @SerializedName("a_id")
    public String aId;

    //播放器播放的时候需要
    @SerializedName("tv_id")
    public String tId;

    //是否是vip, 0:否, 1:是
    @SerializedName("is_vip")
    public String isVip;

    //视频类型 normal:普通视频, trailer: 预告
    @SerializedName("type")
    public String type;

    //	视频属性类型, 1: 单视频专辑, 2: 电视剧, 3: 综艺. 此字段可以用来判断视频是 电视剧还是综艺节目
    @SerializedName("p_type")
    public String pType;

    //视频发布时间的时间戳：1454860800000
    @SerializedName("date_timestamp")
    public String dateTimestamp;

    //格式化之后的视频发布时间,精确到哪天：2016-02-07
    @SerializedName("date_format")
    public String dateFormat;

    //此视频所属专辑一共有多少个视频
    @SerializedName("total_num")
    public String totalNum;

    //此视频所属专辑更新到了第几个视频
    @SerializedName("update_num")
    public String updateNum;

}
