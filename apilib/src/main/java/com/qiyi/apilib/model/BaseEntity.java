package com.qiyi.apilib.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public class BaseEntity implements Serializable {
    /**
     * 服务器返回码，100000=成功
     */
    @SerializedName("code")
    public int code;

}
