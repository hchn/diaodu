package com.jiaxun.sdk.dcl.module.attendant.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.Attendant;

/**
 * 说明：调度台本地操作用户服务接口
 *
 * @author  hubin
 *
 * @Date 2015-3-16
 */
public interface DclAtdService
{
    /**
     * 方法说明 : 是否已经登陆
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdLogin();

    /**
     * 方法说明 : 是否管理员已经登陆
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdAdminLogin();

    /**
     * 方法说明 : 验证登陆名存在
     * @param login
     * @return
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdNameValid(String login);

    /**
     * 方法说明 : 验证锁定密码
     * @param password
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdLoginedPasswordValid(String password);

    /**
     * 方法说明 : 登陆名和密码验证
     * @param login
     * @param password
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdAuthorized(String login, String password);

    /**
     * 方法说明 : 添加用户
     * @param user
     * @return int 
     * @author hubin
     * @Date 2015-9-10
     */
    int addAtd(Attendant user);

    /**
     * 方法说明 : 删除用户
     * @param login
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean removeAtd(String login);

    /**
     * 方法说明 : 删除用户
     * @param user
     * @return int
     * @author hubin
     * @Date 2015-9-10
     */
    int modifyAtdInfo(Attendant user);

    /**
     * 方法说明 : 获取用户列表
     * @return ArrayList<Attendant>
     * @author hubin
     * @Date 2015-9-10
     */
    ArrayList<Attendant> getAttendants();

    /**
     * 方法说明 : 获取当前登录用户
     * @return Attendant
     * @author hubin
     * @Date 2015-9-10
     */
    Attendant getLoginedAttendant();

}
