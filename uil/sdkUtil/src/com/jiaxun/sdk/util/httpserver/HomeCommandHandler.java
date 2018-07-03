/*
 * Copyright (C) 2012 Alvin Aditya H,
 * Shanti F,
 * Selviana
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package com.jiaxun.sdk.util.httpserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.FileEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import android.content.Context;

import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * HTTP服务器主界面
 * 
 * @author fuluo
 * @Date 2014-8-21
 */
public class HomeCommandHandler implements HttpRequestHandler
{
    private Context context = null;
    private String host = "localhost";
    private String FOLDER_SHARE_PATH = CommonConfigEntry.LOG_FILEPATH;
    
    private static String TAG = "HomeCommandHandler";

    public HomeCommandHandler(Context context)
    {
        this.context = context;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp, HttpContext arg2) throws HttpException, IOException
    {
        this.host = req.getFirstHeader("Host").getValue();
        String uri = req.getRequestLine().getUri();
        Log.info(TAG, "host : " + host + " uri:" + uri);
        HttpEntity entity = getEntityFromUri(uri, resp);
        resp.setHeader("Content-Type", "text/html; charset=GBK");
        resp.setEntity(entity);
    }
    
    /**
     * 处理请求，返回信息
     */
    private HttpEntity getEntityFromUri(String uri, HttpResponse response)
    {
        String contentType = "text/html; charset=GBK";
        String filepath = FOLDER_SHARE_PATH;
        if (uri.equalsIgnoreCase("/") || uri.length() <= 0)
        {
            filepath = FOLDER_SHARE_PATH + "/";
        }
        else
        {
            filepath = URLDecoder.decode(uri);
        }
        Log.info(TAG, "uri:" + uri + " filepath:" + filepath);

        final File file = new File(filepath);
        HttpEntity entity = null;
        if (file.isDirectory())
        {//  目录：列表展示
            entity = new EntityTemplate(new ContentProducer()
            {
                public void writeTo(final OutputStream outstream) throws IOException
                {
                    OutputStreamWriter writer = new OutputStreamWriter(outstream, "GBK");
                    String resp = getDirListingHTML(file);
                    writer.write(resp);
                    writer.flush();
                }
            });
            response.setHeader("Content-Type", contentType);
        }
        else if (file.exists())
        {// 文件：下载
            String zipFilepath = filepath + ".zip";
            SdkUtil.zip(filepath, filepath + ".zip");
            File zipFile = new File(zipFilepath);
            contentType = URLConnection.guessContentTypeFromName(zipFile.getAbsolutePath());
            entity = new FileEntity(zipFile, contentType);
            response.setHeader("Content-Type", contentType);
        }
        else
        {// 无：错误
            entity = new EntityTemplate(new ContentProducer()
            {
                public void writeTo(final OutputStream outstream) throws IOException
                {
                    OutputStreamWriter writer = new OutputStreamWriter(outstream, "GBK");
                    String resp = "<html>" + "<head><title>ERROR : NOT FOUND</title></head>" + "<body>"
                            + "<center><h1>FILE OR DIRECTORY NOT FOUND !</h1></center>" + "<p>Sorry, file or directory you request not available<br />"
                            + "Contact your administrator<br />" + "</p>" + "</body></html>";

                    writer.write(resp);
                    writer.flush();
                }
            });
            response.setHeader("Content-Type", "text/html");
        }

        return entity;
    }

    /**
     * 列表展示本文件夹下所有文件和目录
     */
    private String getDirListingHTML(File file)
    {
        StringBuffer buff = new StringBuffer();
        if (file == null || !file.isDirectory() || !file.canRead())
        {
            buff.append("<html><head></head><body>" + "<center><p><font color=\"red\">Permission Denied or Error Reading Directory</font><br />"
                    + "</p></center>" + "</body>" + "</html>");
            return buff.toString();
        }

        File[] files = file.listFiles();
        for (File tempFile : files)
        {
            if(tempFile.getName().endsWith("zip"))
            {
                tempFile.delete();// 删除
            }
        }
        files = file.listFiles();
        Arrays.sort(files);
        buff.append("<html><head><title>Directory Listing</title></head>" + "<body><table border=\"0\" width=\"100%\">"
                + "<tr bgcolor=\"silver\" align=\"center\"><td>file name</td><td>size</td><td>type</td></tr>");

        for (File f : files)
        {
            buff.append("<tr bgcolor=\"" + (f.isDirectory() ? "orange" : "white") + "\">");
            buff.append("<td><a href=\"http://");
            buff.append(this.host);
            buff.append(f.getAbsolutePath().replaceFirst(FOLDER_SHARE_PATH + "/", "") + "\">" + (f.isDirectory() ? f.getName() : f.getName())
                    + "</a><br /></td>");
            buff.append("<td align=\"right\">" + (f.isFile() ? formatByte(f.length(), true) : "") + "</td>");
            buff.append("<td><center>" + (f.isDirectory() ? "dir" : "file") + "</center></td>");
            buff.append("</tr>");
        }

        buff.append("</table>" + "</body></html>");
        return buff.toString();
    }

    private String formatByte(long bytes, boolean si)
    {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
