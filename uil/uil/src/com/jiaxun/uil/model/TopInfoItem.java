package com.jiaxun.uil.model;
/**
 * ˵����������״̬��ϢԪ��
 *
 * @author  chaimb
 *
 * @Date 2015-8-19
 */
public class TopInfoItem
{

    private int id;
    private String name;
    private int iconRes;
    //֪ͨ���ͣ�0��wifi 1������ 2:ϵͳ�ڴ治��
    private int notifyType;
    
    public TopInfoItem()
    {
        super();
    }
    public TopInfoItem(int id, String name, int iconRes)
    {
        super();
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
    }
    public TopInfoItem(int id, String name, int iconRes,int notifyType)
    {
        super();
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
        this.notifyType = notifyType;
    }
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getIconRes()
    {
        return iconRes;
    }
    public void setIconRes(int iconRes)
    {
        this.iconRes = iconRes;
    }
    public int getNotifyType()
    {
        return notifyType;
    }
    public void setNotifyType(int notifyType)
    {
        this.notifyType = notifyType;
    }
    
    
    
    
}
