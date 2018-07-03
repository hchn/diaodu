package com.jiaxun.setting.model;

import com.jiaxun.setting.model.PrefsBaseItem.ItemCallBack;


/**
 * 说明：数字类型
 *
 * @author  zhangxd
 *
 * @Date 2015-6-4
 */
public class PrefsSeekBarItem extends PrefsBaseItem
{
    private int defaultValue;
    private int minValue;// 最小值
    private int maxValue;// 最大值

    /**
     * 数字输入类型
     * @param name key中文名
     * @param imageId 图片id
     * @param key key
     * @param hint 提示输入限制
     * @param minLength 最短长度
     * @param maxLength 最长长度
     * @param minValue 最小值
     * @param maxValue 最大值
     * @param itemClick 执行回调
     * @param defauleValue 默认值
     */
    public PrefsSeekBarItem(String name, int imageId, String key, String hint, int minValue, int maxValue,int defauleValue,ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.key = key;
        this.defaultValue = defauleValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.itemType = PrefItemType.SEEKBAR;
        this.itemCallBack = itemCallBack;
    }

    public int getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public int getMinValue()
    {
        return minValue;
    }

    public int getMaxValue()
    {
        return maxValue;
    }

}
