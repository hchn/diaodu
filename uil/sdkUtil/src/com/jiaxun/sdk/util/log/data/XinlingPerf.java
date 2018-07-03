package com.jiaxun.sdk.util.log.data;

/**
 * 信令性能数据
 * 
 * @author fuluo
 */
public class XinlingPerf
{
    private long request;//信令发送时间戳
    
    private long reponse;//信令返回时间戳

    public long getRequest()
    {
        return request;
    }

    public void setRequest(long request)
    {
        this.request = request;
    }

    public long getReponse()
    {
        return reponse;
    }

    public void setReponse(long reponse)
    {
        this.reponse = reponse;
    }

}
