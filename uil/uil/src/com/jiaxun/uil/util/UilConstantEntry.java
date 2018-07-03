package com.jiaxun.uil.util;

/**
 * 说明：常量
 *
 * @author  zhangxd
 *
 * @Date 2015-9-28
 */
public class UilConstantEntry
{
    /**ip、域名正则表达式 */
    public static final String IP_ADDRESS_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-4]|[1-9]\\d|[1-9])"
            + "|^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
    /**用户名密码正则表达式 */
    public static final String ACCOUNT_OR_PASSWORD = "^[a-zA-Z0-9_@]{1,20}$";
    
    /**号码限制正则表达式 */
    public static final String NUMBER_LIMIT_REGEX = "^[0-9]{1,20}$";
    
    /**号码限制正则表达式，有#和*号 */
    public static final String NUMBER_SIGN_LIMIT_REGEX = "^[0-9*#]{1,20}$";
    
    /**号码限制正则表达式,无长度限制 */
    public static final String NUMBER_SIGN_LIMIT_NOLENGTH_REGEX = "^[0-9*#]*$";
}
