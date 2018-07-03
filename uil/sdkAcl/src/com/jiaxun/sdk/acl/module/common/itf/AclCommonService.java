package com.jiaxun.sdk.acl.module.common.itf;

import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.acl.module.common.callback.AclCommonEventListener;

/**
 * 说明：提供公共接口方法
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclCommonService
{
    /**
     * 方法说明 : 设置公共事件通知
     * @param callback 公共事件通知回调
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int regCommonEventListener(AclCommonEventListener callback);

    /**
     * 方法说明 : 设置夜服业务
     * @param nightService 进入/退出夜服  
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int setNightService(boolean nightService) throws Exception;

    /**
     * 方法说明 : 设置业务配置
     * @param config 业务配置
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int updateAccountConfig(AccountConfig config) throws Exception;

    /**
     * 方法说明 : 启动ACL业务功能服务
     * @param config 业务配置
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-2-4
     */
    int startAclService(AccountConfig config) throws Exception;

    /**
     * 方法说明 : 停止ACL业务功能服务
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-2-4
     */
    int stopAclService() throws Exception;

}
