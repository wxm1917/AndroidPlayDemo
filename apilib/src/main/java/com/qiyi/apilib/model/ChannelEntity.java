package com.qiyi.apilib.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity 实体对象都是针对服务器返回数据进行序列号而定义的，包含返回码等信息，方便反序列化
 * Created by zhouxiaming on 2017/5/5.
 */

public class ChannelEntity extends BaseEntity {
    @SerializedName("data")
    public List<Channel> channelList = new ArrayList<Channel>();

    /**
     * 频道信息
     * Created by zhouxiaming on 2017/5/5.
     */

    public static class Channel extends BaseEntity{
        public Channel(){

        }
        /**
         * 频道ID
         */
        @SerializedName("id")
        public String id;

        /**
         * 频道名称
         */
        @SerializedName("name")
        public String name;

        /**
         * 描述
         */
        @SerializedName("desc")
        public String describe;

    }
}
