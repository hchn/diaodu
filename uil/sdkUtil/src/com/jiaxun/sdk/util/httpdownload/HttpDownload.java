package com.jiaxun.sdk.util.httpdownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：HTTP协议下载
 *
 * @author  fuluo
 *
 * @Date 2015-5-18
 */
public class HttpDownload
{
    private static String TAG = "HttpDownload";

    private Context context;

    private boolean isDownloadApk;// 是否下载

    private String downloadId = "";

    public HttpDownload(Context context)
    {
        this.context = context;
    }

    /**
     * 下载文件直到下载成功，不支持断点下载
     * 
     * @param downloadUrl
     *            下载URL
     * @param file
     *            下载文件完整路径带文件名
     */
    public void download(final String downloadUrl, final String file, String downloadId)
    {
        Log.info(TAG, "download::downloadUrl:" + downloadUrl + " file:" + file);
        if (isDownloadApk)
            return;
        isDownloadApk = true; // 开始下载

        this.downloadId = downloadId;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    downloadFile(downloadUrl, file);
                }
                catch (Exception ex)
                {
                    Log.exception(TAG, ex);
                }
                isDownloadApk = false;// 下载完毕
            }
        }).start();
    }

    /**
     * 下载文件直到下载成功，支持断点下载（服务器需要支持断点续传）
     * 
     * @param downloadUrl
     *            下载URL
     * @param file
     *            下载文件完整路径带文件名
     */
    public void downloadByBreakpoint(final String downloadUrl, final String file)
    {
        Log.info(TAG, "downloadByBreakpoint::downloadUrl:" + downloadUrl + " file:" + file);
        if (isDownloadApk)
            return;
        isDownloadApk = true; // 开始下载

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    downloadFileByBreakpoint(downloadUrl, file);
                }
                catch (Exception ex)
                {
                    Log.exception(TAG, ex);
                }
                isDownloadApk = false;// 下载完毕
            }
        }).start();
    }

    /**
     * 下载文件，支持断点续传
     * 
     * @param downloadUrl
     *            下载URL
     * @param file
     *            下载文件完整路径带文件名
     */
    private void downloadFileByBreakpoint(String downloadUrl, String file)
    {
        Log.info(TAG, "downloadFile::downloadUrl:" + downloadUrl + " file:" + file);
        if (downloadUrl == null || downloadUrl.equals(""))
        {// 验证参数
            return;
        }

        HttpURLConnection urlConn = null;
        InputStream is = null;
        int offset = 0;// 已传文件大小
        File downloadTmp = null;// 下载文件
        int fileLength = 0;// 文件大小

        try
        {
            downloadTmp = new File(file + ".tmp");
            if (!downloadTmp.exists())
            {// 不存在
                downloadTmp.createNewFile();// 创建
            }
            offset = (int) downloadTmp.length();// 已传文件大小

            URL url = new URL(downloadUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            if (offset > 0)
                urlConn.setRequestProperty("Range", "bytes=" + offset + "-");// 设置下载点
            int responseCode = urlConn.getResponseCode();
            Log.info(TAG, "downloadApk responseCode:" + responseCode);
            if (responseCode != 200 && responseCode != 206)
                return;// 不是正确下载返回码，则退出

            fileLength = urlConn.getContentLength() + offset;// 下载文件大小
            int readLength = 32768;// 每次需要读取的字节数
            byte[] bs = new byte[readLength];// 读取字节数组
            Log.info(TAG, "downloadApk readLength:" + readLength + " fileLength:" + fileLength);
            is = urlConn.getInputStream();// 下载输入流

            RandomAccessFile raf = new RandomAccessFile(downloadTmp, "rw");// 随机存储文件
            if (offset > 0)
            {// 已有下载
                raf.seek(offset);// 写入点
                notifyDownLoadProgess(offset, fileLength);// 通知“文件下载进度”
            }

            int result = 0;// 每次读取到个字节数
            while (isDownloadApk && offset < fileLength)
            {// 直到读取完毕
                result = is.read(bs, 0, readLength);
                if (result == -1)
                    break;
                offset += result;
                if (fileLength - offset < readLength)
                    readLength = fileLength - offset;
                raf.write(bs, 0, result);
                if (offset < fileLength)
                {// 下载中
                    notifyDownLoadProgess(offset, fileLength);// 通知“文件下载进度”
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        finally
        {
            try
            {
                if (is != null)
                    is.close();
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
            try
            {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }

            Log.info(TAG, "downloadApk.length:" + downloadTmp.length() + "fileLength:" + fileLength);
            if (downloadTmp.length() == fileLength)
            {// 下载完成
                downloadTmp.renameTo(new File(file));// 改名
                // 通知“文件下载完毕”
                notifyDownLoadProgess(offset, fileLength);
            }
        }
    }

    /**
     * 下载文件，每次删除后再下载
     * 
     * @param downloadUrl
     *            下载URL
     * @param file
     *            下载文件完整路径带文件名
     */
    private void downloadFile(String downloadUrl, String file)
    {
        Log.info(TAG, "downloadApk::downloadUrl:" + downloadUrl);
        if (downloadUrl == null || downloadUrl.equals(""))
        {// 验证参数
            return;
        }

        HttpURLConnection urlConn = null;
        FileOutputStream fis = null;
        File downloadFile = null;

        try
        {
            URL url = new URL(downloadUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            InputStream is = urlConn.getInputStream();// 下载输入流

            downloadFile = new File(file);
            downloadFile.delete();// 先删除
            downloadFile.createNewFile();// 后创建
            fis = new FileOutputStream(downloadFile, true);// 下载文件输出流，追加写入

            int offset = 0;// 已传文件大小
            int fileLength = urlConn.getContentLength();// 文件大小
            int readLength = 32768;// 每次读取的字节数
            byte[] bs = new byte[readLength];// 读取字节数组
            int result = 0;// 每次读取到个字节数
            while (isDownloadApk && (result = is.read(bs, 0, readLength)) != -1)
            {// 直到读取完毕
                offset += result;
                fis.write(bs, 0, result);
                // 通知“文件下载进度”
                notifyDownLoadProgess(offset, fileLength);
            }

            if (downloadFile.length() == fileLength)
            {// 下载完成
                downloadFile.renameTo(new File(file));// 改名
            }
            else if (!isDownloadApk)
            {// 取消下载
                downloadFile.delete();
            }
        }
        catch (IOException e)
        {
            Log.exception(TAG, e);
        }
        finally
        {
            if (urlConn != null)
            {
                try
                {
                    urlConn.disconnect();
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    Log.exception(TAG, e);
                }
            }
        }
    }

    /**
     * 通知下载进度
     */
    private void notifyDownLoadProgess(int offset, int fileLength)
    {
        Intent intent = new Intent();
        intent.setAction(CommonEventEntry.EVENT_PROGRESS_CHANGE);
        intent.putExtra(CommonEventEntry.PARAM_PROGRESS_MAX, fileLength);
        intent.putExtra(CommonEventEntry.PARAM_PROGRESS, offset);
        intent.putExtra(CommonEventEntry.DOWNLOAD_ID, downloadId);
        context.sendBroadcast(intent);
    }

    /**
     * 停止下载
     */
    public void halt()
    {
        this.downloadId = "";
        isDownloadApk = false;
    }

}
