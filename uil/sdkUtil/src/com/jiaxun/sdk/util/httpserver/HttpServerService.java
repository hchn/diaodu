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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * HTTP·þÎñ
 * 
 * @author fuluo
 * @Date 2014-8-21
 */
public class HttpServerService extends Service
{
    private HttpServer server = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        server = new HttpServer(this, "httpserver");
    }

    @Override
    public void onDestroy()
    {
        server.stopServer();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        server.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }

}
