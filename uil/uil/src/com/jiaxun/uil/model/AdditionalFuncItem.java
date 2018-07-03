package com.jiaxun.uil.model;
/**
 * 说明：补充业务功能元素
 *
 * @author  HeZhen
 *
 * @Date 2015-6-8
 */
public class AdditionalFuncItem
{
    private int id;
    private String name;
    private int iconResNormal;
    private int iconResSelect;
    private boolean isChecked;
    public AdditionalFuncItem(String name,int iconNormal,int iconSelect,boolean defaultCheck , int id)
    {
        setName(name);
        setIconResNormal(iconNormal);
        setIconResSelect(iconSelect);
        setChecked(defaultCheck);
        setId(id);
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
    public int getIconResNormal()
    {
        return iconResNormal;
    }
    public void setIconResNormal(int iconResNormal)
    {
        this.iconResNormal = iconResNormal;
    }
    public int getIconResSelect()
    {
        return iconResSelect;
    }
    public void setIconResSelect(int iconResSelect)
    {
        this.iconResSelect = iconResSelect;
    }
    public boolean isChecked()
    {
        return isChecked;
    }
    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }
}
