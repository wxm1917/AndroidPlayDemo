package com.qiyi.openapi.demo.util;

import android.util.Log;

import java.io.File;

/**
 * Created by Bruce on 2017/6/13.
 */

public class FileUtils {
    /**
     * 在SD卡上创建目录
     */
    public static File createDirOnSDCard(String dir)
    {
        File dirFile = new File(dir + File.separator);
        Log.v("createDirOnSDCard", dir + File.separator);
        dirFile.mkdirs();
        return dirFile;
    }

    /**
     * 判断SD卡上文件是否存在
     */
    public static boolean isFileExist(String fileName, String path)
    {
        File file = new File(path + File.separator + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上文件夹是否存在
     */
    public static boolean isFileExist(String path)
    {
        File file = new File(path);
        return file.exists();
    }
}
