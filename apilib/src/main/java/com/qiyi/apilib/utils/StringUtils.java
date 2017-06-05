package com.qiyi.apilib.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    protected static final DecimalFormat dFormat = new DecimalFormat("#0.0");

    public static String encoding(String str) {
        return isEmpty(str) ? "" : URLEncoder.encode(str);
    }

    public static String decoding(String str) {

        return isEmpty(str) ? "" : URLDecoder.decode(str);
    }

    public static String getValue(String str) {
        return (str != null) ? str : "null";
    }

    public static String maskNull(String str) {
        return isEmpty(str) ? "" : str;
    }

    public static final String maskUrl(String strUrl) {
        if (TextUtils.isEmpty(strUrl)) {
            return "";
        }
        String url = strUrl.trim().replaceAll("&amp;", "&");
        url = url.replaceAll(" ", "%20").trim();
        if (TextUtils.isEmpty(url))
            return "";
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
            return url;
        }
        return url;
    }

    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        } else {
            if (str.length() > 4) {
                return false;
            } else {
                return str.equalsIgnoreCase("null");
            }

        }
    }

    /**
     * int 默认值
     *
     * @param num
     * @return
     */
    public static boolean isEmpty(int num) {
        return isEmpty(num, 0);
    }

    /**
     * int 默认值
     *
     * @param num
     * @return
     */
    public static boolean isEmpty(int num, int defauleNum) {
        return num == defauleNum;
    }

    public static boolean isEmptyStr(String str) {
        return null == str || "".equals(str);
    }

    public static String toStr(Object _obj, String _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        return String.valueOf(_obj);
    }

    /**
     * 根据传入的分隔符参数将字符串分割并传入数组;
     *
     * @param regex:分隔符;
     * @param res:字符串;
     * @return
     */
    public static String[] split(String regex, String res) {
        if (regex == null || res == null) {
            return null;
        }

        Vector<String> vector = new Vector<String>();
        int index = res.indexOf(regex);

        if (index == -1) {
            vector.addElement(res);
        } else {
            while (index != -1) {
                vector.addElement(res.substring(0, index));
                res = res.substring(index + 1, res.length());
                index = res.indexOf(regex);
            }

            if (index != res.length() - 1) {
                vector.addElement(res);
            }
        }

        final String[] array = new String[vector.size()];
        vector.copyInto(array);
        vector = null;

        return array;
    }

    public static int toInt(Object _obj, int _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Integer.parseInt(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static boolean toBoolean(Object _obj, boolean _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Boolean.parseBoolean(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static int getInt(String intString, int defaultValue) {
        try {
            if (!StringUtils.isEmpty(intString)) {
                defaultValue = Integer.parseInt(intString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static final float toFloat(Object _obj, float _defaultValue) {
        if (StringUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Float.parseFloat(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static final double toDouble(Object _obj, double _defaultValue) {
        if (StringUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Double.parseDouble(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static final String decimalFormat(Object _obj, double _defaultValue) {
        return dFormat.format(toDouble(_obj, _defaultValue));
    }

    public static final long toLong(Object _obj, long _defaultValue) {
        if (StringUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Long.parseLong(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }


    public static boolean isEmptyList(List<?> list) {
        return null == list || list.size() == 0;
    }

    public static boolean isEmpty(Collection<?> list) {
        return isEmpty(list, 1);
    }

    public static <T> boolean isEmpty(T[] array) {
        return isEmpty(array, 1);
    }

    public static <T> boolean isEmpty(T[] array, int len) {
        return null == array || array.length < len;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isEmpty(map, 1);
    }

    public static boolean isEmpty(Collection<?> list, int len) {
        return null == list || list.size() < len;
    }

    public static boolean isEmpty(Map<?, ?> map, int len) {
        return null == map || map.size() < len;
    }


    public static boolean isEmptyList(List<?> list, int len) {
        return null == list || list.size() < len;
    }

    public static boolean isEmptyMap(Map<?, ?> map) {
        return null == map || map.size() == 0;
    }

    public static boolean isEmptyArray(Object[] array) {
        return isEmptyArray(array, 1);
    }

    public static boolean isEmptyArray(Object array) {
//		return isEmptyArray(array, 1);
        return null == array;
    }

    public static boolean isEmptyArray(Object[] array, int len) {
        return null == array || array.length < len;
    }

    public static String dateString2String(String str, String format) {
        try {
            return dataFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str), format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Date string2Date(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
    }

    public static Date string2Date(String date, String format) throws ParseException {
        if (isEmpty(format)) {
            return string2Date(date);
        }
        return new SimpleDateFormat(format).parse(date);
    }

    public static String dataFormat(Date date, String format) {
        return null == date ? "" : new SimpleDateFormat(format).format(date);
    }

    public static String dateFormat(Date date) {
        return dataFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Double string2DoubleScale(String str, int scale) {
        return isEmpty(str) ? 0.0 : new BigDecimal(str).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 100;
    }

    public static Integer toDoubleScale(String str, int scale) {
        if (isEmpty(str) || scale < 1) {
            return 0;
        }

        Double _double = new BigDecimal(str).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
        return _double.intValue();
    }

    public static String byte2XB(long b) {
        long i = 1L << 10L;
        if (b < i)
            return b + "B";
        i = 1L << 20L;
        if (b < i)
            return calXB(1F * b / (1L << 10L)) + "K";

        i = 1L << 30L;
        if (b < i)
            return calXB(1F * b / (1L << 20L)) + "M";
        i = 1L << 40L;
        if (b < i)
            return calXB(1F * b / (1L << 30L)) + "G";
        if (b < i)
            return calXB(1F * b / (1L << 40L)) + "T";
        return b + "B";
    }

//    private static String calXB(float r) {
//    	String result = r + "";
//    	int index = result.indexOf(".");
//		String s = result.substring(0, index + 1);
//		String n = result.substring(index + 1);
//		if(n.length() > 2)
//			n = n.substring(0, 2);
//		return s + n;
//    }

    public static String cleanperiod(String str) {
        if (str != null && str.length() >= 1) {
            return str.endsWith("期") ? (String) str.subSequence(0, str.length() - 1) : str;
        } else {
            return str;
        }
    }

    public static String getDate(String str) {
        if (str != null && !str.equals("")) {
            String[] subStr = str.split(" ");

            return subStr[0];
        } else {
            return str;
        }
    }

    public static String calXB(float r) {
        String result = r + "";
        int index = result.indexOf(".");
        String s = result.substring(0, index + 1);
        //return s;
        String n = result.substring(index + 1);
        if (n.length() >= 1)
            n = n.substring(0, 1);

        return s + n;
    }

    /**
     * 播放器中格式化播放时间的函数
     */
    public static String stringForTime(int timeMs) {
        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());
        String result = null;

        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            result = formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            result = formatter.format("%02d:%02d", minutes, seconds).toString();
        }

        formatter.close();

        return result;
    }

    /**
     * 去掉输入的空格和换行
     *
     * @param str
     * @return
     */
    public static String removeBlankAndN(String str) {
        if (!StringUtils.isEmpty(str)) {
            str = str.replace("\n", "").replace("\t", "").trim();
        }

        return str;
    }

    /**
     * 判断字符串 s 是否是一个integer
     *
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {
        if (isEmpty(s)) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1)
                    return false;
                else
                    continue;
            }
            if (Character.digit(s.charAt(i), 10) < 0)
                return false;
        }
        return true;
    }


    /**
     * 获取子字符串在原字符串中第N次出现的位置。
     *
     * @param string 原字符串
     * @param sub    要查找的字符串
     * @param N      第几次出现。
     */
    public static int getCharacterPosition(String string, String sub, int N) {

        Matcher slashMatcher = Pattern.compile(sub).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == N) {
                break;
            }
        }
        return slashMatcher.start();
    }

    /**
     * 动态时间处理
     *
     * @param now
     * @param d
     * @return
     */
    public static String getDataUtil(long now, long d) {
//		long ld1= Long.parseLong("1395905391000");
//		long ld2= Long.parseLong("1395514448000");
        try {
            if (StringUtils.isEmpty(String.valueOf(now)) || StringUtils.isEmpty(String.valueOf(d)) || 0 == now || 0 == d) {
                return "";
            }
            if (String.valueOf(now).length() < "1395905391000".length()) {
                now = now * 1000;
            }
            if (String.valueOf(d).length() < "1395905391000".length()) {
                d = d * 1000;
            }
            SimpleDateFormat formatteryear = new SimpleDateFormat("yyyy年MM月dd日");
            Date curDate1 = new Date(now);//获取当前时间
            Date curDate2 = new Date(d);//获取当前时间

            if (curDate1.getYear() > curDate2.getYear()) {//y
                return formatteryear.format(curDate2);
            } else if (curDate1.getMonth() > curDate2.getMonth()) {//m
                String str = formatteryear.format(curDate2);
                if (!StringUtils.isEmpty(str) && str.indexOf("月") > 0) {
                    return str.substring(str.indexOf("年") + 1);
                }
                return "";
            } else if (curDate1.getDate() > curDate2.getDate()) {
                if (curDate1.getDate() - curDate2.getDate() == 1) {
                    return "昨天";
                } else {
                    String str = formatteryear.format(curDate2);
                    if (!StringUtils.isEmpty(str) && str.indexOf("月") > 0) {
                        return str.substring(str.indexOf("年") + 1);
                    }
                }
                return "";
            } else if (curDate1.getHours() > curDate2.getHours()) {
                return (curDate1.getHours() - curDate2.getHours()) + "小时前";
            } else if (curDate1.getMinutes() > curDate2.getMinutes()) {
                return (curDate1.getMinutes() - curDate2.getMinutes()) + "分钟前";
            } else if (curDate1.getSeconds() >= curDate2.getSeconds()) {
                return "1分钟前";
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 处理播放次数
     *
     * @param playtimes
     * @return
     */
    public static String processPlayTimes(int playtimes) {
        String result = "";
        String playTimes = playtimes + "";
        int length = playTimes.length();
        int tempIndex;
        if (length > 3) {
            StringBuilder playTimesBuilder = new StringBuilder();
            tempIndex = length / 3;
            if (tempIndex * 3 == length) {
                int tempStart = 0;
                int tempEnd = 2;
                playTimesBuilder.append(playTimes.substring(0, 2));
                tempIndex -= tempIndex;
                for (int i = tempIndex; i > 0; i--) {
                    tempStart = tempEnd;
                    tempEnd = tempEnd + 3;
                    playTimesBuilder.append("，").append(playTimes.substring(tempStart, tempEnd));
                }
            } else {
                int tempStart = 0;
                int tempEnd = length - tempIndex * 3;
                playTimesBuilder.append(playTimes.substring(tempStart, tempEnd));
                for (int i = tempIndex; i > 0; i--) {
                    tempStart = tempEnd;
                    tempEnd = tempEnd + 3;
                    playTimesBuilder.append(",").append(playTimes.substring(tempStart, tempEnd));
                }
            }

            result = playTimesBuilder.toString();
        } else {
            result = playTimes;
        }

        return result;
    }


    /**
     * 最终规则整理如下：
     * is_ugc_album = false
     * if (upderid > 0 || album_id % 100 == 9 || album_id % 100 == 16) {
     * is_ugc_album = true
     * }
     *
     * @param id
     * @param upderid
     * @return
     */
    public static boolean isUGC(String id, String upderid) {

/*		以前的判断方法，已废弃
 * 		if(!StringUtils.isEmpty(id) && ( id.length() > 9  || (id.length() == 9 && id.substring(0).compareTo("2") > 0) && String.valueOf(id).endsWith("09"))){
			return true;
		}*/

        try {
            if ((!StringUtils.isEmpty(id) && (id.endsWith("09") || id.endsWith("16") || id.equals("9"))) || !(isEmpty(upderid) || upderid.startsWith("-") || upderid.equals("0"))) {
                return true;
            }
        } catch (Exception e) {
        }


        return false;
    }

    /**
     * @param numString
     * @return 万亿为单位的text
     */
    public static String getBillionStyledText(String numString) {
        if (StringUtils.isEmpty(numString)) {
            return "0";
        }

        String formmatedStr = numString;

        DecimalFormat dfWan = new DecimalFormat("0.0万");
        DecimalFormat dfYi = new DecimalFormat("0.0亿");

        try {
            int numInt = Integer.parseInt(numString);

            if (numInt > 99999999) {
                formmatedStr = dfYi.format(numInt / 100000000.0);
            } else if (numInt > 9999) {
                formmatedStr = dfWan.format(numInt / 10000.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formmatedStr;
    }


    /**
     * 从jsonobject中读取字符串
     *
     * @param resObj
     * @param key
     * @return
     */
    public static String readString(JSONObject resObj, String key) {

        return readString(resObj, key, "");
    }

    /**
     * 从jsonobject中读取字符串
     *
     * @param jObj
     * @param key
     * @param _defaultValue
     * @return
     */
    public static String readString(JSONObject jObj, String key, String _defaultValue) {
        String rtnStr = _defaultValue;
        if (null == jObj || StringUtils.isEmpty(key)) {
            return rtnStr;
        }

        try {
            if (jObj.has(key)) {
                rtnStr = jObj.optString(key, _defaultValue);
                rtnStr = StringUtils.maskNull(rtnStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jObj = null;
            key = null;
            _defaultValue = null;
        }

        return rtnStr;
    }


    /**
     * 读取object对象
     *
     * @param jObj
     * @param name
     * @return
     */
    public static JSONObject readObj(JSONObject jObj, String name) {
        try {
            return jObj.optJSONObject(name);
        } catch (Exception e) {

        } finally {
            jObj = null;
            name = null;
        }

        return null;
    }

    /**
     * 读取JSON数组
     *
     * @param jObj
     * @param name
     * @return
     */
    public static JSONArray readArr(JSONObject jObj, String name) {
        try {
            return jObj.optJSONArray(name);
        } catch (Exception e) {

        } finally {
            jObj = null;
            name = null;
        }
        return null;
    }

    public static String timeInSecToString(String timeInSec) {
        String presentStr = "";
        try {
            Long timeInsec = Long.parseLong(timeInSec);
            Date date = new Date(timeInsec * 1000);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            presentStr = format.format(date);
        } catch (Exception e) {

        }

        return presentStr;
    }

    /**
     * 为url添加 token 和 uid 参数
     *
     * @param discover_url
     * @param token
     * @param uid
     * @return
     */
    public static String appendUserInfo(String discover_url, String token, String uid) {
        if (!isEmpty(discover_url)) {
            if (discover_url.contains("?")) {
                if (discover_url.endsWith("?") || discover_url.endsWith("&")) {
                    discover_url += "token=" + token + "&uid=" + uid;
                } else {
                    discover_url += "&token=" + token + "&uid=" + uid;
                }
            } else {
                discover_url += "?token=" + token + "&uid=" + uid;
            }
        }

        return discover_url;
    }


    /**
     * 从字符串中匹配出 http://xxx的地址
     *
     * @param string
     * @return
     */
    public static String findUrlFromString(String string) {
        String real = "";
        try {
            if (!TextUtils.isEmpty(string)) {
                Pattern pattern = Pattern.compile("http://[0-9a-zA-Z|.|/|?|=]+");
                Matcher mMatcher = pattern.matcher(string);
                if (mMatcher.find()) {
                    real = mMatcher.group();
                }
            }
        } catch (Exception e) {

        }
        return real;
    }

    /**
     * 从对象数字中取出index的数据
     *
     * @param obj
     * @param index
     * @return
     */
    public static String getStringFromParas(Object[] obj, int index) {
        String ret = "";
        if (obj != null) {
            if (!isEmptyArray(obj, index + 1) && obj[index] != null) {
                ret += obj[index];
            }
        }
        return ret;
    }

    /**
     * 从对象数字中取出index的数据
     *
     * @param obj
     * @param index
     * @return
     */
    public static String getStringFromParas(Object[] obj, int index, String def) {
        String ret = "";
        if (def != null) {
            ret = def;
        }
        if (obj != null) {
            if (!isEmptyArray(obj, index + 1) && obj[index] != null) {
                ret += obj[index];
            }
        }
        return ret;
    }

    /**
     * 为url添加 gateway参数（假如没有该参数，有的话则不做任何操作）
     *
     * @param url
     * @param from_type
     * @param from_sub_type
     * @return
     */
    public static String appendGateway(String url, int from_type, int from_sub_type) {

        if (!isEmpty(url) && (from_type + from_sub_type) > 0 && !url.contains("gateway=")) {
            if (url.contains("?")) {
                if (url.endsWith("?") || url.endsWith("&")) {
                    url += "gateway=" + from_type + ":" + from_sub_type;
                } else {
                    url += "&gateway=" + from_type + ":" + from_sub_type;
                }
            } else {
                url += "?gateway=" + from_type + ":" + from_sub_type;
            }
        }


        return url;
    }

    /**
     * 为url添加 P00001 和 uid 参数
     *
     * @param discover_url
     * @param token
     * @param uid
     * @return
     */
    public static String replaceUserInfo(String version, String discover_url, String token, String uid) {

        if (isEmpty(discover_url)) {
            return "";
        }

        String url = discover_url;
        String cookie = "";
        String uid_old = "";

        int cookie_index = url.indexOf("P00001");
        int uid_index = url.indexOf("uid");

        if (cookie_index != -1) {
            cookie = url.substring(cookie_index + 7);

            if (cookie.contains("&")) {
                int i = cookie.indexOf("&");
                if (i != -1) {
                    cookie = cookie.substring(0, i);
                }
            }
        }

        if (uid_index != -1) {
            uid_old = url.substring(uid_index + 4);

            if (uid_old.contains("&")) {
                int i = uid_old.indexOf("&");
                if (i != -1) {
                    uid_old = uid_old.substring(0, i);
                }
            }
        }

        if (!isEmpty(discover_url)) {
            if (discover_url.contains("?")) {
                if (!isEmpty(cookie) && cookie.equals("0")) {
                    discover_url = discover_url.replace("P00001=0", "P00001=" + token);
                }
                if (!isEmpty(uid_old) && uid_old.equals("0")) {
                    discover_url = discover_url.replace("uid=0", "uid=" + uid);
                }

                if (discover_url.contains("mobact2rd/actlist?")) {
                    discover_url = url;
                }

            } else {
                if (discover_url.contains("mobact2rd/acts/1/6/0/")) {
                    discover_url = discover_url.replace(version + "/0/", version + "/" + token + "/");
                }
                if (discover_url.contains("mobact2rd/acts/1/6/0/")) {
                    discover_url = discover_url.replace("acts/1/6/0/", "acts/1/6/" + uid + "/");
                }
            }
        }

        if (discover_url.equals(url)) {
            return "";
        } else {
            return discover_url;
        }
    }

    /**
     * 为url添加参数（有则替换）
     *
     * @return
     */
    public static String appendOrReplaceUrlParameter(String url, LinkedHashMap<String, String> parameterMap) {

        String ret = url;

        if (ret != null && parameterMap != null) {
            LinkedHashMap<String, String> appendMap = new LinkedHashMap<String, String>();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                String parameterName = entry.getKey();
                if (parameterName != null) {
                    //做替换
                    String parameterOccurrence;
                    if (ret.contains(parameterOccurrence = "?" + parameterName + "=")) {
                        ret = ret.replaceAll("\\" + parameterOccurrence + "([^&]+|)", parameterOccurrence + entry.getValue());
                    } else if (ret.contains(parameterOccurrence = "&" + parameterName + "=")) {
                        ret = ret.replaceAll(parameterOccurrence + "([^&]+|)", parameterOccurrence + entry.getValue());
                    }
                    //append
                    else {
                        appendMap.put(parameterName, entry.getValue());
                    }
                }
            }

            ret = appendParam(ret, appendMap);
        }

        return ret;
    }

    /**
     * 为url后面添加参数
     *
     * @param url
     * @param parameterMap
     * @return
     */
    public static String appendParam(String url, LinkedHashMap<String, String> parameterMap) {

        String appendStr = "";
        if (parameterMap != null) {
            StringBuffer appendSB = new StringBuffer();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                String parameterName = entry.getKey();
                if (!isEmpty(parameterName)) {
                    try {
                        appendSB.append(parameterName + "=" + String.valueOf(entry.getValue()) + "&");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            appendStr = appendSB.toString();
        }

        int lastIndexOfDivider = appendStr.lastIndexOf("&");
        if (lastIndexOfDivider >= 0 && (lastIndexOfDivider + "&".length()) == appendStr.length()) {
            appendStr = appendStr.substring(0, lastIndexOfDivider);
        }

        if (!(isEmpty(appendStr) || isEmpty(url))) {
            if (url.contains("?")) {
                if (url.endsWith("&")) {
                    url += appendStr;
                } else {
                    url += "&" + appendStr;
                }
            } else {
                url += "?" + appendStr;
            }
        }

        return url;
    }


    public static String getNumString(String s, int count) {
        if (isEmpty(s)) return s;
        if (s.length() > count) {
            s = s.substring(0, count);
        }
        return s;
    }

    /**
     * 验证字符串是否是数字（允许以0开头的数字）
     * 通过正则表达式验证。
     */
    public static boolean isNumber(String numStr) {
        if (TextUtils.isEmpty(numStr))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(numStr);
        return match.matches();
    }

    public static float parseFloat(String strFloat, float defaultfloat) {
        if (TextUtils.isEmpty(strFloat)) {
            return defaultfloat;
        }
        try {
            defaultfloat = Float.parseFloat(strFloat);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultfloat;
        }
        return defaultfloat;
    }

    public static int parseInt(String strInt, int defaultInt) {
        if (StringUtils.isEmpty(strInt)) {
            return defaultInt;
        }
        try {
            defaultInt = Integer.parseInt(strInt);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultInt;
        }
        return defaultInt;
    }

    public static int parseInt(Object intObj, int defaultInt) {
        if (intObj instanceof Integer) {
            return (int) intObj;
        }
        if (intObj instanceof String) {
            String strInt = (String) intObj;
            return parseInt(strInt, defaultInt);
        }
        return defaultInt;
    }

    public static long parseLong(String strLong, long defaultLong) {
        if (StringUtils.isEmpty(strLong)) {
            return defaultLong;
        }
        try {
            defaultLong = Long.parseLong(strLong);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultLong;
        }
        return defaultLong;
    }

    public static long parseLong(Object longObj, long defaultLong) {
        if (longObj instanceof Long) {
            return (long) longObj;
        }
        if (longObj instanceof String) {
            String strLong = (String) longObj;
            return parseLong(strLong, defaultLong);
        }
        return defaultLong;
    }

    /**
     * 截取url中的host地址。
     */
    public static String getHost(String url) {
        if (url == null || "".equals(url.trim()))
            return null;

        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if (matcher.find())
            return matcher.group();
        else
            return null;
    }


    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 字符串转为md5编码
     *
     * @param str 源字符串
     * @return md5编码后的字符串
     */
    public static String str2md5(String str) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(str.getBytes());
            byte[] bytes = algorithm.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                hexString.append(HEX_DIGITS[b >> 4 & 0xf]);
                hexString.append(HEX_DIGITS[b & 0xf]);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCountDisplay(long count) {
        String str = "";
        DecimalFormat df0 = new DecimalFormat("#");
        DecimalFormat df1 = new DecimalFormat("#.#");
        if (count >= 1E9) {
            str += df0.format((double) count / 1E8) + "亿";
        } else if (count >= 1E8) {
            str += df1.format((double) count / 1E8) + "亿";
        } else if (count < 1E8 && count >= 1E5) {
            str += df0.format((double) count / 1E4) + "万";
        } else if (count >= 1E4) {
            str += df1.format((double) count / 1E4) + "万";
        } else {
            str += count;
        }
        return str;
    }

    public static String urlEncoderString(String source, String charsetName) {
        String result = null;
        try {
            result = URLEncoder.encode(source, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 移除 str末尾的sign
     *
     * @param str
     * @param sign
     * @return
     */
    public static String removeLastSign(String str, String sign) {
        String ret = null;
        if (!TextUtils.isEmpty(str)) {
            if (str.endsWith(sign)) {
                ret = str.substring(0, str.lastIndexOf(sign));
            }
        }
        return ret;
    }

    /***
     * String转换成int
     *
     * @param numStr
     * @return
     */
    public static final int parseInt(String numStr) {
        int val = 0;
        try {
            val = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
        }
        return val;
    }

    /**
     * 测试获取JsonObject
     *
     * @param json
     * @return
     */
    public static JSONObject optJSONObject(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.equals(version1, version2)) {
            return 0;
        }
        if (version1 == null) {
            return -1;
        }
        if (version2 == null) {
            return 1;
        }
        if (!version1.contains(".") || !version2.contains(".")) {
            return version1.compareTo(version2);
        }

        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    /**
     * 不超过一千直接显示
     * 超过一千显示万
     * 最多显示4个有效数字
     */
    public static String getFormatLargeNum(double currentNum) {
        String strNum = "";
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        if (currentNum < 10000) { // 小于1万
            strNum = (int) currentNum + "";
        } else if (currentNum < 10 * 1000 * 1000) { //小于1亿
            strNum = nf.format(currentNum / 10000) + "万";
        } else if (currentNum < 100 * 1000 * 1000) { //小于1亿,显示4位
            nf.setMaximumFractionDigits(0);
            strNum = nf.format(currentNum / 10000) + "万";
        } else { //大于1亿
            strNum = nf.format(currentNum / 100000000) + "亿";
        }
        return strNum;
    }

    /**
     * 不超过一千直接显示
     * 超过一千显示万
     * 最多显示4个有效数字
     */
    public static String getFormatLargeNum(String currentNum) {
        return getFormatLargeNum(toDouble(currentNum, 0));
    }

    /**
     * 获取位置
     *
     * @param key
     * @param arrays
     * @return
     */
    public static <T> int indexOf(Object key, T... arrays) {
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
