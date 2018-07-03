/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.jiaxun.sdk.acl.line.sip.codecs;

import com.jiaxun.sdk.util.log.Log;

public class OPUS extends CodecBase implements Codec
{

    OPUS()
    {
        CODEC_NAME = "OPUS";
        CODEC_USER_NAME = "OPUS";
        CODEC_DESCRIPTION = "14kbit";//比特率
        CODEC_NUMBER = 121;
        CODEC_DEFAULT_SETTING = "wlanor3g";
        CODEC_SAMPLE_RATE = 8000;//采样频率
        CODEC_FRAME_SIZE = 320;//采样位数，40ms发一次包
        super.update();
        load();//加载库
    }

    void load()
    {
        try
        {
            System.loadLibrary("OPUS_jni");
            super.load();
        }
        catch (Exception ex)
        {
            Log.exception("OPUS", ex);
        }
    }

    public native static long initEncoder(boolean useVariableBitrate, int biteRate, int samples);

    public native static int encoder(long handle, short[] lin, int offset, byte[] bitstream, int packetCounts);

    public native static void freeEncoder(long handle);

    public native static long initDecoder();

    public native static int decoder(long handle, byte[] bitstream, int len, short[] synth_short, int i, boolean lost);

    public native static void freeDecoder(long handle);

    public void init()
    {
        load();
    }

    @Override
    public int decode(byte[] encoded, short[] lin, int size, int offset)
    {
        return 0;
    }

    @Override
    public int decode(byte[] encoded, short[] lin, int size)
    {
        return 0;
    }

    @Override
    public int encode(short[] lin, int offset, byte[] alaw, int frames)
    {
        return 0;
    }

    @Override
    public void close()
    {

    }
}
