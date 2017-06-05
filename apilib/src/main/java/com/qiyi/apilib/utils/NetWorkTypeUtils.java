package com.qiyi.apilib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetWorkTypeUtils {


    /**
     * network is HSPA+
     * 因该参数为android api13 新增的参数。固此处使用常量表示
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    public static NetworkInfo getAvailableNetWorkInfo(Context context) {

        try {

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isAvailable()) {
                return activeNetInfo;

            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 是否有可用网络
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        return getAvailableNetWorkInfo(context) != null;
    }

    public static String getNetWorkType(Context context) {
        if (context == null) {
            return "";
        }

        String netWorkType = "";
        NetworkInfo netWorkInfo = getAvailableNetWorkInfo(context);

        if (netWorkInfo != null) {
            if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netWorkType = "1";
            } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager telephonyManager =
                        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        netWorkType = "2";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        netWorkType = "3";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        netWorkType = "4";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        netWorkType = "5";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        netWorkType = "6";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        netWorkType = "7";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        netWorkType = "8";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        netWorkType = "9";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        netWorkType = "10";
                        break;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        netWorkType = "11";
                        break;
                    case NETWORK_TYPE_HSPAP:
                        netWorkType = "12";
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netWorkType = "14";
                        break;

                    default:
                        netWorkType = "-1";
                }
            }
        }

        return netWorkType;
    }

    public static NetworkStatus getNetworkStatus(Context context) {
        NetworkStatus status = getNetworkStatusFor4G(context);
        if (status == NetworkStatus.MOBILE_4G) {
            return NetworkStatus.MOBILE_3G;
        } else {
            return status;
        }
    }

    public static NetworkStatus getNetworkStatusFor4G(Context context) {
        NetworkInfo networkInfo = getAvailableNetWorkInfo(context);
        if (null == networkInfo)
            return NetworkStatus.OFF;
        int type = networkInfo.getType();
        if (ConnectivityManager.TYPE_WIFI == type) {
            return NetworkStatus.WIFI;
        }
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        type = telephonyManager.getNetworkType();
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return NetworkStatus.MOBILE_2G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetworkStatus.MOBILE_4G;
            default:
                return NetworkStatus.MOBILE_3G;
        }
    }

    @Deprecated
    public static String getNetWorkApnType(Context context) {
        if (null == context) {
            return null;
        }
        String mApnName = null;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            mApnName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            if ("wifi".equalsIgnoreCase(mApnName)) {
                mApnName = "wifi";
            } else {
                mApnName = info.getExtraInfo().toLowerCase(); // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mApnName;
    }
}
