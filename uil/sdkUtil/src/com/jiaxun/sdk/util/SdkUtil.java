package com.jiaxun.sdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.content.Intent;

import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.httpserver.HttpServerService;
import com.jiaxun.sdk.util.log.Log;

/**
 * sdk工具类
 * 
 * @author fuluo
 *
 */
public class SdkUtil
{
    private static String TAG = "UtilsHelper";

    private static Context appContext;

    public static Context getApplicationContext()
    {
        return appContext;
    }

    public static void setApplicationContext(Context context)
    {
        appContext = context;
    }

    /**
     * 获取终端系统型号
     */
    public static String getModel()
    {
        return android.os.Build.MODEL + " ";
    }

    /**
     * 获取操作系统系统开机运行时间
     */
    public static long getRealTime()
    {
        return android.os.SystemClock.elapsedRealtime();
    }

    /**
     * 是否是铁路紧急呼叫
     * 
     * @param number
     *              号码
     * @param priority
     *              优先级
     * @return
     */
    public static boolean checkEmergencyCall(String number, int priority)
    {
        if (number == null || number.equals(""))
        {// 验证参数
            return false;
        }
        if (priority < CommonConfigEntry.PRIORITY_MIN || priority > CommonConfigEntry.PRIORITY_MAX)
        {// 验证参数
            return false;
        }

        if (checkWeatherGroupCall(number) && (number.endsWith(CommonConfigEntry.URGENCY_CALLER) || priority == 0)
                && (number.length() == 5 || number.length() == 10))
        {// 0级组呼，或299
            return true;
        }
        return false;
    }

    /*
     * 根据电话号码判断话路是个呼还是组呼
     * 个呼：false 组呼:true
     */
    private static boolean checkWeatherGroupCall(String number)
    {
        if (number == null || number.length() < 3)
        {
            return false;
        }
        try
        {
            if (number.startsWith(CommonConfigEntry.GROUP_NUMBER) || number.startsWith(CommonConfigEntry.BROADCAST_NUMBER))
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

        return false;
    }

    /**
     * 方法说明 : 通过号码判断号码所属类型
     * @author hubin
     * @Date 2013-11-18
     */
    public static int getCallType(String callNum, boolean video)
    {
        if (callNum == null || callNum.equals(""))
        {
            return 0;
        }

//        if (callNum.startsWith(CommonConfigEntry.BROADCAST_NUMBER))
//        {
//            return CommonConstantEntry.CALL_TYPE_BROADCAST;
//        }
//        else if (callNum.startsWith(CommonConfigEntry.GROUP_NUMBER))
//        {
//            return CommonConstantEntry.CALL_TYPE_GROUP;
//        }
//        else 
        if (callNum.equals(CommonConfigEntry.CONFCALLEE))
        {
            if(video)
                return CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO;
            else
                return CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO;
        }
        else
        {
            if(video)
                return CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO;
            else
                return CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO;
        }
    }

    // input : 2006922101
    // output : 0005050260291210
    public static String vFunctionNumber2UUIE(String strFn)
    {
        if (strFn == null)
        {
            Log.error(TAG, "strFn:" + strFn);
            return null;
        }
        int iOctetCnt = 0;

        StringBuffer strUUIE = new StringBuffer("0005");

        if (strFn.length() % 2 != 0)
        {
            // 奇数个数需补F
            strFn += "F";
        }

        iOctetCnt = strFn.length() / 2;
        if (iOctetCnt < 10)
        {
            strUUIE.append("0").append(iOctetCnt);
        }
        else
        {
            strUUIE.append(iOctetCnt);
        }

        int temp;
        for (int i = 0; i < iOctetCnt; i++)
        {
            temp = 2 * i;
            strUUIE.append(strFn.substring(temp + 1, temp + 2));
            strUUIE.append(strFn.substring(temp, temp + 1));
        }

        return strUUIE.toString();
    }

    // input : 0005050260291210
    // output : 2006922101
    public static String vUUIE2FunctionNumber(String strUUIE)
    {
        if (strUUIE == null)
        {
            Log.error(TAG, "strFn:" + strUUIE);
            return null;
        }
        String strTmp;
        String strInvertedFn;
        int iOctetCnt = 0;

        // 00 |05 |len| 倒序的功能码 |
        // ---|---|---|--------------------------------|
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13
        // --------------------------------------------
        // 0 0 0 5 0 7 1 3 2 0 1 4 1 1 3 5 0 6 f 1 => 3102311153601
        // 0 0 0 5 0 7 1 3 2 0 1 4 1 1 3 5 0 6 1 0 => 31023111536001

        // 长度
        strTmp = strUUIE.substring(0, 2);
        iOctetCnt = Integer.parseInt(strTmp);
        if (iOctetCnt == 0)
        {
            return null;
        }
        // 取倒序的功能码
        strInvertedFn = strUUIE.substring(2);
        if (iOctetCnt > strInvertedFn.length() / 2)
        {
            return null;
        }

        StringBuffer strFn = new StringBuffer();// 功能码

        // 循环取功能码，两两倒序
        int temp;
        for (int i = 0; i < iOctetCnt; ++i)
        {
            temp = 2 * i;
            strFn.append(strInvertedFn.substring(temp + 1, temp + 2));
            strFn.append(strInvertedFn.substring(temp, temp + 1));
        }

        if (strFn.lastIndexOf("f") > -1 || strFn.lastIndexOf("F") > -1)
        {
            strFn.delete(strFn.length() - 1, strFn.length());
        }
        return strFn.toString();
    }

    /**
     * 获取成员列表
     * 
     * @param memberStr
     * @return
     */
    private static ArrayList<String> getMemberList(String memberStr)
    {
        String[] members = memberStr.split(",");
        ArrayList<String> memberList = new ArrayList<String>();
        for (String member : members)
        {
            memberList.add(member);
        }
        return memberList;
    }

    /**
     * 转换组呼数据
     * 
     * @param group
     *              接口组呼数据
     * 
     * @return 转换后的组呼数据
     */
    public static String[] getTempGroup(String[] group)
    {
        String szGroupID = "";
        String[] groupStrs = new String[10];

        if ("VGCS".equalsIgnoreCase(group[1]))
        {
            groupStrs[0] = String.valueOf(CommonConstantEntry.CALL_TYPE_GROUP);
            szGroupID = CommonConfigEntry.GROUP_NUMBER + group[2];
        }
        else if ("VBS".equalsIgnoreCase(group[1]))
        {
            groupStrs[0] = String.valueOf(CommonConstantEntry.CALL_TYPE_BROADCAST);
            szGroupID = CommonConfigEntry.BROADCAST_NUMBER + group[2];
        }
        else if ("TMP".equalsIgnoreCase(group[1]))
        {
            groupStrs[0] = String.valueOf(CommonConstantEntry.CALL_TYPE_TEMPGROUP);
            szGroupID = CommonConfigEntry.GROUP_NUMBER + group[3] + group[2];
        }

        groupStrs[1] = szGroupID;// 组号码
        groupStrs[2] = group[6];// 组名称
        groupStrs[3] = (group[4] == null || group[4].equals("")) ? "-1" : group[4];// 默认不带优先级

        groupStrs[4] = group[7];// 编组人号码
        groupStrs[5] = group[8];// 最大生存周期
        groupStrs[6] = group[9];// 成员列表
        groupStrs[7] = group[10];// 无讲者释放时长
        groupStrs[8] = group[3];// SA
        groupStrs[9] = group[11];// 创建时间

        return groupStrs;
    }

    /**
     * 压缩
     * 
     * @param src
     *          源文件路径
     * @param dest
     *          目的文件路径
     */
    public static void zip(String src, String dest)
    {
        // 提供了一个数据项压缩成一个ZIP归档输出流
        ZipOutputStream out = null;
        try
        {

            File outFile = new File(dest);// 源文件或者目录
            File fileOrDirectory = new File(src);// 压缩文件路径
            out = new ZipOutputStream(new FileOutputStream(outFile));
            // 如果此文件是一个文件，否则为false。
            if (fileOrDirectory.isFile())
            {
                zipFileOrDirectory(out, fileOrDirectory, "");
            }
            else
            {
                // 返回一个文件或空阵列。
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++)
                {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], "");
                }
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        finally
        {
            try
            {
                if (out != null)
                    out.close();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }
    }

    /**
     * 压缩一个文件或者目录下所有文件
     */
    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)
    {
        // 从文件中读取字节的输入流
        FileInputStream in = null;
        try
        {
            // 如果此文件是一个目录，否则返回false。
            if (!fileOrDirectory.isDirectory())
            {
                // 压缩文件
                byte[] buffer = new byte[4096];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);
                // 实例代表一个条目内的ZIP归档
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                // 条目的信息写入底层流
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            }
            else
            {
                // 压缩目录
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++)
                {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + "/");
                }
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        finally
        {
            try
            {
                if (in != null)
                    in.close();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }
    }

    /**
     * 解压
     * 
     * @param zipFileName
     *                  压缩文件
     * @param outputDirectory
     *                  输出路径
     */
    public static void unzip(String zipFileName, String outputDirectory)
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(zipFileName);
            Enumeration e = zipFile.entries();
            ZipEntry zipEntry = null;
            File dest = new File(outputDirectory);
            dest.mkdirs();
            while (e.hasMoreElements())
            {
                zipEntry = (ZipEntry) e.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try
                {
                    if (zipEntry.isDirectory())
                    {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);

                        File f = new File(outputDirectory + File.separator + name);
                        f.mkdirs();
                    }
                    else
                    {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1)
                        {
                            File df = new File(outputDirectory + File.separator + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1)
                        {
                            File df = new File(outputDirectory + File.separator + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        File f = new File(outputDirectory + File.separator + zipEntry.getName());
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);

                        int c;
                        byte[] by = new byte[1024];

                        while ((c = in.read(by)) != -1)
                        {
                            out.write(by, 0, c);
                        }
                        out.flush();
                    }
                }
                catch (Exception ex)
                {
                    Log.exception(TAG, ex);
                }
                finally
                {
                    try
                    {
                        if (in != null)
                            in.close();
                    }
                    catch (Exception ex)
                    {
                        Log.exception(TAG, ex);
                    }
                    try
                    {
                        if (out != null)
                            out.close();
                    }
                    catch (Exception ex)
                    {
                        Log.exception(TAG, ex);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        finally
        {
            try
            {
                if (zipFile != null)
                    zipFile.close();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }
    }

    /**
     * 主控单元控制PoC SDK设置诊断开关
     * 适用场景：机车台故障时，用户开启诊断开关；故障解除后，用户关闭诊断开关；用户可以通过访问http://机车台地址:8080/下载诊断信息
     * AndroidManifest.xml需要增加：
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * <service android:name="com.jiaxun.poc.httpserver.HttpServerService" />
     * 
     * @param bOn
     *            开/关
     * 
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     */
    public int aclDiagnosis(Context context, boolean bOn)
    {
        Log.info(TAG, "diagnosis:: bOn:" + bOn);
        if (CommonConfigEntry.diagnosis == bOn)
        {// 验证是否重复设置
            return CommonConstantEntry.METHOD_FAILED;
        }

        CommonConfigEntry.diagnosis = bOn;

//        android.content.Context context = SdkAclApplication.getContext();
//        if (context == null)
//        {// 空
//            return CommonConstantEntry.METHOD_FAILED;
//        }

        try
        {
            Intent intent = new Intent(context, HttpServerService.class);// http服务
            if (bOn)
            {// 诊断开关打开
                context.startService(intent);// 启动服务
            }
            else
            {// 诊断开关关闭
                context.stopService(intent);// 停止服务
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        return CommonConstantEntry.METHOD_SUCCESS;
    }

    /**
     * 检测网络是否与传入的地址连接上
     * 
     * @param address
     *              传入的地址
     * 
     * @return true：网络已经连接
     */
    public static boolean isConnect(String address)
    {
        if (address == null || address.equals(""))
        {// 验证参数
            return false;
        }
        boolean isConnect = false;// 是否连接上
        try
        {
            Process ping = Runtime.getRuntime().exec("/system/bin/ping -c 3 " + address);
            BufferedReader br = new BufferedReader(new InputStreamReader(ping.getInputStream()));
            String str = null;
            while ((str = br.readLine()) != null)
            {
                if (str.indexOf("time=") > -1)
                {
                    isConnect = true;
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        return isConnect;
    }
    
    /**
     * 方法说明 : 解析失败原因
     * @param reason
     * @return String
     * @author hubin
     * @Date 2015-7-14
     */
    public static String parseReleaseReason(int reason)
    {
        String result;
        switch (reason)
        {
            case CommonConstantEntry.CALL_END_CALLER_RELEASE:
                result = "主叫释放";
                break;
            case CommonConstantEntry.CALL_END_PEER_RELEASE:
                result = "被叫释放";
                break;
            case CommonConstantEntry.CALL_END_NO_RESPONSE:
                result = "无应答";
                break;
            case CommonConstantEntry.CALL_END_PEER_OFFLINE:
                result = "被叫不在线";
                break;
            case CommonConstantEntry.CALL_END_PEER_NO_RESPONSE:
                result = "被叫无应答";
                break;
            case CommonConstantEntry.CALL_END_PEER_NO_ANSWER:
                result = "被叫无响应";
                break;
            case CommonConstantEntry.CALL_END_CALL_CANCEL:
                result = "呼叫取消";
                break;
            case CommonConstantEntry.CALL_END_SPACE_OR_USER_NO_ONLINE:
                result = "空号或不在线";
                break;
            case CommonConstantEntry.CALL_END_PEER_BUSY:
                result = "被叫忙";
                break;
            case CommonConstantEntry.CALL_END_INTERCEPT:
                result = "呼叫被拦截";
                break;
            case CommonConstantEntry.CALL_END_DND:
                result = "免打扰模式";
                break;
            case CommonConstantEntry.CALL_END_HEARTBEAT_TIMEOUT:
            case CommonConstantEntry.SIP_OFFLINE:
                result = "呼叫中断";
                break;
            case CommonConstantEntry.CALL_END_EXISTED:
                result = "呼叫已存在";
                break;
            case CommonConstantEntry.CALL_END_TIMEOUT:
                result = "呼叫超时";
                break;
            case CommonConstantEntry.CALL_END_FORBID:
                result = "没有权限";
                break;
            case CommonConstantEntry.CONF_MEMBER_END_FAILED:
                result = "成员加入失败";
                break;
            case CommonConstantEntry.CONF_MEMBER_END_OFFLINE:
                result = "成员离会";
                break;
            case CommonConstantEntry.CALL_END_HUANGUP:
                result = "呼叫释放";
                break;
            case CommonConstantEntry.CALL_END_OFFLINE:
                result = "网络异常";
                break;
            default:
                result = "未知";
                break;
        }
        return result;
    }
}
