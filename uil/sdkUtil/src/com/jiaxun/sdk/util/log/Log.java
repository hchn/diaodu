/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * 
 * This file is part of MjSip (http://www.mjsip.org)
 * 
 * MjSip is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * MjSip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 */

package com.jiaxun.sdk.util.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

// import java.util.Locale;
// import java.text.DateFormat;
// import java.text.SimpleDateFormat;

/**
 * Class Log allows the printing of log messages onto standard output or files
 * or any PrintStream.
 * <p>
 * Every Log has a <i>verboselevel</i> associated with it; any log request with
 * <i>loglevel</i> less or equal to the <i>verbose-level</i> is logged. <br>
 * Verbose level 0 indicates no log. The log levels should be greater than 0.
 * <p>
 * Parameter <i>logname</i>, if non-null, is used as log header (i.e. written
 * at the begin of each log row).
 */
public class Log
{
    /** ***************************** Attributes ****************************** */

    /** (static) Default maximum log file size */
    public static int MAX_SIZE = 40; // MB

    /** The log output stream */
    private static PrintStream out_stream;

    /** ���ڸ�ʽ    */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    
    /** ��Ϣ����    */
    private static BlockingQueue<String> logQueue = new LinkedBlockingQueue<String>();
    
    /** ��¼��־�߳�    */
    private static Thread logThread = null;
    
    /** ��־�ļ�    */
    private static File logFile = null;
    
    /** The log input stream    */
    private static FileInputStream input_stream = null;
    
    /** ���������־��С����־�����ļ�    */
    private static String bakLogName = ".bak";
    
    /** ��־�����������    */
    private static int maxLogQueueSize = 2000;
    
    /**
     * ���ص�Ԫ����PoC SDK������־�ļ��洢·��
     * 
     * @param szLogPath
     *            ��־·��(·���������ļ���)
     * @param szLogName
     *            ��־�ļ���
     * @param nMaxSize
     *            �����������λ��M
     * 
     * @return ServiceConstant.METHOD_SUCCESS���ɹ����ã�
     *         ServiceConstant.METHOD_FAILED����������ʧ�ܣ�
     *         ServiceConstant.PARAM_ERROR����������
     */
    public static int startLogService(String szLogPath, String szLogName, int nMaxSize)
    {
        Log.info("LogSystem", "setLogPath:: szLogPath:" + szLogPath + " szLogName:" + szLogName + " nMaxSize:" + nMaxSize);
        CommonConfigEntry.LOG_FILEPATH = szLogPath;
        CommonConfigEntry.LOG_NAME = szLogName;
        CommonConfigEntry.TEST_RECORDEINGS_PATH = CommonConfigEntry.LOG_FILEPATH + "/recordings/";
        CommonConfigEntry.LOG_MAXSIZE = nMaxSize;
        Log.startLogFile();
        return CommonConstantEntry.METHOD_SUCCESS;
    }
    
    /**
     * ������¼��־�ļ�
     */
    public static void startLogFile()
    {
    	if(logThread != null)
    	{
    		return;
    	}
    	
    	initLog();//��ʼ��
    	
    	logThread = new Thread()
    	{
			@Override
			public void run()
			{
			    int count = 0;//������
				while(CommonConfigEntry.LOG_OUTFILE)
				{
					try
					{
						String logStr = logQueue.take();
						if(CommonConfigEntry.LOG_OUTFILE)
						{//��¼��־�ļ�
						    checkLogExists();//�����־�ļ��Ƿ���ڣ������������´���
							out_stream.print(logStr);//д��
							if(count % 100 == 0)
							{//ÿn�μ��һ����־
							    checkLogSize();//�����־��С
							}
							count++;
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					    break;
					}
				}
				logThread = null;//������־����ÿ�
			}
    	};
    	logThread.start();
    }
    
    /**
     * �����־�ļ��Ƿ���ڣ�����������򴴽�
     */
    private static void checkLogExists()
    {
        if(!logFile.exists())
        {//��־�ļ������ڣ���ɾ����
            try
            {
                out_stream.close();//�ر������
                input_stream.close();//�ر�������
                
                initLog();//���³�ʼ����־
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * �����־�ļ���С����ֹ��־�ļ�����ռ�ô洢�ռ�
     */
    private static void checkLogSize()
    {
        try
        {
            int size = input_stream.available() / (1024 * 1024);//��С
            
            if(size >= MAX_SIZE)
            {//������С
                logFile.renameTo(new File(CommonConfigEntry.LOG_FILEPATH + CommonConfigEntry.LOG_NAME + bakLogName));//����
                logFile.delete();
                initLog();//���³�ʼ��
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ��ʼ����־����
     */
    private static void initLog()
    {
        try
        {
        	MAX_SIZE = CommonConfigEntry.LOG_MAXSIZE;//������־��С
            new File(CommonConfigEntry.LOG_FILEPATH).mkdirs();//����Ŀ¼
            logFile = new File(CommonConfigEntry.LOG_FILEPATH + CommonConfigEntry.LOG_NAME);
            if(!logFile.exists())
            {//��־�ļ������ڣ��򴴽�
                logFile.createNewFile();
            }
            //׷��д�룬�Զ�flush
            out_stream = new PrintStream(new FileOutputStream(logFile, true), true);
            //��־��ȡ�������ڻ�ȡ��־�ļ���С
            input_stream = new FileInputStream(logFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ��¼�쳣��־
     * 
     * @param tag
     *          ��ʶ
     * @param e
     *          �쳣����
     */
    public static void exception(String tag, Exception e)
    {
        if(tag == null || e == null)
        {// ������֤
            return;
        }
        log(tag, ExceptionPrinter.getStackTraceOf(e), LogLevel.EXCEPTION);
    }
    
    /**
     * ��¼�쳣��־
     * 
     * @param tag
     *          ��ʶ
     * @param e
     *          �쳣����
     */
    public static void throwable(String tag, Throwable e)
    {
        if(tag == null || e == null)
        {// ������֤
            return;
        }
        log(tag, ExceptionPrinter.getStackTraceOf(e), LogLevel.EXCEPTION);
    }

    /**
     * ��¼������־
     * 
     * @param tag
     *          ��ʶ
     * @param message
     *          ��־��Ϣ
     */
    public static void error(String tag, String message)
    {
        if(tag == null || message == null)
        {// ������֤
            return;
        }
        log(tag, message, LogLevel.ERROR);
    }
    
    /**
     * ��¼��Ϣ��־
     * 
     * @param tag
     *          ��ʶ
     * @param message
     *          ��־��Ϣ
     * @param show
     *          �Ƿ�����¼
     */
    public static void info(String tag, String message)
    {
        if(tag == null || message == null)
        {// ������֤
            return;
        }
        log(tag, message, LogLevel.INFO);
    }

    /**
     * ��¼������־
     * 
     * @param tag
     *          ��ʶ
     * @param message
     *          ��־��Ϣ
     */
//    public static void debug(String tag, String message)
//    {
//        if(tag == null || message == null)
//        {// ������֤
//            return;
//        }
//        if(CommonConfigEntry.LOG_DEBUG)
//        {
//            log(tag, message, LogLevel.DEBUG);
//        }
//    }

    /**
     * �����־
     * 
     * @param logStr
     *          ��־��Ϣ
     * @param level
     *          ��־����
     */
    private static void log(String tag, String message, int level)
    {
        try
        {
            String log = packLog(tag, message, level);
            
            if(CommonConfigEntry.LOG_OUTFILE)
            {//������ļ�
            	try
            	{
					if(logQueue.size() == maxLogQueueSize)
					{// ����������
						logQueue.poll();
					}
					logQueue.add(log);
				}
            	catch (Exception e)
            	{
					e.printStackTrace();
				}
            }
            
            if(CommonConfigEntry.LOG_LOGCAT)
            {//���������̨
                logcat_Android(tag, message, level);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ��װ��־
     */
    private static String packLog(String tag, String message, int level)
    {
        StringBuffer logsb = new StringBuffer();
        logsb.append(sdf.format(new Date())).append(": ");
//        logsb.append(System.currentTimeMillis()).append(": ");
        logsb.append(Thread.currentThread().getId()).append(": ");
        if (level == LogLevel.DEBUG)
        {
            logsb.append("DEBUG: ");
        }
        else if (level == LogLevel.INFO)
        {
            logsb.append("INFO: ");
        }
        else if (level == LogLevel.EXCEPTION)
        {
            logsb.append("EXCEPTION: ");
        }
        else if (level == LogLevel.ERROR)
        {
            logsb.append("ERROR: ");
        }
        logsb.append(tag).append(":");
        logsb.append(message).append("\r\n");
        
        return logsb.toString();
    }
    
    /**
     * �����console
     */
    private static void log_Console(String log)
    {
        System.out.print(log);//�����console
    }
    
    /**
     * �����logcat
     */
    private static void logcat_Android(String tag, String message, int level)
    {
        if (level == LogLevel.DEBUG)
        {
            android.util.Log.d(tag, message);
        }
        if (level == LogLevel.INFO)
        {
            android.util.Log.i(tag, message);
        }
        else if (level == LogLevel.EXCEPTION)
        {
            android.util.Log.w(tag, message);
        }
        else if (level == LogLevel.ERROR)
        {
            android.util.Log.e(tag, message);
        }
    }

}
