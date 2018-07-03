package com.jiaxun.sdk.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.SystemClock;

import com.jiaxun.sdk.util.log.Log;

public class DateUtils
{

    private static final String TAG = DateUtils.class.getName();

    /**
     * 方法说明 : 格式化时长
     * @param duration 单位：秒
     * @return String
     * @author hubin
     * @Date 2014-4-23
     */
    public static String formatDurationTime(long duration)
    {
        StringBuilder result = new StringBuilder();
        if (duration == 0)
        {
            return result.append("未接通").toString();
        }
        else
        {
            long hour = duration / 3600;
            duration = duration % 3600;
            long minute = duration / 60;
            long second = duration % 60;
            if (hour != 0)
            {
                result.append(hour + "小时");
            }
            if (minute != 0)
            {
                result.append(minute + "分");
            }
            if (second != 0)
            {
                result.append(second + "秒");
            }
            return result.toString();
        }
    }

    /**
     * 方法说明 : 格式化时间
     * @param time
     * @return String
     * @author hubin
     * @Date 2014-4-22
     */
    public static String formatDateTime(long time)
    {
        Date date = new Date(time);
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateFormat = format.format(date);

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance(); // 今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today))
        {
            return "今天\n" + dateFormat.split(" ")[1];
        }
        else if (current.before(today) && current.after(yesterday))
        {

            return "昨天\n" + dateFormat.split(" ")[1];
        }
        else
        {
            int index = dateFormat.indexOf("-") + 1;
            return dateFormat.split(" ")[0].substring(index) + "\n" + dateFormat.split(" ")[1];
//            return dateFormat.substring(index, dateFormat.length());
        }
    }

    /**
     * 格式化时间
     * 方法说明 :yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     * @author chaimb
     * @Date 2015-6-18
     */

    public static String formatStartTime(long time)
    {
        if (time == 0)
        {
            return "0";
        }
        Date date = new Date(time);
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormat = format.format(date);
        return dateFormat;
    }
    
    /**
     * 格式化时间
     * 方法说明 :yyyy年MM月dd日 HH时mm分ss秒
     * @param time
     * @return
     * @author chaimb
     * @Date 2015-6-18
     */

    public static String formatTime(long time)
    {
        if (time == 0)
        {
            return "0";
        }
        Date date = new Date(time);
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String dateFormat = format.format(date);
        return dateFormat;
    }

    /**
     * 格式化时间
     * 方法说明 :
     * @param time
     * @return
     * @author chaimb
     * @Date 2015-6-18
     */

    public static String formatCallStartTime(long time)
    {
        if (time == 0)
        {
            return "0";
        }
        Date date = new Date(time);
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy.MM.dd");
        String dateFormat = format.format(date);
        return dateFormat;
    }

    /**\
     * 获取现在时间
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-6-18
     */
    public static String getNowTime()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new java.text.SimpleDateFormat("HH:mm:ss");
        String dateFormat = format.format(date);
        return dateFormat;
    }

    /**\
     * 获取现在时间   yyyy年mm月dd日\nhh:ss
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-6-18
     */
    public static String getNowFormatTime()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String week = getWeek(calendar);
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy年MM月dd日\n");
        String dateFormat = format.format(date);
        dateFormat = dateFormat + week;
        return dateFormat;
    }

    private static String getWeek(Calendar calendar)
    {
        String week = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK))
        {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;

            default:
                week = "周日";
                break;
        }
        return week;
    }

    /**
     * 时间反格式化
     * 方法说明 :
     * @param formatTime
     * @return
     * @author chaimb
     * @Date 2015-6-18
     */

    public static long getTimeMilliseconds(String formatTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        Date date = null;
        try
        {
            date = (Date) sdf.parse(formatTime);
        }
        catch (ParseException e)
        {
            Log.exception(TAG, e);
        }
        long dateTime = date.getTime();
        return dateTime > 0 ? dateTime : 0;

    }

    public static String getFormatTime(long formatTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = sdf.format(formatTime);

        return result;
    }

    /**
     * 方法说明 :设置日期时间
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @throws IOException
     * @throws InterruptedException
     * @author chaimb
     * @Date 2015-9-1
     */
    public static void setDateTime(int year, int month, int day, int hour, int minute) throws IOException, InterruptedException
    {

        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);

        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE)
        {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        // Log.d(TAG, "set tm="+when + ", now tm="+now);

        if (now - when > 1000)
            throw new IOException("failed to set Date.");

    }

    /**
     * 方法说明 :设置日期
     * @param year
     * @param month
     * @param day
     * @throws IOException
     * @throws InterruptedException
     * @author chaimb
     * @Date 2015-9-1
     */
    public static boolean setDate(int year, int month, int day) throws IOException, InterruptedException
    {

        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE)
        {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        // Log.d(TAG, "set tm="+when + ", now tm="+now);
        if (now - when > 1000)
            Log.error(TAG, "failed to set Date.");

        if (now - when > 1000)
        {
            return false;
//            throw new IOException("failed to set Date.");
        }
        else
        {
            return true;
        }
    }

    /**
     * 方法说明 : 设置时间
     * @param hour
     * @param minute
     * @throws IOException
     * @throws InterruptedException
     * @author chaimb
     * @Date 2015-9-1
     */
    public static boolean setTime(int hour, int minute) throws IOException, InterruptedException
    {

        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE)
        {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        // Log.d(TAG, "set tm="+when + ", now tm="+now);
        if (now - when > 1000)
            Log.error(TAG, "failed to set Date.");

        if (now - when > 1000)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void requestPermission() throws InterruptedException, IOException
    {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    public static Process createSuProcess() throws IOException
    {
        File rootUser = new File("/system/xbin/ru");
        if (rootUser.exists())
        {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        }
        else
        {
            return Runtime.getRuntime().exec("su");
        }
    }

    public static Process createSuProcess(String cmd) throws IOException
    {

        DataOutputStream os = null;
        Process process = createSuProcess();

        try
        {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        return process;
    }

}
