package com.qiyi.apilib.service;


import com.qiyi.apilib.model.ChannelDetailEntity;
import com.qiyi.apilib.model.ChannelEntity;
import com.qiyi.apilib.model.RecommendEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public interface ApiService {
    /**
     * 获取频道列表接口
     * @param params
     * @return
     */
    @GET("channel")
    Observable<ChannelEntity> qiyiChannelList(@QueryMap Map<String, String> params);

    /**
     * 获取频道详情接口
     * @param params
     * @return
     */
    @GET("channel")
    Observable<ChannelDetailEntity> qiyiChannelDetail(@QueryMap Map<String, String> params);

    /**
     * 获取推荐页数据接口
     * @param params
     * @return
     */
    @GET("recommend")
    Observable<RecommendEntity> qiyiRecommendDetail(@QueryMap Map<String, String> params);

}
