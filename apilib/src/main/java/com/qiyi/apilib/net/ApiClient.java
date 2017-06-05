package com.qiyi.apilib.net;


import com.qiyi.apilib.service.ApiService;
import com.qiyi.apilib.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhouxiaming on 2017/5/5.
 */

public class ApiClient {

    private static Map<String, ApiService> apiServiceMap = new HashMap<>();
    /**
     * 获取ApiService对象，避免重复创建，static缓存起来
     * @param host
     * @return
     */
    public static ApiService getAPiService(String host) {
        ApiService apiService = apiServiceMap.get(host);

        if (apiService == null) {
            RetrofitClient client = new RetrofitClient(host, LogUtils.isDebug, true, false);
            apiService = client.createApi(ApiService.class);
            apiServiceMap.put(host, apiService);
        }
        return apiService;
    }

}
