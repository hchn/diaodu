package com.jiaxun.sdk.util.version;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.httpdownload.HttpDownload;
import com.jiaxun.sdk.util.log.Log;

/**
 * 终端版本获取
 * 
 * @author fuluo
 */
public class VersionClient
{
    private static String TAG = "PocVersion";

    private String newVersionFileName;// 新版本文件名
    
    private Context context;
    
    private HttpDownload httpDownload;// HTTP下载

    public VersionClient(Context context)
    {
        this.context = context;
        httpDownload = new HttpDownload(context);
    }

    /**
     * 获取终端最新的版本号
     * 
     * @param serverAddr
     *                  版本服务器地址，IP或域名
     * @param port
     *                  版本服务器端口
     * @param module
     *                  模块名称
     * 
     * @return 最新版本
     */
    public String getLatestVersion(final String serverAddr, final int port, final String module)
    {
        try
        {
            return getLatestVersion("http://" + serverAddr + ":" + port + "/version/" + module);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return "";
        }
    }

    /**
     * 下载终端最新的APK，支持断点续传
     * 
     * @param serverAddr
     *                  版本服务器地址，IP或域名
     * @param port
     *                  版本服务器端口
     * @param module
     *                  模块名称
     */
    public void downloadApk(final String serverAddr, final int port, final String module)
    {
        String downloadUrl = "http://" + serverAddr + ":" + port + "/download/" + module + "?version=" + newVersionFileName;
        httpDownload.downloadByBreakpoint(downloadUrl, CommonConfigEntry.DOWNLOAD_PATH + newVersionFileName + ".apk");
    }

    /**
     * 安装apk
     * 
     * @param file
     *          文件名称
     * @param context
     *          上下文对象
     */
    public void installerApk(String file, Context context)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取终端最新版本，通过HTTP协议get方法
     */
    private String getLatestVersion(String versionUrl)
    {
        if (versionUrl == null || versionUrl.equals(""))
        {// 验证参数
            return newVersionFileName;
        }
        try
        {
            HttpGet getMethod = new HttpGet(versionUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(getMethod); // 发起GET请求
            int resCode = response.getStatusLine().getStatusCode();// 获取响应码
            Log.info(TAG, "resCode = " + resCode);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");// 获取服务器响应内容
            Log.info(TAG, "result = " + result);

            if (resCode == 200 && result != null)
            {// 成功
                Element root = parse(result);
                if (root != null)
                {// 解析成功
                    NodeList versionInfoNodeList = root.getElementsByTagName("VersionInfo");
                    if (versionInfoNodeList != null && versionInfoNodeList.getLength() > 0)
                    {// 存在版本
                        NodeList versionNodeList = versionInfoNodeList.item(0).getChildNodes();
                        if (versionNodeList != null && versionNodeList.getLength() > 0)
                        {// 存在版本编号
                            for (int i = 0; i < versionNodeList.getLength(); i++)
                            {
                                Node versionNode = versionNodeList.item(i);
                                if (versionNode.getNodeType() == Node.ELEMENT_NODE && versionNode.getNodeName().equals("version"))
                                {
                                    newVersionFileName = versionNode.getTextContent().trim();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.error(TAG, "getLatestVersion error.");
            Log.exception(TAG, e);
        }
        return newVersionFileName;
    }
    
    /**
     * 解析xml字符串
     * 
     * @param xml
     *          xml字符串
     * 
     * @return 根目录Element
     */
    public Element parse(String xml)
    {
        Element root = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            root = document.getDocumentElement();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        return root;
    }

    /**
     * 是否需要更新，比较最新版本号是否比当前使用版本号大
     * 
     * @param curVersion
     *                  当前使用的版本号
     * @param newVersion
     *                  最新版本号
     * @return true:更新
     */
    public boolean compareVersion(String curVersion, String newVersion)
    {
        if (newVersion == null && "".equals(newVersion))
        {
            return false;
        }
        // 截取M6100V3.0.10到61003010
        String regex = "[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher curMatcher = pattern.matcher(curVersion);
        Matcher newMatcher = pattern.matcher(newVersion);
        curVersion = curMatcher.replaceAll("").trim();
        newVersion = newMatcher.replaceAll("").trim();

        for (int i = 0; i < curVersion.length(); i++)
        {
            // 如果存在新版本号大于旧版本号，则说明需要更新
            if (newVersion.charAt(i) > curVersion.charAt(i))
            {
                return true;
            }
            // 如果新版本号小于旧版本号，则不需要升级
            else if (newVersion.charAt(i) < curVersion.charAt(i))
            {
                return false;
            }
        }

        // 相同位数的版本号比对一致，如果新版本号存在其他小版本，则需要更新
        if (newVersion.length() > curVersion.length())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
