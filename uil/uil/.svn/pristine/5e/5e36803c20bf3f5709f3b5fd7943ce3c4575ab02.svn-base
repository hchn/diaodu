package com.jiaxun.setting.model;

/**
 * 文本类型，包括：字符文本，密码文本
 */
public class PrefsTextItem extends PrefsBaseItem
{
    private String regex;//输入字符串正则限制
    /**
     * @param name 名字
     * @param imageId 图片id
     * @param itemType 类型
     * @param key 键值
     * @param regular 正则限制
     * @param hint 默认提示
     * @param itemCallBack 回调
     */
    public PrefsTextItem(String name, int imageId, int itemType, String key, String regex, String hint,  ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.key = key;
        this.itemCallBack = itemCallBack;
        this.itemType = itemType;
        this.regex = regex;
        this.hint=hint;
    }
    public String getRegex()
    {
        return regex;
    }
    public void setRegex(String regex)
    {
        this.regex = regex;
    }
     
}
