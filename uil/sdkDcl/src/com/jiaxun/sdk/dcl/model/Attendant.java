package com.jiaxun.sdk.dcl.model;

/**
 * 说明：调度台本地操作用户实体
 * 
 * @author hubin
 * 
 * @Date 2015-2-10
 */
public class Attendant
{
    /** 编号 */
    private int id;
    /** 姓名 */
    private String name;
    /** 登陆名 */
    private String login;
    /** 密码 */
    private String password;
    /** 优先级，0：值班员 1：管理员 */
    private int priority;

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

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    
    @Override
    public String toString()
    {
        return "name: " + name + ",login: " + login + ",password: " + password + ",priority: " + priority;
    }

}
