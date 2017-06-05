package com.qiyi.apilib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import com.qiyi.apilib.ApiLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by zhouxiaming on 2017/4/18.
 */

public class DeviceUtils {
    private static String TAG = "DeviceUtils";
    private static String mDeviceId;
    private static String mDeviceHW;
    private static String mResolution;//分辨率
    private static int mDensityDpi; //屏幕密度
    /**
     * 获取设备ID
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        if (!StringUtils.isEmpty(mDeviceId)) {
            return mDeviceId;
        }

        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        String cacheValue = preferenceUtils.getDeviceId();
        if (!StringUtils.isEmpty(cacheValue)) {
            mDeviceId = cacheValue;
            return mDeviceId;
        }
        String deviceId = "";
        String str1 = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        try {
            TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            str1 = localTelephonyManager.getDeviceId();
        } catch (Exception e) {
            LogUtils.i(TAG, "TelephonyManager getDeviceId exception : "  + e);
        }

        try {
            str2 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            LogUtils.i(TAG, "Settings.Secure ANDROID_ID exception : "  + e);
        }

        str4 = getMacAddress(context);

        str3 = Build.SERIAL;

        if (str1 == null) {
            str1 = "";
        }

        if (str2 == null) {
            str2 = "";
        }

        if (str3 == null) {
            str3 = "";
        }

        if (str4 == null) {
            str4 = "";
        }
        deviceId = str1 + str2 + str3 + str4;
        if (!StringUtils.isEmpty(deviceId)) {
            preferenceUtils.saveDeviceId(deviceId);
            mDeviceId = deviceId;
        }
        return getSha1(deviceId);

    }

    public static String getSha1(String str){
        if (null == str || 0 == str.length()){
            return null;
        }
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取mac地址,
     *
     * @param _mContext
     * @return
     */
    public static String getMacAddress(Context _mContext) {

        String macAddress = "";
        if (_mContext != null) {

            WifiManager wifiManager = (WifiManager) _mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiManager.getConnectionInfo();
                macAddress = wifiInfo.getMacAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return macAddress;
    }

    /**
     * 获取系统版本号
     * @return
     */
    public static String getAndroidOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机的UA信息，如：XIAOMI NOTE
     * @return
     */
    public static String getDeviceUA() {
        return Build.MODEL;
    }

    /**
     * 获取部分硬件信息
     * @return
     */
    public static String getDeviceHW() {
        if (!StringUtils.isEmpty(mDeviceHW)) {
            return mDeviceHW;
        }
        try {
            JSONObject o = new JSONObject();
            o.put("gpu", "");
            o.put("mem", StringUtils.calXB((1F * getAvailableMemorySize()) / (1L << 20)) + "MB");
            mDeviceHW = StringUtils.encoding(o.toString());
            return mDeviceHW;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取分辨率
     * @return
     */
    public static String getResolution(Context context) {
        if (StringUtils.isEmpty(mResolution)) {
            if (null != ApiLib.CONTEXT) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                mResolution = wm.getDefaultDisplay().getWidth() + "*" + wm.getDefaultDisplay().getHeight();
            }
        }
        return mResolution;
    }

    /**
     * 获取屏幕密度
     * @param context
     * @return
     */
    public static int getScreenDensityDpi(Context context) {
        if (mDensityDpi != 0) {
            return mDensityDpi;
        }
        int ret = DisplayMetrics.DENSITY_MEDIUM;
        context = context == null ? ApiLib.CONTEXT : context;
        if (context != null) {
            try {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                ret = dm.densityDpi;
                mDensityDpi = ret;
            } catch (Exception e) {
                if (LogUtils.isDebug) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        }
        return ret;
    }

    public static long getMemorySize() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getAllocatedMemorySize() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getAvailableMemorySize() {
        return getMemorySize() - getAllocatedMemorySize();
    }
}
