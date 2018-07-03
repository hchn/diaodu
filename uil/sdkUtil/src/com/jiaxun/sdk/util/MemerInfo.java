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
 * ˵����
 *
 * @author  chaimb
 *
 * @Date 2015-6-5
 */
public class MemerInfo
{
    private final static String TAG = MemerInfo.class.getName();

    /**
     * ���ϵͳ�Ƿ��ڵ��ڴ�����
     * ����˵�� :
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

//        Log.info(TAG,"ϵͳʣ���ڴ�:"+(info.availMem >> 10)+"k");   

//        Log.info(TAG, "ϵͳ�Ƿ��ڵ��ڴ����У�" + info.lowMemory);

//        Log.info(TAG,"��ϵͳʣ���ڴ����"+info.threshold+"ʱ�Ϳ��ɵ��ڴ�����");
        return info.lowMemory;
    }

    /**
     * ��ȡ�ڲ��洢���ÿռ��С
     * ����˵�� :
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
     * ����˵�� : ��ȡSD���ܿռ��С
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */
    public static long getSdTotalSize(Context context)
    {
        File path = Environment.getExternalStorageDirectory();// ��ȡSDCard��Ŀ¼
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long totalBlocks = sf.getBlockCount();
        Log.info(TAG, "blockSize * totalBlocks::" + blockSize * totalBlocks);
        return (blockSize * totalBlocks);
    }

    /**
     * ��ȡSD�����ÿռ��С
     * ����˵�� :
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-6-11
     */

    public static long getSdAvailableSize(Context context)
    {
        File path = Environment.getExternalStorageDirectory();// ��ȡSDCard��Ŀ¼
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long availableBlocks = sf.getAvailableBlocks();
        return (blockSize * availableBlocks);
    }

    /**
     * ��ȡSD�������ÿռ��С
     * ����˵�� :
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
     * ��ȡ�ڲ��洢���ÿռ��С
     * ����˵�� :
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
     * ����˵�� :��ȡ�ڲ��洢��������С
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
     * ����˵�� :��ȡSD����������
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
     * ����˵�� :��ȡ�ڲ��洢�Ŀ��ÿռ�
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
     * ����˵�� :��ȡ�ڲ��洢������
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
     * ����˵�� :��ȡSD���Ŀ��ÿռ�
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
     * ����˵�� :�ⲿ�洢�Ƿ���� (�����Ҿ��ж�дȨ��)
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
     * ����˵�� :SD���Ƿ��ڴ治�㣨ռ�����ڴ�ٷ�֮��ʮ��
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
     * ����˵�� :�ڲ��洢ռ����
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

    // �ٷֱȸ�ʽ
    private static DecimalFormat df = new DecimalFormat("#0.00%");

    /**
     * �ڴ�ռ����
     */
    public static String getMemoryPer()
    {
        long totalMemory = getTotalMemory();
        long appMemory = getAppMemory();
        return df.format((double) appMemory / totalMemory);
    }

    /**
     * CPUռ����
     */
    public static String getCpuPer()
    {
        long totalCpu = getTotalCpuTime();
        long appCpu = getAppCpuTime();
        return df.format((double) appCpu / totalCpu);
    }

    /**
     * ����˵�� :��ȡϵͳ��cupռʱ
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return ϵͳ��cupռʱ
     */
    public static long getTotalCpuTime()
    { // ��ȡϵͳ��CPUʹ��ʱ��
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
     * ����˵�� :��ȡ��ǰӦ��cupռʱ
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return ��ǰӦ��cupռʱ
     */
    public static long getAppCpuTime()
    { // ��ȡӦ��ռ�õ�CPUʱ��
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
     * ����˵�� :ϵͳ���ڴ��С����λ��kb
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return ϵͳ���ڴ�
     */
    public static long getTotalMemory()
    {
        String str1 = "/proc/meminfo";// ϵͳ�ڴ���Ϣ�ļ�
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// ��ȡmeminfo��һ�У�ϵͳ���ڴ��С
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// ���ϵͳ���ڴ棬��λ��KB������1024ת��ΪByte
            localBufferedReader.close();
        }
        catch (IOException e)
        {
            Log.exception(TAG, e);
        }
        return initial_memory;
    }

    /**
     * ����˵�� :��ȡ��ǰӦ���ڴ棬��λ��kb
     * 
     * @author fuluo
     * @Date 2014-3-19
     * 
     * @return Ӧ���ڴ�
     */
    public static long getAppMemory()
    {
        Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory() - runtime.freeMemory();
    }

    /**
     * ��ȡϵͳ��cpuʹ����
     * ����˵�� :
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
