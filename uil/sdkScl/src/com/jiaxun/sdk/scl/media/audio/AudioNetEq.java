package com.jiaxun.sdk.scl.media.audio;

import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.log.Log;

/**
 * “Ù∆µÕ¯¬Á  ”¶–‘
 *
 * @author  fuluo
 *
 * @Date 2015-7-30
 */
public class AudioNetEq
{
    private static String TAG = "poc";
    
    private static final String DATA_FOLDER = String.format("/data/data/%s", SdkUtil.getApplicationContext().getPackageName());
    private static final String LIBS_FOLDER = String.format("%s/lib", DATA_FOLDER);
    
    public static void init()
    {
        try
        {
            System.load(String.format("%s/%s", LIBS_FOLDER, "libneteqjni.so"));
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
    }

    public native static long Init();
    
    public native static int Release(long handle);
    
    public native static int RecIn(long handle, byte[] input, int len);
    
    public native static int RecOut(long handle, short[] output);

}
