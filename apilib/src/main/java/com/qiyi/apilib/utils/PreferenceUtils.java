package com.qiyi.apilib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public class PreferenceUtils {
    private String API_CONFIG_NAME = "api_config_name";
    private String KEY_DEVICE_ID = "device_id";
    private SharedPreferences mPref;
    public  PreferenceUtils(Context context) {
        mPref = context.getSharedPreferences(API_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存DeviceID
     * @param deviceId
     */
    public void saveDeviceId(String deviceId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.apply();
    }

    /**
     * 读取DeviceID
     * @return
     */
    public String getDeviceId() {
        return mPref.getString(KEY_DEVICE_ID, null);
    }
}
