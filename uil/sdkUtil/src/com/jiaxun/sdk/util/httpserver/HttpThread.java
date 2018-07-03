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

import java.io.IOException;
import java.net.Socket;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import android.content.Context;

import com.jiaxun.sdk.util.log.Log;

/**
 * HTTP监听处理线程
 * 
 * @author fuluo
 * @Date 2014-8-21
 */
public class HttpThread extends Thread
{
    private static final String ALL_PATTERN = "*";

    private Context context = null;

    private BasicHttpProcessor httproc;
    private HttpService httpserv = null;
    private BasicHttpContext httpContext = null;
    private HttpRequestHandlerRegistry reg = null;

    Socket soket = null;
    
    private static String TAG = "HttpThread";

    public HttpThread(Context ctx, Socket soket, String threadName)
    {
        this.setContext(ctx);
        this.soket = soket;
        this.setName(threadName);
        httproc = new BasicHttpProcessor();
        httpContext = new BasicHttpContext();

        httproc.addInterceptor(new ResponseDate());
        httproc.addInterceptor(new ResponseServer());
        httproc.addInterceptor(new ResponseContent());
        httproc.addInterceptor(new ResponseConnControl());

        httpserv = new HttpService(httproc, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());

        reg = new HttpRequestHandlerRegistry();

        reg.register(ALL_PATTERN, new HomeCommandHandler(ctx));
        httpserv.setHandlerResolver(reg);
    }

    public void run()
    {
        DefaultHttpServerConnection httpserver = new DefaultHttpServerConnection();
        try
        {
            httpserver.bind(this.soket, new BasicHttpParams());
            httpserv.handleRequest(httpserver, httpContext);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        finally
        {
            try
            {
                if(httpserver != null)
                    httpserver.close();
            }
            catch (IOException e)
            {
                Log.exception(TAG, e);
            }
        }
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

}
