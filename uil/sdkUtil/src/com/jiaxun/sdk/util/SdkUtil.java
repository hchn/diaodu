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
 * sdk������
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
     * ��ȡ�ն�ϵͳ�ͺ�
     */
    public static String getModel()
    {
        return android.os.Build.MODEL + " ";
    }

    /**
     * ��ȡ����ϵͳϵͳ��������ʱ��
     */
    public static long getRealTime()
    {
        return android.os.SystemClock.elapsedRealtime();
    }

    /**
     * �Ƿ�����·��������
     * 
     * @param number
     *              ����
     * @param priority
     *              ���ȼ�
     * @return
     */
    public static boolean checkEmergencyCall(String number, int priority)
    {
        if (number == null || number.equals(""))
        {// ��֤����
            return false;
        }
        if (priority < CommonConfigEntry.PRIORITY_MIN || priority > CommonConfigEntry.PRIORITY_MAX)
        {// ��֤����
            return false;
        }

        if (checkWeatherGroupCall(number) && (number.endsWith(CommonConfigEntry.URGENCY_CALLER) || priority == 0)
                && (number.length() == 5 || number.length() == 10))
        {// 0���������299
            return true;
        }
        return false;
    }

    /*
     * ���ݵ绰�����жϻ�·�Ǹ����������
     * ������false ���:true
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
     * ����˵�� : ͨ�������жϺ�����������
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
            // ���������貹F
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

        // 00 |05 |len| ����Ĺ����� |
        // ---|---|---|--------------------------------|
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13
        // --------------------------------------------
        // 0 0 0 5 0 7 1 3 2 0 1 4 1 1 3 5 0 6 f 1 => 3102311153601
        // 0 0 0 5 0 7 1 3 2 0 1 4 1 1 3 5 0 6 1 0 => 31023111536001

        // ����
        strTmp = strUUIE.substring(0, 2);
        iOctetCnt = Integer.parseInt(strTmp);
        if (iOctetCnt == 0)
        {
            return null;
        }
        // ȡ����Ĺ�����
        strInvertedFn = strUUIE.substring(2);
        if (iOctetCnt > strInvertedFn.length() / 2)
        {
            return null;
        }

        StringBuffer strFn = new StringBuffer();// ������

        // ѭ��ȡ�����룬��������
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
     * ��ȡ��Ա�б�
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
     * ת���������
     * 
     * @param group
     *              �ӿ��������
     * 
     * @return ת������������
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

        groupStrs[1] = szGroupID;// �����
        groupStrs[2] = group[6];// ������
        groupStrs[3] = (group[4] == null || group[4].equals("")) ? "-1" : group[4];// Ĭ�ϲ������ȼ�

        groupStrs[4] = group[7];// �����˺���
        groupStrs[5] = group[8];// �����������
        groupStrs[6] = group[9];// ��Ա�б�
        groupStrs[7] = group[10];// �޽����ͷ�ʱ��
        groupStrs[8] = group[3];// SA
        groupStrs[9] = group[11];// ����ʱ��

        return groupStrs;
    }

    /**
     * ѹ��
     * 
     * @param src
     *          Դ�ļ�·��
     * @param dest
     *          Ŀ���ļ�·��
     */
    public static void zip(String src, String dest)
    {
        // �ṩ��һ��������ѹ����һ��ZIP�鵵�����
        ZipOutputStream out = null;
        try
        {

            File outFile = new File(dest);// Դ�ļ�����Ŀ¼
            File fileOrDirectory = new File(src);// ѹ���ļ�·��
            out = new ZipOutputStream(new FileOutputStream(outFile));
            // ������ļ���һ���ļ�������Ϊfalse��
            if (fileOrDirectory.isFile())
            {
                zipFileOrDirectory(out, fileOrDirectory, "");
            }
            else
            {
                // ����һ���ļ�������С�
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++)
                {
                    // �ݹ�ѹ��������curPaths
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
     * ѹ��һ���ļ�����Ŀ¼�������ļ�
     */
    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)
    {
        // ���ļ��ж�ȡ�ֽڵ�������
        FileInputStream in = null;
        try
        {
            // ������ļ���һ��Ŀ¼�����򷵻�false��
            if (!fileOrDirectory.isDirectory())
            {
                // ѹ���ļ�
                byte[] buffer = new byte[4096];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);
                // ʵ������һ����Ŀ�ڵ�ZIP�鵵
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                // ��Ŀ����Ϣд��ײ���
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            }
            else
            {
                // ѹ��Ŀ¼
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++)
                {
                    // �ݹ�ѹ��������curPaths
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
     * ��ѹ
     * 
     * @param zipFileName
     *                  ѹ���ļ�
     * @param outputDirectory
     *                  ���·��
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
     * ���ص�Ԫ����PoC SDK������Ͽ���
     * ���ó���������̨����ʱ���û�������Ͽ��أ����Ͻ�����û��ر���Ͽ��أ��û�����ͨ������http://����̨��ַ:8080/���������Ϣ
     * AndroidManifest.xml��Ҫ���ӣ�
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * <service android:name="com.jiaxun.poc.httpserver.HttpServerService" />
     * 
     * @param bOn
     *            ��/��
     * 
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     */
    public int aclDiagnosis(Context context, boolean bOn)
    {
        Log.info(TAG, "diagnosis:: bOn:" + bOn);
        if (CommonConfigEntry.diagnosis == bOn)
        {// ��֤�Ƿ��ظ�����
            return CommonConstantEntry.METHOD_FAILED;
        }

        CommonConfigEntry.diagnosis = bOn;

//        android.content.Context context = SdkAclApplication.getContext();
//        if (context == null)
//        {// ��
//            return CommonConstantEntry.METHOD_FAILED;
//        }

        try
        {
            Intent intent = new Intent(context, HttpServerService.class);// http����
            if (bOn)
            {// ��Ͽ��ش�
                context.startService(intent);// ��������
            }
            else
            {// ��Ͽ��عر�
                context.stopService(intent);// ֹͣ����
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        return CommonConstantEntry.METHOD_SUCCESS;
    }

    /**
     * ��������Ƿ��봫��ĵ�ַ������
     * 
     * @param address
     *              ����ĵ�ַ
     * 
     * @return true�������Ѿ�����
     */
    public static boolean isConnect(String address)
    {
        if (address == null || address.equals(""))
        {// ��֤����
            return false;
        }
        boolean isConnect = false;// �Ƿ�������
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
     * ����˵�� : ����ʧ��ԭ��
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
                result = "�����ͷ�";
                break;
            case CommonConstantEntry.CALL_END_PEER_RELEASE:
                result = "�����ͷ�";
                break;
            case CommonConstantEntry.CALL_END_NO_RESPONSE:
                result = "��Ӧ��";
                break;
            case CommonConstantEntry.CALL_END_PEER_OFFLINE:
                result = "���в�����";
                break;
            case CommonConstantEntry.CALL_END_PEER_NO_RESPONSE:
                result = "������Ӧ��";
                break;
            case CommonConstantEntry.CALL_END_PEER_NO_ANSWER:
                result = "��������Ӧ";
                break;
            case CommonConstantEntry.CALL_END_CALL_CANCEL:
                result = "����ȡ��";
                break;
            case CommonConstantEntry.CALL_END_SPACE_OR_USER_NO_ONLINE:
                result = "�պŻ�����";
                break;
            case CommonConstantEntry.CALL_END_PEER_BUSY:
                result = "����æ";
                break;
            case CommonConstantEntry.CALL_END_INTERCEPT:
                result = "���б�����";
                break;
            case CommonConstantEntry.CALL_END_DND:
                result = "�����ģʽ";
                break;
            case CommonConstantEntry.CALL_END_HEARTBEAT_TIMEOUT:
            case CommonConstantEntry.SIP_OFFLINE:
                result = "�����ж�";
                break;
            case CommonConstantEntry.CALL_END_EXISTED:
                result = "�����Ѵ���";
                break;
            case CommonConstantEntry.CALL_END_TIMEOUT:
                result = "���г�ʱ";
                break;
            case CommonConstantEntry.CALL_END_FORBID:
                result = "û��Ȩ��";
                break;
            case CommonConstantEntry.CONF_MEMBER_END_FAILED:
                result = "��Ա����ʧ��";
                break;
            case CommonConstantEntry.CONF_MEMBER_END_OFFLINE:
                result = "��Ա���";
                break;
            case CommonConstantEntry.CALL_END_HUANGUP:
                result = "�����ͷ�";
                break;
            case CommonConstantEntry.CALL_END_OFFLINE:
                result = "�����쳣";
                break;
            default:
                result = "δ֪";
                break;
        }
        return result;
    }
}
