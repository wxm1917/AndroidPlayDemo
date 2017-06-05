package com.qiyi.apilib.utils;

/**
 * Created by zhouxiaming on 2017/5/8.
 */

public class ImageUtils {
    public static String IMG_120_160 = "_120_160";
    public static String IMG_180_236 = "_180_236";
    public static String IMG_195_260 = "_195_260";
    public static String IMG_260_360 = "_260_360";
    public static String IMG_220_124 = "_220_124";
    public static String IMG_480_270 = "_480_270";
    public static String IMG_170_100 = "_170_100";
    public static String IMG_284_160 = "_284_160";
    public static String IMG_480_360 = "_480_360";

    /**
     * 获取指定分辨率的图片URL
     * @param url 图片原始地址
     * @param imageReg 分辨率
     * @return
     */
    public static String getRegImage(String url, String imageReg) {
        if (url == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        if (imageReg == null) {
            sb.append(url);
        } else {
            int lastDotIndex = url.lastIndexOf(".");
            sb.append(url.substring(0, lastDotIndex));
            System.out.println(sb.toString());
            sb.append(imageReg);
            sb.append(url.substring(lastDotIndex));
            System.out.println(sb.toString());
        }

        return sb.toString();
    }
}
