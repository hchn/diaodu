package com.jiaxun.sdk.dcl.module.blackWhite.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;

/**
 * 说明：黑白名单业务功能接口
 *
 * @author  HeZhen
 *
 * @Date 2015-7-9
 */
public interface DclBlackWhiteListService
{
    /**
     * 方法说明 : 添加黑名单
     * @param blackWhiteEntity
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean addBlack(BlackWhiteModel blackWhiteEntity);

    /**
     * 方法说明 :批量添加黑名单
     * @param blackWhiteList
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean addBlacks(ArrayList<BlackWhiteModel> blackWhiteList);

    /**
     * 方法说明 :删除黑名单
     * @param blackWhiteEntity
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean removeBlack(BlackWhiteModel blackWhiteEntity);

    /**
     * 方法说明 :删除所有黑名单
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean removeAllBlack();

    /**
     * 方法说明 :获得黑名单列表
     * @return ArrayList<BlackWhiteModel>
     * @author HeZhen
     * @Date 2015-8-27
     */
    ArrayList<BlackWhiteModel> getBLackList();

    /**
     * 方法说明 :添加白名单
     * @param blackWhiteEntity
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean addWhite(BlackWhiteModel blackWhiteEntity);

    /**
     * 方法说明 :批量添加白名单
     * @param blackWhiteList
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean addWhites(ArrayList<BlackWhiteModel> blackWhiteList);

    /**
     * 方法说明 :删除白名单
     * @param blackWhiteEntity
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean removeWhite(BlackWhiteModel blackWhiteEntity);

    /**
     * 方法说明 :删除所有白名单
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean removeAllWhite();

    /**
     * 方法说明 :获得白名单列表
     * @return ArrayList<BlackWhiteModel>
     * @author HeZhen
     * @Date 2015-8-27
     */
    ArrayList<BlackWhiteModel> getWhiteList();

    /**
     * 方法说明 :删除所有的黑白名单
     * @return boolean
     * @author HeZhen
     * @Date 2015-8-27
     */
    boolean removeAllBlackWhite();

    /**
     * 方法说明 :通过ID获得黑名单
     * @param id
     * @return BlackWhiteModel
     * @author HeZhen
     * @Date 2015-8-27
     */
    BlackWhiteModel getBlackById(int id);

    /**
     * 方法说明 :通过ID获得白名单
     * @param id
     * @return BlackWhiteModel
     * @author HeZhen
     * @Date 2015-8-27
     */
    BlackWhiteModel getWhiteById(int id);
}
