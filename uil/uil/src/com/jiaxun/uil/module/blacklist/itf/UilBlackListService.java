package com.jiaxun.uil.module.blacklist.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-7-9
 */
public interface UilBlackListService
{
    /**
     * 方法说明 :添加黑白名单
     * @author HeZhen
     * @Date 2015-7-2
     */
    void addNumToBWList(BlackWhiteModel blackWhiteEntity);

    /**
     * 方法说明 :批量添加黑白名单
     * @param blackWhiteList
     * @author HeZhen
     * @Date 2015-7-3
     */
    void addNumToBWList(ArrayList<BlackWhiteModel> blackWhiteList);

    /**
     * 方法说明 :删除名单中号码
     * @author HeZhen
     * @Date 2015-7-2
     */
    void removeBlack(int blackId);

    void removeWhite(int whiteId);

    /**
     * 方法说明 :清空名单
     * @param removeBlackList  true all black ,false all white
     * @author HeZhen
     * @Date 2015-7-2
     */
    void removeAllBlack();

    void removeAllWhite();

    /**
     * 方法说明 :
     * @param black  true all black ,false all white
     * @return
     * @author HeZhen
     * @Date 2015-7-9
     */
    ArrayList<BlackWhiteModel> getBWList(boolean black);

}
