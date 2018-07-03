/**
 * @author shanhz
 *         session common fields
 */
package com.jiaxun.sdk.scl.session;

import android.os.Bundle;
import android.os.Message;

import com.jiaxun.sdk.dcl.handler.DataLooperHandler;
import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.module.DclServiceFactory;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;

public class Session
{
    private int sessionType;
    private String sessionId;
    private int status;

    public void recycle()
    {
        SessionManager.getInstance().recycle(this);
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public int getSessionType()
    {
        return sessionType;
    }

    public void setSessionType(int sessionType)
    {
        this.sessionType = sessionType;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void handleCallRecord(CallRecord callRecord)
    {
        Message msg = DataLooperHandler.getInstance().obtainMessage();
        msg.what = CommonEventEntry.MESSAGE_TYPE_CALL_RECORD;
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, callRecord);
        msg.setData(bundle);
        DataLooperHandler.getInstance().sendMessage(msg);
    }

    public String getContactNameByNumber(String number)
    {
        ContactModel contact = DclServiceFactory.getDclContactService().getContactByNum(number);
        if (contact != null)
        {
            return contact.getName();
        }
        else
        {
            return number;
        }
    }
}
