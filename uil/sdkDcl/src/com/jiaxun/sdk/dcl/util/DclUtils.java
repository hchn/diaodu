package com.jiaxun.sdk.dcl.util;

import java.util.ArrayList;

import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.module.DclServiceFactory;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-8-31
 */
public class DclUtils
{
    /**
     * 方法说明 :
     * @param num
     * @return true 验证通过可接来电 ， false 未通过不允许来电
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
            for (BlackWhiteModel blackEntity : whiteList) // 先检查白名单中是否存在
            {
                if (containNum(blackEntity, num))
                {
                    canAcceptCall = true;
                    break;
                }
            }
            if (canAcceptCall)// 如果白名单中存在 检查黑中是否有
            {
                for (BlackWhiteModel blackEntity : blackList)
                {
                    if (containNum(blackEntity, num))
                    {
                        canAcceptCall = false; // 说明黑名单中也存在
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
                    canAcceptCall = false; // 说明黑名单中也存在
                    break;
                }
            }
        }

        return canAcceptCall;
    }

    /**
     * 方法说明 :
     * @param blackEntity
     * @param num
     * @return true 包含，false 不包含
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
