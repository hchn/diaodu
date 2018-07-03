package com.jiaxun.sdk.scl.module.common.itf;

import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.model.ServiceConfig;
import com.jiaxun.sdk.scl.module.common.callbcak.SclCommonEventListener;

/**
 * 说明：提供公共接口方法
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface SclCommonService
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
    int sclRegCommonEventListener(SclCommonEventListener callback);

    /**
     * 方法说明 : 设置夜服业务
     * @param nightService 进入/退出夜服  
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int setNightService(boolean nightService);

    /**
     * 方法说明 : 设置账户配置
     * @param accountConfig 账户配置信息
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int updateAcountConfig(AccountConfig accountConfig);

    /**
     * 方法说明 : 设置媒体设备配置
     * @param mediaConfig 媒体配置信息
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @return int
     * @author hubin
     * @Date 2015-1-30
     */
    int updateMediaConfig(MediaConfig mediaConfig);
    
    /**
     * 方法说明 : 设置业务配置
     * @param serviceConfig 业务配置信息
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @return int
     * @author hubin
     * @Date 2015-1-30
     */
    int updateServiceConfig(ServiceConfig serviceConfig);
    
    /**
     * 方法说明 : 启动服务
     * @param config 配置参数
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int startSclService(AccountConfig config);

    /**
     * 方法说明 : 停止服务
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int stopSclService();

//
//    /**
//     * 方法说明 : 对话音量设置, 0-100,  -1=auto
//     * @param mic
//     * @param handfree
//     * @param handset
//     * @param headset
//     * @param bluetooth
//     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
//     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
//     *         CommonConstantEntry.PARAM_ERROR：参数错误。
//     * @author hubin
//     * @Date 2015-1-30
//     */
//    int setTalkVolume(int mic, int handfree, int handset, int headset, int bluetooth);
//
//    /**
//     * 方法说明 : 音乐音量设置, 0-100,  -1=auto
//     * @param handfree
//     * @param handset
//     * @param headset
//     * @param bluetooth
//     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
//     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
//     *         CommonConstantEntry.PARAM_ERROR：参数错误。
//     * @author hubin
//     * @Date 2015-1-30
//     */
//    int setMusicVolume(int handfree, int handset, int headset, int bluetooth);
//
//    /**
//     * 方法说明 : 铃音音量设置,设置大小铃，0-100,
//     * @param loudRing
//     * @param softRing
//     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
//     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
//     *         CommonConstantEntry.PARAM_ERROR：参数错误。
//     * @author hubin
//     * @Date 2015-1-30
//     */
//    int setRingVolume(int loudRing, int softRing);

    /**
     * 方法说明 : 环回测试
     * @param audio 音频环回
     * @param video 视频环回
     * @param loopType 1: 本地环回，2：远端环回
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int loopTest(boolean audio, boolean video, int loopType);

}
