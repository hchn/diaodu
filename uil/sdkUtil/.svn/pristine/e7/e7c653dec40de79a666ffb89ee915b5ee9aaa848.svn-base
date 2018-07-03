package com.jiaxun.sdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-6-5
 */
public class MemerInfo
{
    private final static String TAG = MemerInfo.class.getName();

    /**
     * 检测系统是否处于低内存运行
     * 方法说明 :
     * @param context
     * @return 
     * @author chaimb
     * @Date 2015-6-15
     */
    public static boolean displayBriefMemory(Context context)
    {

        final ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(info);

//        Log.info(TAG,"系统剩余内存:"+(info.availMem >> 10)+"k");   

//        Log.info(TAG, "系统是否处于低内存运行：" + info.lowMemory);

//        Log.info(TAG,"当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
        return info.lowMemory;
    }

    /**
     * 获取内部存储可用空间大小
     * 方法说明 :
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */
    public static long getFreeMemory(Context context)
    {
        File path = Environment.getDataDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long availBlocks = sf.getAvailableBlocks();
        return (blockSize * availBlocks);
    }

    /**
     * 
     * 方法说明 : 获取SD卡总空间大小
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */
    public static long getSdTotalSize(Context context)
    {
        File path = Environment.getExternalStorageDirectory();// 获取SDCard根目录
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long totalBlocks = sf.getBlockCount();
        Log.info(TAG, "blockSize * totalBlocks::" + blockSize * totalBlocks);
        return (blockSize * totalBlocks);
    }

    /**
     * 获取SD卡可用空间大小
     * 方法说明 :
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */

    public static long getSdAvailableSize(Context context)
    {
        File path = Environment.getExternalStorageDirectory();// 获取SDCard根目录
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long availableBlocks = sf.getAvailableBlocks();
        return (blockSize * availableBlocks);
    }

    /**
     * 获取SD卡可已用空间大小
     * 方法说明 :
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */

    public static String getSdUsedSize(Context context)
    {
        long usedSize = getSdTotalSize(context) - getSdAvailableSize(context);
        return Formatter.formatFileSize(context, usedSize);
    }

    /**
     * 获取内部存储已用空间大小
     * 方法说明 :
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */

    public static String getMemoryUsedSize(Context context)
    {
        long usedSize = getDataTotalSize(context) - getFreeMemory(context);
        return Formatter.formatFileSize(context, usedSize);
    }

    /**
     *
     * 方法说明 :获取内部存储总容量大小
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */
    public static long getDataTotalSize(Context context)
    {
        File path = Environment.getDataDirectory();

        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long totalBlocks = sf.getBlockCount();
        return (totalBlocks * blockSize);
    }

    /**
     *
     * 方法说明 :获取SD卡的总容量
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-7-6
     */
    public static String getSDTotalSize(Context context)
    {
        return Formatter.formatFileSize(context, getSdTotalSize(context));
    }

    /**
     *
     * 方法说明 :获取内部存储的可用空间
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-7-6
     */
    public static String getMemoryAvailableSize(Context context)
    {
        return Formatter.formatFileSize(context, getFreeMemory(context));
    }

    /**
     *
     * 方法说明 :获取内部存储总容量
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-7-6
     */
    public static String getMemoryTotalSize(Context context)
    {
        return Formatter.formatFileSize(context, getDataTotalSize(context));
    }

    /**
     *
     * 方法说明 :获取SD卡的可用空间
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-7-6
     */
    public static String getSDAvailableSize(Context context)
    {
        return Formatter.formatFileSize(context, getSdAvailableSize(context));
    }

    /**
     * 
     * 方法说明 :外部存储是否可用 (存在且具有读写权限)
     * @return
     * @author chaimb
     * @Date 2015-7-6
     */

    public static boolean isExternalStorageAvailable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 
     * 方法说明 :SD卡是否内存不足（占用总内存百分之八十）
     * @return
     * @author chaimb
     * @Date 2015-7-7
     */
    public static boolean getSDPer(Context context)
    {
        long usedSDMemory = Math.abs(getSdTotalSize(context) - getSdAvailableSize(context));
        long totalSDMmeory = getSdTotalSize(context);
        Log.info(TAG, "usedSDMemory::" + usedSDMemory);
        Log.info(TAG, "totalSDMmeory::" + totalSDMmeory);
        Log.info(TAG, ((double) usedSDMemory / (double) totalSDMmeory) + "");
        return ((double) usedSDMemory / (double) totalSDMmeory) > 0.8 ? true : false;
    }

    /**
     * 
     * 方法说明 :内部存储占有率
     * @return
     * @author chaimb
     * @Date 2015-7-7
     */
    public static String getMemoryPer(Context context)
    {
        long usedMemory = Math.abs(getDataTotalSize(context) - getFreeMemory(context));
        long totalMmeory = getDataTotalSize(context);
        return df.format((double) usedMemory / totalMmeory);
    }

    // 百分比格式
    private static DecimalFormat df = new DecimalFormat("#0.00%");

    /**
     * 内存占用率
     */
    public static String getMemoryPer()
    {
        long totalMemory = getTotalMemory();
        long appMemory = getAppMemory();
        return df.format((double) appMemory / totalMemory);
    }

    /**
     * CPU占用率
     */
    public static String getCpuPer()
    {
        long totalCpu = getTotalCpuTime();
        long appCpu = getAppCpuTime();
        return df.format((double) appCpu / totalCpu);
    }

    /**
     * 方法说明 :获取系统总cup占时
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return 系统总cup占时
     */
    public static long getTotalCpuTime()
    { // 获取系统总CPU使用时间
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            Log.exception(TAG, ex);
        }
        long totalCpu = Long.parseLong(cpuInfos[2]) + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4]) + Long.parseLong(cpuInfos[6])
                + Long.parseLong(cpuInfos[5]) + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    /**
     * 方法说明 :获取当前应用cup占时
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return 当前应用cup占时
     */
    public static long getAppCpuTime()
    { // 获取应用占用的CPU时间
        String[] cpuInfos = null;
        try
        {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            Log.exception(TAG, ex);
        }
        long appCpuTime = Long.parseLong(cpuInfos[13]) + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15]) + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

    /**
     * 方法说明 :系统总内存大小，单位：kb
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return 系统总内存
     */
    public static long getTotalMemory()
    {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        }
        catch (IOException e)
        {
            Log.exception(TAG, e);
        }
        return initial_memory;
    }

    /**
     * 方法说明 :获取当前应用内存，单位：kb
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return 应用内存
     */
    public static long getAppMemory()
    {
        Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory() - runtime.freeMemory();
    }

    /**
     * 获取系统的cpu使用率
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-6-16
     */

//    public static float readUsage()
//    {
//
//        try
//        {
//            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
//            String load = reader.readLine();
//            String[] toks = load.split(" ");
//
//            long idle1 = Long.parseLong(toks[5]);
//            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7])
//                    + Long.parseLong(toks[8]);
//            try
//            {
//                Thread.sleep(360);
//            }
//            catch (Exception e)
//            {
//                Log.exception(TAG, e);
//            }
//            reader.seek(0);
//            load = reader.readLine();
//            reader.close();
//            toks = load.split(" ");
//            long idle2 = Long.parseLong(toks[5]);
//            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7])
//                    + Long.parseLong(toks[8]);
//            return (int) (100 * (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)));
//        }
//        catch (IOException e)
//        {
//            Log.exception(TAG, e);
//        }
//
//        return 0;
//    }

}
