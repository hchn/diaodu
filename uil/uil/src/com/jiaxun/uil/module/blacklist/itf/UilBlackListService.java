package com.jiaxun.uil.module.blacklist.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;

/**
 * ˵����
 *
 * @author  HeZhen
 *
 * @Date 2015-7-9
 */
public interface UilBlackListService
{
    /**
     * ����˵�� :��Ӻڰ�����
     * @author HeZhen
     * @Date 2015-7-2
     */
    void addNumToBWList(BlackWhiteModel blackWhiteEntity);

    /**
     * ����˵�� :������Ӻڰ�����
     * @param blackWhiteList
     * @author HeZhen
     * @Date 2015-7-3
     */
    void addNumToBWList(ArrayList<BlackWhiteModel> blackWhiteList);

    /**
     * ����˵�� :ɾ�������к���
     * @author HeZhen
     * @Date 2015-7-2
     */
    void removeBlack(int blackId);

    void removeWhite(int whiteId);

    /**
     * ����˵�� :�������
     * @param removeBlackList  true all black ,false all white
     * @author HeZhen
     * @Date 2015-7-2
     */
    void removeAllBlack();

    void removeAllWhite();

    /**
     * ����˵�� :
     * @param black  true all black ,false all white
     * @return
     * @author HeZhen
     * @Date 2015-7-9
     */
    ArrayList<BlackWhiteModel> getBWList(boolean black);

}
