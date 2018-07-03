package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：媒体配置
 *
 * @author  hubin
 *
 * @Date 2015-7-13
 */
public class MediaConfig implements Parcelable
{
    // Audio Config
    /** RTP音频端口号   */
    public int audioPort;
    /** RTP最大音频端口号   */
    public int audioMaxPort;
    /** 排在首位的是优选编码方式，1:G.711 2:G.729 3:G.723 4:G.722 5:OP8 6: OP16 */
    public int[] audioCodecs;
    /** 打包时间（MS），按照优选编码顺序排列 */
    public int[] audioPacketTime;
    /** DTMF config: 1: inband 2: sip-info 3: RFC2833 */
    public int dtmfMode;
    /** 来呼提示音 */
    public String incomingCallVoice;

    // Video Config
    /** RTP视频端口号   */
    public int videoPort;
    /** RTP最大视频端口号   */
    public int videoMaxPort;
    /** 视频宽度  */
    public int videoWidth;
    /** 视频高度  */
    public int videoHeight;
    /** 视频帧率15/25/30 */
    public int videoFrameRate;
    /** 视频码率 */
    public int videoBitRate;
    /** 排在首位的是优选编码方式，1:H.264 2:H.265 3:H.263 */
    public int[] videoCodecs;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(audioPort);
        dest.writeInt(audioMaxPort);
        dest.writeInt(videoPort);
        dest.writeInt(videoMaxPort);
        if (audioCodecs == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(audioCodecs.length);
            dest.writeIntArray(audioCodecs);
        }

        if (audioPacketTime == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(audioPacketTime.length);
            dest.writeIntArray(audioPacketTime);
        }
        dest.writeInt(videoWidth);
        dest.writeInt(videoHeight);
        dest.writeInt(videoFrameRate);
        dest.writeInt(videoBitRate);

        if (videoCodecs == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(videoCodecs.length);
            dest.writeIntArray(videoCodecs);
        }
        dest.writeInt(dtmfMode);
        dest.writeString(incomingCallVoice);
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<MediaConfig> CREATOR = new Creator<MediaConfig>()
    {
        public MediaConfig createFromParcel(Parcel source)
        {
            MediaConfig config = new MediaConfig();
            config.audioPort = source.readInt();
            config.audioMaxPort = source.readInt();
            config.videoPort = source.readInt();
            config.videoMaxPort = source.readInt();
            int length = source.readInt();
            int[] audioCodecs = null;
            if (length > 0)
            {
                audioCodecs = new int[length];
                source.readIntArray(audioCodecs);
            }
            config.audioCodecs = audioCodecs;

            length = source.readInt();
            int[] audioPacketTime = null;
            if (length > 0)
            {
                audioPacketTime = new int[length];
                source.readIntArray(audioPacketTime);
            }
            config.audioPacketTime = audioPacketTime;

            config.videoWidth = source.readInt();
            config.videoHeight = source.readInt();
            config.videoFrameRate = source.readInt();
            config.videoBitRate = source.readInt();

            length = source.readInt();
            int[] videoCodecs = null;
            if (length > 0)
            {
                videoCodecs = new int[length];
                source.readIntArray(videoCodecs);
            }
            config.videoCodecs = videoCodecs;
            config.dtmfMode = source.readInt();
            config.incomingCallVoice = source.readString();
            return config;
        }

        public MediaConfig[] newArray(int size)
        {
            return new MediaConfig[size];
        }
    };

}
