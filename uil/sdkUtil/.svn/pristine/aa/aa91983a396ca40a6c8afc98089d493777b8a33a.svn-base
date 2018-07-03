package com.jiaxun.sdk.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.jiaxun.sdk.util.log.Log;

import android.os.Environment;
import android.text.TextUtils;

/**
 * 导入导出文件工具类
 */
public class FileManager
{
    private final static String TAG = FileManager.class.getSimpleName();
    private static FileManager instance;

    public static FileManager getInstance()
    {
        if (instance == null)
        {
            synchronized (FileManager.class)
            {
                if (instance == null)
                {
                    instance = new FileManager();
                }
            }
        }
        return instance;
    }

    /**
     * 逐行读取文件
     */
    public List<Row> readFile(File file)
    {
        Log.info(TAG, "readFile::");
        
        try
        {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            List<Row> rows = new ArrayList<Row>();
            String rawRow;
            while ((rawRow = br.readLine()) != null)
            {
                Log.info(TAG, rawRow);
                rows.add(new Row(rawRow));
            }
            br.close();
            return rows;
        }
        catch (FileNotFoundException e)
        {
            Log.exception(TAG, e);
        }
        catch (IOException ie)
        {
            Log.exception(TAG, ie);
        }
        return null;
    }

    /**
     * 逐行写出文件
     */
    public boolean writeFile(List<String> rows, String path, String fileName)
    {
        Log.info(TAG, "writeFile::path:" + path + " fileName:" + fileName);
        File root;
        if (TextUtils.isEmpty(path))
        {
            root = Environment.getExternalStorageDirectory();
        }
        else
        {
            root = new File(path);
        }
        if (root.canWrite())
        {
            try
            {
                File file = new File(root, fileName);
                FileOutputStream fileOutPutStream = new FileOutputStream(file);
                OutputStreamWriter fw = new OutputStreamWriter(fileOutPutStream, "GBK");
                BufferedWriter out = new BufferedWriter(fw);
                for (String row : rows)
                {
                    out.write(row);
                    out.newLine();
                }
                out.flush();
                out.close();
                return true;
            }
            catch (IOException e)
            {
                Log.exception(TAG, e);
            }
        }
        return false;
    }

    /**
     * 方法说明 :
     * @param localPath  路径
     * @param fileName   文件名字
     * @param suffixName 后缀名
     * @return
     * @author chaimb
     * @Date 2015-8-19
     */
    public boolean hasLocalFile(String localPath, String fileName, String suffixName)
    {

        Log.info(TAG, "fileName::" + fileName);
        if (TextUtils.isEmpty(fileName))
        {
            Log.error(TAG, "fileName is null");
            return false;
        }

        if (TextUtils.isEmpty(suffixName))
        {
            Log.error(TAG, "suffixName is null");
            return false;
        }
        if (TextUtils.isEmpty(localPath))
        {
            Log.error(TAG, "localPath is null");
            return false;
        }
        File[] recordList = getFileLists(localPath);

        if (TextUtils.isEmpty(fileName))
        {
            return false;
        }

        if (recordList == null || recordList.length == 0)
        {
            return false;
        }

        for (int i = 0; i < recordList.length; i++)
        {
            // 判断是否为文件夹
            if (!recordList[i].isDirectory())
            {
//                long size = getFileSizes(recordList[i]);
                String filename = recordList[i].getName();
//                Log.info(TAG, "filename=" + filename + "size=" + size);
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(suffixName))
                {
                    if (filename.substring(0, filename.length() - 4).equals(fileName))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * 方法说明 :根据路径获取文件集合
     * @return
     * @author chaimb
     * @Date 2015-6-16
     */
    public File[] getFileLists(String filePath)
    {
        File record = new File(filePath);
        File[] recordList = null;
        if (record.exists())
        {
            recordList = record.listFiles();
        }
        return recordList;

    }
}
