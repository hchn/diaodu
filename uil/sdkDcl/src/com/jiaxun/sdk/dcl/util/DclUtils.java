package com.jiaxun.sdk.dcl.util;

import java.util.ArrayList;

import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.module.DclServiceFactory;

/**
 * ˵����
 *
 * @author  HeZhen
 *
 * @Date 2015-8-31
 */
public class DclUtils
{
    /**
     * ����˵�� :
     * @param num
     * @return true ��֤ͨ���ɽ����� �� false δͨ������������
     * @author HeZhen
     * @Date 2015-7-9
     */
    public static boolean verifyBlackList(String num)
    {
        boolean canAcceptCall = false;
        if (TextUtils.isEmpty(num))
        {
            return false;
        }
        ArrayList<BlackWhiteModel> whiteList = DclServiceFactory.getDclBlackListService().getWhiteList();
        ArrayList<BlackWhiteModel> blackList = DclServiceFactory.getDclBlackListService().getBLackList();
        if (whiteList.size() > 0)
        {
            for (BlackWhiteModel blackEntity : whiteList) // �ȼ����������Ƿ����
            {
                if (containNum(blackEntity, num))
                {
                    canAcceptCall = true;
                    break;
                }
            }
            if (canAcceptCall)// ����������д��� �������Ƿ���
            {
                for (BlackWhiteModel blackEntity : blackList)
                {
                    if (containNum(blackEntity, num))
                    {
                        canAcceptCall = false; // ˵����������Ҳ����
                        break;
                    }
                }
            }
        }
        else
        {
            canAcceptCall = true;
            for (BlackWhiteModel blackEntity : blackList)
            {
                if (containNum(blackEntity, num))
                {
                    canAcceptCall = false; // ˵����������Ҳ����
                    break;
                }
            }
        }

        return canAcceptCall;
    }

    /**
     * ����˵�� :
     * @param blackEntity
     * @param num
     * @return true ������false ������
     * @author HeZhen
     * @Date 2015-7-9
     */
    private static boolean containNum(BlackWhiteModel blackEntity, String num)
    {
        if (blackEntity == null)
        {
            return false;
        }
        int contactId = Integer.valueOf(blackEntity.getContactId());
        ContactModel contact = DclServiceFactory.getDclContactService().getContactById(contactId);
        if (contact != null)
        {
            ArrayList<ContactNum> contactNums = contact.getPhoneNumList();
            for (ContactNum contactNum : contactNums)
            {
                if (num.equals(contactNum.getNumber()))
                {
                    return true;
                }
            }
        }
        else if (num.startsWith(blackEntity.getPhoneNum()))
        {
            return true;
        }
        return false;
    }
}
