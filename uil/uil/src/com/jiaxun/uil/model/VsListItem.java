package com.jiaxun.uil.model;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.VsModel;

 
/**
 * 说明：视频监控列表单元
 *
 * @author  zhangxd
 *
 * @Date 2015-5-29
 */
public class VsListItem extends BaseListItem
{
    private static final long serialVersionUID = 1L;
    
    private VsModel vsModel;
    private int status;
    private int type;
    private SurfaceView remoteVideoView;
    private boolean opended;//监控打开关闭标志
    /**
     * 正在打开监控
     */
    private boolean opening=false; 
    private String userName;//用户姓名
    private String videoUrl;//video路径
    private int userIcon;//用户图标
       
    public boolean isOpening()
    {
        return opening;
    }

    public void setOpening(boolean  opening)
    {
        this.opening = opening;
    }
    public VsModel getVsModel()
    {
        return vsModel;
    }

    public void setVsModel(VsModel vsModel)
    {
        this.vsModel = vsModel;
    }
    public String getVideoUrl()
    {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public int getUserIcon()
    {
        return userIcon;
    }

    public void setUserIcon(int userIcon)
    {
        this.userIcon = userIcon;
    }
    
    
    public Boolean isOpened()
    {
        return opended;
    }

    public void setOpened(Boolean opended)
    {
        this.opended = opended;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public SurfaceView getRemoteVideoView()
    {
        return remoteVideoView;
    }

    public void setRemoteVideoView(SurfaceView remoteVideoView)
    {
        this.remoteVideoView = remoteVideoView;
    }
  
 
}
