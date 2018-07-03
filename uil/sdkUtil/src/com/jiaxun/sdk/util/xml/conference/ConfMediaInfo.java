package com.jiaxun.sdk.util.xml.conference;


/**
 * 说明：媒体信息
 *
 * @author  fuluo
 *
 * @Date 2015-5-4
 */
public class ConfMediaInfo
{
    public final static String TAG = "mediaInfo";
    
    public final static String TAG_ENDPOINT = "endpoint";
    
    public final static String ATTRIBUTE_TAG = "tag";
    
    private String tag;
    private ConfMedia audio = new ConfMedia();
    private ConfMedia video = new ConfMedia();

    public ConfMedia getAudio()
    {
        return audio;
    }
    
    public ConfMedia getVideo()
    {
        return video;
    }

    public void addMedia(ConfMedia confItem)
    {
        if(confItem == null)
            return;
        if(confItem.getId().equals(ConfMedia.ID_VIDEO))
            video = confItem;
        else if(confItem.getId().equals(ConfMedia.ID_AUDIO))
            audio = confItem;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

}
