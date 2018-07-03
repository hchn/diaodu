package com.jiaxun.uil.module.surveillance.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;
import com.jiaxun.uil.model.VsListItem;

/**
 * 说明：视频监控业务功能接口
 *
 * @author  zhangxd
 *
 * @Date 2015-6-1
 */
public interface UilVsService
{
    /**
     * 方法说明 : 注册视频监控事件回调
     * @param callback 监控事件回调
     * @return void
     * @author hubin
     * @Date 2015-9-16
     */
    void regVsEventListener(SclVsEventListener callback);
    
    /**
     * 方法说明 :视频监控轮巡开始
     * @param period 轮巡时间间隔 
     * @author zhangxd
     * @Date 2015-6-10
     */
    void vsLoopStart(int period);

    /**
     * 视频监控轮巡结束
     */
    void vsLoopStop();

    /**
     * 方法说明 : 打开视频监控
     * @param videoNum 要打开监控用户号码
     * @return void
     * @author hubin
     * @Date 2015-9-16
     */
    void openVs(String videoNum);
    
    /**
     * 方法说明 : 关闭视频监控
     * @param sessionId 要关闭监控的会话ID
     * @return void
     * @author hubin
     * @Date 2015-9-16
     */
    void closeVs(String sessionId);
    
    /**
     * 方法说明 :添加监控用户
     * @param userNumber 要打开监控用户号码
     * @author zhangxd
     * @Date 2015-6-29
     */
    void addVsUser(String userNumber);

    /**
     * 方法说明 :添加多个监控用户
     * @param openUserNumberList 要打开用户号码列表
     * @author zhangxd
     * @Date 2015-7-2
     */
    void addVsUsers(ArrayList<String> openUserNumberList);

    /**
     * 方法说明 :从监控列表中删除指定号码监控用户
     * @param deleteUserNumber
     * @author zhangxd
     * @Date 2015-7-7
     */
    void deleteVsUser(String deleteUserNumber);

    /**
     * 方法说明 :
     * @param deleteArrayList 要删除监控用户号码列表
     * @author zhangxd
     * @Date 2015-7-2
     */
    void deleteVsUsers(ArrayList<String> deleteArrayList);

    /**
     * 方法说明 :删除全部监控用户
     * @author zhangxd
     * @Date 2015-6-29
     */
    void deleteAllVsUsers();

    /**
     * 方法说明 :获取监控历史列表
     * @return 当前监控列表
     * @author zhangxd
     * @Date 2015-7-10
     */
    ArrayList<VsListItem> getVsUserList();

    /**
    * 方法说明 :返回当前是否在轮巡
    * @return
    * @author zhangxd
    * @Date 2015-9-10
    */
    boolean isLoopStarted();
    /**
     * 方法说明 :返回当前打开个数
     * @return
     * @author zhangxd
     * @Date 2015-9-10
     */
     int getOpenVsCount();

}
