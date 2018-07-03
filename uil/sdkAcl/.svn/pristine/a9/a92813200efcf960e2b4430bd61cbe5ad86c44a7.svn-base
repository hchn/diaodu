package com.jiaxun.sdk.acl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：账户配置
 *
 * @author  hubin
 *
 * @Date 2015-7-13
 */
public class AccountConfig implements Parcelable
{
    /** 本地IP地址 */
    public String localIp;
    /** 本地端口号 */
    public int[] localPort;
    /** 主备服务器类型：1: slotA 2:slotB 3:SIP */
    public int[] serverType;
    /** 主备服务器IP */
    public String[] serverIp;
    /** 主备服务器域名 */
    public String[] serverDomain;
    /** 主备服务器端口号 */
    public int[] serverPort;
    /** 主备服务器注册账号 */
    public String[] account;
    /** 主备服务器注册密码 */
    public String[] password;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
//        dest.writeInt(dtmfMode);
//        dest.writeInt(dtmfPayloadType);
        dest.writeString(localIp);
        if (serverType == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(serverType.length);
            dest.writeIntArray(serverType);
        }

        if (serverIp == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(serverIp.length);
            dest.writeStringArray(serverIp);
        }

        if (serverDomain == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(serverDomain.length);
            dest.writeStringArray(serverDomain);
        }
        
        if (localPort == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(localPort.length);
            dest.writeIntArray(localPort);
        }

        if (serverPort == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(serverPort.length);
            dest.writeIntArray(serverPort);
        }

        if (account == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(account.length);
            dest.writeStringArray(account);
        }

        if (password == null)
        {
            dest.writeInt(0);
        }
        else
        {
            dest.writeInt(password.length);
            dest.writeStringArray(password);
        }
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<AccountConfig> CREATOR = new Creator<AccountConfig>()
    {
        public AccountConfig createFromParcel(Parcel source)
        {
            AccountConfig config = new AccountConfig();
//            config.dtmfMode = source.readInt();
//            config.dtmfPayloadType = source.readInt();
            config.localIp = source.readString();

            int length = source.readInt();
            int[] line = null;
            if (length > 0)
            {
                line = new int[length];
                source.readIntArray(line);
            }
            config.serverType = line;

            length = source.readInt();
            String[] sipServerIp = null;
            if (length > 0)
            {
                sipServerIp = new String[length];
                source.readStringArray(sipServerIp);
            }
            config.serverIp = sipServerIp;

            length = source.readInt();
            String[] sipServerName = null;
            if (length > 0)
            {
                sipServerName = new String[length];
                source.readStringArray(sipServerName);
            }
            config.serverDomain = sipServerName;

            length = source.readInt();
            int[] localPort = null;
            if (length > 0)
            {
                localPort = new int[length];
                source.readIntArray(localPort);
            }
            config.localPort = localPort;

            length = source.readInt();
            int[] serverSipPort = null;
            if (length > 0)
            {
                serverSipPort = new int[length];
                source.readIntArray(serverSipPort);
            }
            config.serverPort = serverSipPort;

            length = source.readInt();
            String[] sipAccount = null;
            if (length > 0)
            {
                sipAccount = new String[length];
                source.readStringArray(sipAccount);
            }
            config.account = sipAccount;

            length = source.readInt();
            String[] sipPassword = null;
            if (length > 0)
            {
                sipPassword = new String[length];
                source.readStringArray(sipPassword);
            }
            config.password = sipPassword;
            return config;
        }

        public AccountConfig[] newArray(int size)
        {
            return new AccountConfig[size];
        }
    };
}
