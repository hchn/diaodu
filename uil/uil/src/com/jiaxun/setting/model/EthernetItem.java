package com.jiaxun.setting.model;



/**
 * ˵����ͨѶ¼����
 *
 * @author  zhangxd
 *
 * @Date 2015-6-4
 */
public class EthernetItem extends PrefsBaseItem
{
    
    public EthernetItem(String name, int imageId,String key, ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.key=key;
       // this.which=which;
        this.itemCallBack = itemCallBack;
        this.itemType = PrefItemType.CONTACT;
        
    }    
}
