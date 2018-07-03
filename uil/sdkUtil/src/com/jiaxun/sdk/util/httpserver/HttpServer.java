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

import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;

import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * HTTP·þÎñÆ÷
 * 
 * @author fuluo
 * @Date 2014-8-21
 */
public class HttpServer extends Thread
{
    private Context ctx;
    private boolean RUN = false;
    
    private static String TAG = "HttpServer";

    public HttpServer(Context context, String threadName)
    {
        this.ctx = context;
        this.setName(threadName);
    }

    @Override
    public void run()
    {
        super.run();
        ServerSocket server = null;
        Socket soket = null;
        try
        {
            server = new ServerSocket(CommonConfigEntry.HTTP_PORT);
            server.setReuseAddress(true);
            RUN = true;
            while (RUN)
            {
                try
                {
                    soket = server.accept();
                    Log.info(TAG, "client connected!");

                    Thread httpthread = new HttpThread(ctx, soket, "httpthread");
                    httpthread.start();
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
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
                if (soket != null)
                    soket.close();
                if (server != null)
                    server.close();
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
    }

    public void stopServer()
    {
        RUN = false;
    }

}