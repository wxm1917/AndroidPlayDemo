package com.qiyi.apilib.net;

import com.qiyi.apilib.ApiConstant;
import com.qiyi.apilib.ApiLib;
import com.qiyi.apilib.utils.DeviceUtils;
import com.qiyi.apilib.utils.NetWorkTypeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2017/5/5.
 */
public class ApiParamsGen {

    /**
     * 构建Open API通用参数
     * @return
     */
    public static Map<String, String> genCommonParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_k", ApiConstant.APP_KEY);
        params.put("version", ApiConstant.APP_V);
        params.put("app_v", ApiConstant.APP_V);
        params.put("app_t", ApiConstant.APP_T);
        params.put("platform_id", ApiConstant.PLATFORM_ID);
        params.put("dev_os", DeviceUtils.getAndroidOSVersion());
        params.put("dev_ua", DeviceUtils.getDeviceUA());
        params.put("dev_hw", DeviceUtils.getDeviceHW());
        params.put("net_sts", NetWorkTypeUtils.getNetWorkType(ApiLib.CONTEXT));
        params.put("scrn_sts", "0"); //屏幕状态，0：默认， 1：竖屏， 2：横屏，3：mini
        params.put("scrn_res", DeviceUtils.getResolution(ApiLib.CONTEXT)); //屏幕分辨率
        params.put("scrn_dpi", String.valueOf(DeviceUtils.getScreenDensityDpi(ApiLib.CONTEXT))); //屏幕密度
        params.put("qyid", DeviceUtils.getDeviceId(ApiLib.CONTEXT));
        params.put("secure_v", "1"); //客户端安全版本，固定写死为1
        params.put("secure_p", "GPhone");//客户端安全标识
        params.put("core", ""); //播放内核类型
        params.put("req_sn", String.valueOf(System.currentTimeMillis())); //客户端请求接口的时间戳,长整数
        params.put("req_times", "1"); //请求次数,客户端请求接口的次数，第一次请求req_times=1,如失败后重试一次，则req_times=2，以此类推
        return params;
    }

    /**
     * 构建获取频道列表的参数
     * @return
     */
    public static Map<String, String> genChannelParams() {
        Map<String, String> params = genCommonParams();
        params.put("type", "list"); //请求频道列表
        return params;
    }

    /**
     * 获取频道详情信息参数
     * @param channelId 频道ID
     * @param channelName 频道名称
     * @param pageIndex 分页码
     * @param pageSize 每页数据条数
     * @return
     */
    public static Map<String, String> genChannelDetailParams(String channelId, String channelName, int pageIndex, int pageSize) {
        Map<String, String> params = genCommonParams();
        params.put("type", "detail"); //频道详情
        params.put("channel_name", channelName);

        /**
         * 排序方式（排序方式，若无此参数，则默认按照相关性排序,使用此参数对应基线中片库:热播榜,好评榜,新上线 等功能）:
         * 1 按照相关性排序；
         * 2 视频创建时间；
         * 4 最新更新时间排序(新上线)；
         * 5 vv；
         * 8 评分 (好评榜)；
         * 9 历史点击量排序；
         * 10 周点击排序；
         * 11 昨日点击排序 (热播榜)排序方式。
         */
        params.put("mode", "11");

        /**
         * 付费方式（付费方式，若无此参数，则返回所有付费和非付费的）：
         * 0：免费
         * 1：付费未划价(是付费片子，但是还没有定价，这样状态的片子还不允许上线)
         * 2：付费已划价(判断是否会员视频，直接判断is_purchase=2即可)
         */
       // params.put("is_purchase", "2");

        /**
         * 1： 仅要付费点播的结果
         * 0：不要付费点播的结果
         * 不传：要付费点播和非付费点播的结果
         */
        params.put("on_demand", "0");

        params.put("page_num", String.valueOf(pageIndex));
        params.put("page_size", String.valueOf(pageSize));

        //是否需要返回频道标签列表
        params.put("require_tag_list", "1");

        return params;
    }

    /**
     * 构建推荐页参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static Map<String, String> genRecommendDetailParams(int pageIndex, int pageSize) {
        Map<String, String> params = genCommonParams();
        params.put("page_num", String.valueOf(pageIndex));
        params.put("page_size", String.valueOf(pageSize));
        return params;
    }
}
