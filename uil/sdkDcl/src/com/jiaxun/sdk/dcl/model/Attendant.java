package com.jiaxun.sdk.dcl.model;

/**
 * ˵��������̨���ز����û�ʵ��
 * 
 * @author hubin
 * 
 * @Date 2015-2-10
 */
public class Attendant
{
    /** ��� */
    private int id;
    /** ���� */
    private String name;
    /** ��½�� */
    private String login;
    /** ���� */
    private String password;
    /** ���ȼ���0��ֵ��Ա 1������Ա */
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
