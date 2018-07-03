package com.jiaxun.sdk.util.constant;

/**
 * 说明：
 *
 * @author  hubin
 *
 * @Date 2015-1-7
 */
public class CommonConstantEntry
{
    public static class TransportConstants
    {
        public final static byte UND_SOCK = 0;
        public final static byte TCP_SOCK = 1;
        public final static byte UDP_SOCK = 2;
    }

    /**线路类型**********************************************************************/
    /** 线路类型： slotA  */
    public static final int LINE_TYPE_SLOTA = 1;
    /** 线路类型： slotB  */
    public static final int LINE_TYPE_SLOTB = 2;
    /** 线路类型： sip  */
    public static final int LINE_TYPE_SIP = 3;
    /**线路类型**********************************************************************/

    /**服务状态**********************************************************************/
    /** 服务状态，离线    */
    public final static int SERVICE_STATUS_OFFLINE = 0;
    /** 服务状态，注册成功主用    */
    public final static int SERVICE_STATUS_ACTIVE = 1;
    /** 服务状态，注册成功备用    */
    public final static int SERVICE_STATUS_STANDBY = 2;
    /** 服务状态，离线：非法用户   */
    public final static int SERVICE_STATUS_FORBIDUSER = 3;
    /** 服务状态，离线：密码错误   */
    public final static int SERVICE_STATUS_PASSWORDERROR = 4;
    /** 服务状态，离线：用户闭锁   */
    public final static int SERVICE_STATUS_LOCKEDUSER = 5;
    /** 服务状态，离线：禁止上线   */
    public final static int SERVICE_STATUS_FORBIDUPLINE = 6;
    /** 服务状态，离线：心跳中断   */
    public final static int SERVICE_STATUS_HEARTBEAT_HALT = 7;
    /** 服务状态，离线：网络不可用   */
    public final static int SERVICE_STATUS_NETWORK_DISABLED = 8;
    /** 服务状态，离线：超时   */
    public final static int SERVICE_STATUS_TIMEOUT = 9;
    /**服务状态**********************************************************************/

    /**业务类型**********************************************************************/
    /** 业务类型，1：单呼业务    */
    public final static int SESSION_TYPE_SCALL = 1;
    /** 业务类型，2：会议业务    */
    public final static int SESSION_TYPE_CONF = 2;
    /** 业务类型，3：监控业务    */
    public final static int SESSION_TYPE_VS = 3;
    /**业务类型**********************************************************************/

    /**链路状态**********************************************************************/
    /** 链路状态：可用    */
    public static final int LINK_STATUS_OK = 1;
    /** 链路状态：    */
    public static final int LINK_STATUS_E1_LOS = 2;
    /** 链路状态：    */
    public static final int LINK_STATUS_E1_AIS = 3;
    /** 链路状态：    */
    public static final int LINK_STATUS_E1_RAI = 4;
    /** 链路状态：    */
    public static final int LINK_STATUS_U_ACTIVATING = 5;
    /** 链路状态：    */
    public static final int LINK_STATUS_U_NON_ACTIVE = 6;
    /** 链路状态：    不可用*/
    public static final int LINK_STATUS_HARDWARE_FAILURE = 7;
    /**链路状态**********************************************************************/

    /**上线模式**********************************************************************/
    /** 上线模式，1：机车台主用上线（即正常情况）    */
    public final static int MODE_NORMAL_MASTER_UPLINE = 1;
    /** 上线模式，2：机车台主用下线|机车台备用上线（即主备切换IP发生变化后）    */
    public final static int MODE_SWITCH_SLAVE_UPLINE = 2;
    /** 上线模式，2：机车台备用下线|机车台主用上线（即主备切换IP发生变化后）    */
    public final static int MODE_SWITCH_MASTER_UPLINE = 3;
    /**上线模式**********************************************************************/

    /**下线模式**********************************************************************/
    /** 下线模式，1：机车台主用下线（即正常情况）    */
    public final static int MODE_NORMAL_MASTER_OFFLINE = 1;
    /** 下线模式，2：机车台主用下线|机车台备用上线（即主备切换IP发生变化后）    */
    public final static int MODE_SWITCH_SLAVE_OFFLINE = 2;
    /** 下线模式，2：机车台备用下线|机车台主用上线（即主备切换IP发生变化后）    */
    public final static int MODE_SWITCH_MASTER_OFFLINE = 3;
    /**下线模式**********************************************************************/

    /**方法执行结果**********************************************************************/
    /** 方法执行成功    */
    public final static int METHOD_SUCCESS = 0;
    /** 方法执行失败    */
    public final static int METHOD_FAILED = -1;
    /** 参数错误    */
    public final static int PARAM_ERROR = -2;
    /** 规格超限    */
    public final static int OUT_ATTEND_COUNT = -3;
    /**方法执行结果**********************************************************************/

    /**呼叫类型**********************************************************************/
    /** 呼叫类型：语音单呼    */
    public final static int CALL_TYPE_SINGLE_AUDIO = 0;
    /** 呼叫类型：视频单呼    */
    public final static int CALL_TYPE_SINGLE_VIDEO = 1;
    /** 呼叫类型：组呼    */
    public final static int CALL_TYPE_GROUP = 2;
    /** 呼叫类型：广播    */
    public final static int CALL_TYPE_BROADCAST = 3;
    /** 呼叫类型：临时组呼    */
    public final static int CALL_TYPE_TEMPGROUP = 4;
    /** 呼叫类型：呼叫中组    */
    public final static int CALL_TYPE_ACTIVE = 5;
    /** 呼叫类型：重联组呼    */
    public final static int CALL_TYPE_DOUBLEHEAD = 6;
    /** 呼叫类型：语音会议    */
    public final static int CALL_TYPE_CONFERENCE_AUDIO = 7;
    /** 呼叫类型：视频会议    */
    public final static int CALL_TYPE_CONFERENCE_VIDEO = 8;
    /** 呼叫类型：视频监控   */
    public final static int CALL_TYPE_VIDEO_SURVEILLANCE = 9;
    /**呼叫类型**********************************************************************/

    /**呼叫角色**********************************************************************/
    /** 发起者    */
    public final static int CALL_ROLE_CREATE = 1;
    /** 参数者   */
    public final static int CALL_ROLE_MEMBER = 2;
    /**呼叫角色**********************************************************************/

    /**视频类型**********************************************************************/
    /** 视频类型：双向视频    */
    public final static int VIDEO_TYPE_PUSHPULL = 1;
    /** 视频类型：推送视频    */
    public final static int VIDEO_TYPE_PUSH = 2;
    /** 视频类型：打开视频    */
    public final static int VIDEO_TYPE_PULL = 3;
    /**视频类型**********************************************************************/

    /**呼叫状态**********************************************************************/
    /** 呼叫状态：空闲     */
    public static final int CALL_STATE_IDLE = 0;
    /** 呼叫状态：去电     */
    public static final int CALL_STATE_OUTGOING = 1;
    /** 呼叫状态： 回铃    */
    public static final int CALL_STATE_RING_BACK = 2;
    /** 呼叫状态： 通话*/
    public static final int CALL_STATE_IN_CALL = 3;
    /** 呼叫状态： 响铃    */
    public static final int CALL_STATE_RING = 4;
    /** 呼叫状态： 来电    */
    public static final int CALL_STATE_INCOMING = 5;
    /** 呼叫状态： 保持    */
    public static final int CALL_STATE_HOLD = 6;
    /** 呼叫状态： 被保持   */
    public static final int CALL_STATE_HOLDED = 7;
    /**呼叫状态**********************************************************************/

    /**单呼状态**********************************************************************/
    /** 呼叫状态：空闲     */
    public static final int SCALL_STATE_IDLE = 0;
    /** 呼叫状态：呼出，等待证实     */
    public static final int SCALL_STATE_DIAL = 1;
    /** 呼叫状态： 收到证实，等待回铃    */
    public static final int SCALL_STATE_PROCEEDING = 2;
    /** 呼叫状态： 回铃，等待应答*/
    public static final int SCALL_STATE_RINGBACK = 3;
    /** 呼叫状态： 等待对方证实   */
    public static final int SCALL_STATE_CONNECT_ACK = 4;
    /** 呼叫状态： 通话状态    */
    public static final int SCALL_STATE_CONNECT = 5;
    /** 呼叫状态： 呼入振铃    */
    public static final int SCALL_STATE_RINGING = 6;
    /** 呼叫状态： 保持对方   */
    public static final int SCALL_STATE_HOLD = 7;
    /** 呼叫状态： 等待被保持方证实   */
    public static final int SCALL_STATE_HOLD_ACK = 8;
    /** 呼叫状态： 等待被解保持方证实   */
    public static final int SCALL_STATE_RETRIEVE_ACK = 9;
    /** 呼叫状态： 被对方保持   */
    public static final int SCALL_STATE_REMOTE_HOLD = 10;
    /** 呼叫状态： 双向保持   */
    public static final int SCALL_STATE_BOTH_HOLD = 11;
    /**呼叫状态**********************************************************************/

    /**单呼状态计时器**********************************************************************/
    /** 呼叫状态：呼出，等待证实     */
    public static final int SCALL_TIMEOUT_DIAL = 15000;
    /** 呼叫状态： 收到证实，等待回铃    */
    public static final int SCALL_TIMEOUT_PROCEEDING = 15000;
    /** 呼叫状态： 回铃，等待应答*/
    public static final int SCALL_TIMEOUT_RINGBACK = 62000;
    /** 呼叫状态： 等待对方证实   */
    public static final int SCALL_TIMEOUT_CONNECT_ACK = 15000;
    /** 呼叫状态： 通话状态    */
    public static final int SCALL_TIMEOUT_CONNECT = -1;
    /** 呼叫状态： 呼入振铃    */
    public static final int SCALL_TIMEOUT_RINGING = 92000;
    /** 呼叫状态： 保持对方   */
    public static final int SCALL_TIMEOUT_HOLD = -1;
    /** 呼叫状态： 等待被保持方证实   */
    public static final int SCALL_TIMEOUT_HOLD_ACK = 15000;
    /** 呼叫状态： 等待被解保持方证实   */
    public static final int SCALL_TIMEOUT_RETRIEVE_ACK = 15000;
    /**呼叫状态计时器**********************************************************************/

    /**会议状态**********************************************************************/
    /** 呼叫状态：空闲     */
    public static final int CONF_STATE_IDLE = 20;
    /** 呼叫状态： 发起会议请求，等待确认证实    */
    public static final int CONF_STATE_PROCEEDING = 21;
    /** 呼叫状态： 通话状态    */
    public static final int CONF_STATE_CONNECT = 22;
    /** 呼叫状态： 退会状态    */
    public static final int CONF_STATE_EXIT = 23;
    /** 呼叫状态： 发起退会请求，等待确认证实    */
    public static final int CONF_STATE_EXIT_ACK = 24;
    /** 呼叫状态： 发起入会请求，等待确认证实    */
    public static final int CONF_STATE_RETURN_ACK = 25;
    /**会议状态**********************************************************************/

    /**会议成员状态**********************************************************************/
    /** 呼叫状态：空闲     */
    public static final int CONF_MEMBER_STATE_IDLE = 20;
    /** 呼叫状态： 回铃状态    */
    public static final int CONF_MEMBER_STATE_RING = 21;
    /** 呼叫状态： 通话状态    */
    public static final int CONF_MEMBER_STATE_CONNECT = 22;
    /**会议成员状态**********************************************************************/

    /**会议状态计时器**********************************************************************/
    /** 呼叫状态： 发起会议请求，等待确认证实    */
    public static final int CONF_TIMEOUT_PROCEEDING = 15000;
    /** 呼叫状态： 发起退会请求，等待确认证实*/
    public static final int CONF_TIMEOUT_EXIT_ACK = 15000;
    /** 呼叫状态： 发起入会请求，等待确认证实   */
    public static final int CONF_TIMEOUT_RETURN_ACK = 15000;
    /**会议状态计时器**********************************************************************/

    /**视频监控状态**********************************************************************/
    /** 视频监控状态：关闭状态     */
    public static final int VS_STATE_CLOSE = 30;
//    /** 视频监控状态： 发出关闭监控请求，等待确认证实    */
//    public static final int VS_STATE_CLOSE_ACK = 31;
    /** 视频监控状态： 开启状态    */
    public static final int VS_STATE_OPEN = 32;
    /** 视频监控状态： 发出开启监控请求，等待确认证实    */
    public static final int VS_STATE_OPEN_ACK = 33;
    /**视频监控状态**********************************************************************/

    /**视频监控状态计时器**********************************************************************/
    /** 呼叫状态： 发起监控请求，等待确认证实    */
    public static final int VS_TIMEOUT_OPEN_ACK = 15000;
//    /** 呼叫状态： 发起关闭请求，等待确认证实*/
//    public static final int VS_TIMEOUT_CLOSE_ACK = 15000;
    /**视频监控状态计时器**********************************************************************/

    /**用户状态**********************************************************************/
    /** 用户状态：关闭状态     */
    public static final int USER_STATUS_ONLINE = 40;
    /** 用户状态： 发出关闭监控请求，等待确认证实    */
    public static final int USER_STATUS_OFFLINE = 41;
    /**用户状态**********************************************************************/

    /**DTMF模式**********************************************************************/
    /** DTMF模式： inband  */
    public static final int DTMF_MODE_INBAND = 1;
    /** DTMF模式： sip info  */
    public static final int DTMF_MODE_SIP_INFO = 2;
    /** DTMF模式： rfc2833  */
    public static final int DTMF_MODE_RFC2833 = 3;
    /**DTMF模式**********************************************************************/

    /**保持|恢复操作**********************************************************************/
    /** 保持失败原因：重复保持    */
    public static final String HOLD_REPEAT = "hold_repeated";
    /** 恢复失败原因：重复恢复    */
    public static final String RECOVERY_REPEAT = "recovery_repeated";
    /**保持|恢复操作**********************************************************************/

    /**返回结果**********************************************************************/

    /** 返回结果：成功    */
    public static final int RESPONSE_SUCCESS = 0;
    /** 返回结果：失败    */
    public static final int RESPONSE_FAILED = -1;

    /**返回结果**********************************************************************/

    /**数据类型**********************************************************************/
    public static final String DATA_PRIORITY = "DATA_PRIORITY";
    public static final String DATA_NUMBER = "DATA_NUMBER";
    public static final String DATA_SESSION_ID = "DATA_SESSION_ID";
    public static final String DATA_TYPE = "DATA_TYPE";
    public static final String DATA_STATUS = "DATA_STATUS";
    public static final String DATA_LINK_STATUS = "DATA_LINK_STATUS";
    public static final String DATA_REASON = "DATA_REASON";
    public static final String DATA_OBJECT = "DATA_OBJECT";
    public static final String DATA_RESULT = "DATA_RESULT";
    public static final String DATA_DURATION = "DATA_DURATION";
    public static final String DATA_CHANNEL = "DATA_CHANNEL";
    public static final String DATA_AUDIO = "DATA_AUDIO";
    public static final String DATA_VIDEO = "DATA_VIDEO";
    public static final String DATA_REMOTE_ADDRESS = "DATA_REMOTE_ADDRESS";
    public static final String DATA_AUDIO_LOCAL_PORT = "DATA_AUDIO_LOCAL_PORT";
    public static final String DATA_AUDIO_REMOTE_PORT = "DATA_AUDIO_REMOTE_PORT";
    public static final String DATA_VIDEO_LOCAL_PORT = "DATA_VIDEO_LOCAL_PORT";
    public static final String DATA_VIDEO_REMOTE_PORT = "DATA_VIDEO_REMOTE_PORT";
    public static final String DATA_CODEC = "DATA_CODEC";
    public static final String DATA_ENABLE = "DATA_ENABLE";
    public static final String DATA_SIZE = "DATA_SIZE";
    public static final String DATA_MEMBER_LIST = "DATA_MEMBER_LIST";
    public static final String DATA_MEDIA_TAG = "DATA_MEDIA_TAG";
    public static final String DATA_CONTACT_ID = "DATA_CONTACT_ID";
    public static final String DATA_TASK_ID = "DATA_TASK_ID";
    public static final String DATA_IS_CONF_MEMBER = "DATA_IS_CONF_MEMBER";
    public static final String DATA_COMMAND = "DATA_COMMAND";
    public static final String DATA_COMMAND_PARA1 = "DATA_COMMAND_PARA1";
    public static final String DATA_COMMAND_PARA2 = "DATA_COMMAND_PARA2";
    public static final String DATA_COMMAND_PARA3 = "DATA_COMMAND_PARA3";
    /**数据类型**********************************************************************/

    /**功能号注册失败**********************************************************************/
    /** 功能号注册失败：  失败 */
    public static final String REGISTER_FAILED = "fnRegFailed_failed";
    /** 功能号注册失败：功能码冲突   */
    public static final String REGISTER_FAILED_CONFLICT = "fnRegFailed_conflict";
    /** 功能号注册失败：该用户未授权   */
    public static final String REGISTER_FAILED_UNAUTHORIZED = "fnRegFailed_unAuthorized";
    /** 功能号注册失败：注册的功能号码数量超出限制    */
    public static final String REGISTER_FAILED_TOOMANYREGISTER = "fnRegFailed_tooManyRegister";
    /** 功能号注册失败：服务能力不足    */
    public static final String REGISTER_FAILED_INADEQUATESEVICECAPACITY = "fnRegFailed_inadequateSeviceCapacity";
    /** 功能号注册失败：强制注销锁定中   */
    public static final String REGISTER_FAILED_LOCKED = "fnRegFailed_locked";
    /** 功能号注册失败：超时   */
    public static final String REGISTER_FAILED_TIMEOUT = "fnRegFailed_timeout";
    /**功能号注册失败**********************************************************************/

    /**功能号注销失败**********************************************************************/
    /** 功能号注销失败：  失败 */
    public static final String DEREGISTER_FAILED = "fnDeregFailed_failed";
    /** 功能号注销失败：已注销   */
    public static final String DEREGISTER_FAILED_CANCELLED = "fnDeregFailed_cancelled";
    /** 功能号注销失败：该用户未授权    */
    public static final String DEREGISTER_FAILED_UNAUTHORIZED = "fnDeregFailed_unAuthorized";
    /** 功能号注销失败：超时   */
    public static final String DEREGISTER_FAILED_TIMEOUT = "fnDeregFailed_timeout";
    /** 功能号注销失败：不是功能码所有者   */
    public static final String DEREGISTER_FAILED_NOT_OWNER = "fnDeregFailed_not_owner";
    /**功能号注销失败**********************************************************************/

    /**功能号强制注销失败**********************************************************************/
    /** 功能号强制注销失败：  后台操作失败 */
    public static final String FORCE_DEREGISTER_FAILED = "force_fnDeregFailed_failed";
    /** 功能号强制注销失败：被操作功能码未注册   */
    public static final String FORCE_DEREGISTER_FAILED_FN_UNREGISTER = "force_fnDeregFailed_fn_unregister";
    /** 功能号强制注销失败：该用户未授权    */
    public static final String FORCE_DEREGISTER_FAILED_UNAUTHORIZED = "force_fnDeregFailed_unAuthorized";
    /** 功能号强制注销失败：超时   */
    public static final String FORCE_DEREGISTER_FAILED_TIMEOUT = "force_fnDeregFailed_timeout";
    /** 功能号强制注销失败：被操作用户未注册   */
    public static final String FORCE_DEREGISTER_FAILED_USER_UNREGISTER = "force_fnDeregFailed_user_unregister";
    /** 功能号强制注销失败：被操作用户已闭塞   */
    public static final String FORCE_DEREGISTER_FAILED_USER_FAIL = "force_fnDeregFailed_user_fail";
    /**功能号强制注销失败**********************************************************************/

    /**功能码单个查询失败**********************************************************************/
    /** 功能码单个查询失败：  用户无权查询 */
    public static final String FN_QUERY_FAILED_UNAUTHORIZED = "FN_QUERY_FAILED_UNAUTHORIZED";
    /** 功能码单个查询失败：后台操作失败   */
    public static final String FN_QUERY_FAILED = "FN_QUERY_FAILED";
    /** 功能码单个查询失败：功能号未注册    */
    public static final String FN_QUERY_FAILED_FN_UNREGISTER = "FN_QUERY_FAILED_FN_UNREGISTER";
    /**功能码单个查询失败**********************************************************************/

    /**PTT申请话权失败**********************************************************************/
    /** PTT申请话权失败：  非法的操作，当前通话不是VGCS */
    public static final String PTT_REQUEST_FAILED_NOT_VGCS = "PTT_REQUEST_FAILED_NOT_VGCS";
    /** PTT申请话权失败：当前有其他成员已申请话权   */
    public static final String PTT_REQUEST_FAILED_CONFLICT = "PTT_REQUEST_FAILED_CONFLICT";
    /** PTT申请话权失败：后台操作申请话权失败    */
    public static final String PTT_REQUEST_FAILED = "PTT_REQUEST_FAILED";
    /**PTT申请话权失败**********************************************************************/

    /**PTT释放话权失败**********************************************************************/
    /** PTT释放话权失败：  非法的操作，当前通话不是VGCS */
    public static final String PTT_RELEASE_FAILED_NOT_VGCS = "PTT_RELEASE_FAILED_NOT_VGCS";
    /** PTT释放话权失败：当前成员未申请话权，无需释放   */
    public static final String PTT_RELEASE_FAILED_NO_PTT = "PTT_RELEASE_FAILED_NO_PTT";
    /** PTT释放话权失败：后台操作申请话权失败    */
    public static final String PTT_RELEASE_FAILED = "PTT_RELEASE_FAILED";
    /**PTT释放话权失败**********************************************************************/

    /**呼叫失败原因**********************************************************************/
    // Q850 numbers
    public final static int Q850_NOREASON = 0;
    /**抢占*/
    public final static int Q850_PREEMPTION = 8;
    /**抢占失败*/
    public final static int Q850_PRECEDENCE_CALL_BLOCKED = 46;
    /**退出组呼*/
    public final static int Q850_QUIT_GROUP_CALL = 112;
    /**拒绝，服务器拒绝加入呼叫中组*/
    public final static int Q850_CALL_REJECTED = 21;

    /**用户位置错误：没有小区号*/
    public final static int SIP_CELLID_NOTEXIST = 440;
    /**用户功能号缺失：组呼发起者没有携带功能号*/
    public final static int SIP_FN_NOTEXIST = 441;
    /**无发起权限：组呼发起者携带功能号没有配置*/
    public final static int SIP_FN_FORBID = 442;
    /**组呼号不存在：呼叫的群组号码没有配置*/
    public final static int SIP_GROUP_NOTEXIST = 443;
    /**呼叫失败原因：心跳释放*/
    public final static int SIP_KEEPALIVE_RELEASE = 452;
    /**呼叫失败原因：对端离线*/
    public final static int SIP_OFFLINE = 454;
    /**呼叫释放原因：主席释放*/
    public final static int CALL_FAILED_PRESIDENT_RELEASE = 457;
    /**免打扰模式：拒绝接听呼入*/
    public final static int SIP_CALL_DND = 475;
    /** 呼叫失败原因：呼叫超时（无人接听）    */
    public static final int CALL_FAILED_TIMEOUT = 408;
    /** 呼叫失败原因：拒绝接听    */
    public static final int CALL_FAILED_REFUSE = 406;
    /** 呼叫失败原因：呼叫忙    */
    public static final int CALL_FAILED_BUSY = 486;
    /** 呼叫失败原因：不在线    */
    public static final int CALL_FAILED_OFFLINE = 480;
    /** 呼叫失败原因：被叫无应答    */
    public static final int CALL_FAILED_CALLEE_ACK_LOCK = 482;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_441 = 441;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_442 = 442;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_444 = 444;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_445 = 445;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_446 = 446;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_447 = 447;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_448 = 448;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_449 = 449;
    /** 呼叫失败原因：对方不可及（无法建立）    */
    public static final int CALL_FAILED_UNREACHABLE_450 = 450;
    /** 呼叫失败原因：取消响应    */
    public static final int CALL_FAILED_CANCEL_RSP = 487;
    /** 呼叫失败原因：空号    */
    public static final int CALL_FAILED_NOACCOUNT = 404;
    /** 呼叫失败原因：没有权限    */
    public static final int CALL_FAILED_FORBID = 403;
    public static final int SCALL_FAILED_UNREACHABLE = 605;
    /** 呼叫失败原因：被拦截    */
    public static final int SCALL_FAILED_INTERCEPT = 606;

//    /** 呼叫失败原因：呼叫被抢占    */
//    public static final String CALL_FAILED_PREEMPTED = "callFailed_preempted";
//    /** 呼叫失败原因：呼叫抢占失败    */
//    public static final String CALL_FAILED_PREEMPTFAILED = "callFailed_preemptFailed";
//    /** 呼叫失败原因：加入呼叫中组失败    */
//    public static final String CALL_FAILED_ACTIVEGROUP = "callFailed_activeGroupFailed";
//    /** 呼叫失败原因：没有小区号    */
//    public static final String CALL_FAILED_CELLID_NOTEXIST = "callFailed_cellidNotExist";
//    /** 呼叫失败原因：无主叫功能号    */
//    public static final String CALL_FAILED_FN_NOTEXIST = "callFailed_fnNotExist";
//    /** 呼叫失败原因：主叫功能号无权限    */
//    public static final String CALL_FAILED_FN_FORBID = "callFailed_fnForbid";
//    /** 呼叫失败原因： 没有配置该群组   */
//    public static final String CALL_FAILED_GROUP_NOTEXIST = "callFailed_groupNotExist";

    /**呼叫失败原因**********************************************************************/

    /**呼叫取消|拒绝|退出|结束原因**********************************************************************/

    /** 呼叫取消|拒绝|退出|结束原因：手动    */
    public static final String CALL_REASON_HANDLE = "hangupCall_handle";
    /** 呼叫取消|拒绝|退出|结束原因：抢占失败    */
    public static final String CALL_REASON_SEIZEFAILED = "hangupCall_seizeFailed";
    /** 呼叫取消|拒绝|退出|结束原因：被抢占    */
    public static final String CALL_REASON_SEIZED = "hangupCall_seized";

    /**呼叫取消|拒绝|退出|结束原因**********************************************************************/

    /**接听类型**********************************************************************/

    /** 接听类型：手动接听    */
    public static final String ANSWER_TYPE_HANDLE = "answerCall_handle";
    /** 接听类型：自动应答    */
    public static final String ANSWER_TYPE_AUTO = "answerCall_auto";

    /**接听类型**********************************************************************/

    /**临时组创建失败原因**********************************************************************/

    /** 临时组创建失败原因：数量超限    */
    public static final String TMPCREATE_FAILED_OUTSIDE = "tmpCreateFailed_outside";
    /** 临时组创建失败原因：处理失败    */
    public static final String TMPCREATE_FAILED_HANDLE = "tmpCreateFailed_handle";
    /** 临时组创建失败原因：成员超限    */
    public static final String TMPCREATE_FAILED_MEMBER_OUTSIDE = "tmpCreateFailed_memberOutside";
    /** 临时组创建失败原因：参数错误    */
    public static final String TMPCREATE_FAILED_PARAMERROR = "tmpCreateFailed_paramError";
    /** 临时组修改失败原因：禁止创建    */
    public static final String TMPCREATE_FAILED_FORBID = "tmpCreateFailed_forbid";
    /** 临时组创建失败原因：创建超时    */
    public static final String TMPCREATE_FAILED_TIMEOUT = "tmpCreateFailed_timeout";
    /** 临时组创建失败原因：无权限    */
    public static final String TMPCREATE_FAILED_UNAUTHORIZED = "tmpCreateFailed_unauthorized";

    /**临时组创建失败原因**********************************************************************/

    /**临时组修改失败原因**********************************************************************/

    /** 临时组修改失败原因：处理失败    */
    public static final String TMPMODIFY_FAILED_HANDLE = "tmpModifyFailed_handle";
    /** 临时组修改失败原因：成员超限    */
    public static final String TMPMODIFY_FAILED_MEMBER_OUTSIDE = "tmpModifyFailed_memberOutside";
    /** 临时组修改失败原因：参数错误    */
    public static final String TMPMODIFY_FAILED_PARAMERROR = "tmpModifyFailed_paramError";
    /** 临时组修改失败原因：号码不存在    */
    public static final String TMPMODIFY_FAILED_NOTEXIST = "tmpModifyFailed_notExist";
    /** 临时组修改失败原因：禁止修改    */
    public static final String TMPMODIFY_FAILED_FORBID = "tmpModifyFailed_forbid";
    /** 临时组修改失败原因：修改超时    */
    public static final String TMPMODIFY_FAILED_TIMEOUT = "tmpModifyFailed_timeout";

    /**临时组创建失败原因**********************************************************************/

    /**临时组删除失败原因**********************************************************************/

    /** 临时组删除失败原因：处理失败    */
    public static final String TMPREMOVE_FAILED_HANDLE = "tmpRemoveFailed_handle";
    /** 临时组删除失败原因：号码不存在    */
    public static final String TMPREMOVE_FAILED_NOTEXIST = "tmpRemoveFailed_notExist";
    /** 临时组修改失败原因：禁止删除    */
    public static final String TMPREMOVE_FAILED_FORBID = "tmpRemoveFailed_forbid";
    /** 临时组删除失败原因：删除超时    */
    public static final String TMPREMOVE_FAILED_TIMEOUT = "tmpRemoveFailed_timeout";

    /**临时组创建失败原因**********************************************************************/

    /**重联组加入失败原因**********************************************************************/

    /** 重联组加入失败原因：群组数量超限    */
    public static final String DOUBLEHEADJOIN_FAILED_OUTSIDE = "doubleHeadJoinFailed_outside";
    /** 重联组加入失败原因：处理失败    */
    public static final String DOUBLEHEADJOIN_FAILED_HANDLE = "doubleHeadJoinFailed_handle";
    /** 重联组加入失败原因：成员超限    */
    public static final String DOUBLEHEADJOIN_FAILED_MEMBER_OUTSIDE = "doubleHeadJoinFailed_memberOutside";
    /** 重联组加入失败原因：禁止创建    */
    public static final String DOUBLEHEADJOIN_FAILED_FORBID = "doubleHeadJoinFailed_forbid";
    /** 重联组加入失败原因：创建超时    */
    public static final String DOUBLEHEADJOIN_FAILED_TIMEOUT = "doubleHeadJoinFailed_timeout";

    /**重联组加入失败原因**********************************************************************/

    /**重联组退出失败原因**********************************************************************/

    /** 重联组退出失败原因：处理失败    */
    public static final String DOUBLEHEADUNJOIN_FAILED_HANDLE = "doubleHeadUnjoinFailed_handle";
    /** 重联组退出失败原因：号码不存在    */
    public static final String DOUBLEHEADUNJOIN_FAILED_NOTEXIST = "doubleHeadUnjoinFailed_notExist";
    /** 重联组退出失败原因：禁止创建    */
    public static final String DOUBLEHEADUNJOIN_FAILED_FORBID = "doubleHeadUnjoinFailed_forbid";
    /** 重联组退出失败原因：创建超时    */
    public static final String DOUBLEHEADUNJOIN_FAILED_TIMEOUT = "doubleHeadUnjoinFailed_timeout";

    /**重联组加入失败原因**********************************************************************/

    /**呼叫失败原因**********************************************************************/
    /** chaimb ADD*/
    /**通话结束:主叫释放*/
    public final static int CALL_END_CALLER_RELEASE = 1001;
    /**通话结束:被叫释放*/
    public final static int CALL_END_PEER_RELEASE = 1002;
    /**通话结束:无应答*/
    public final static int CALL_END_NO_RESPONSE = 1003;
    /**通话结束:被叫不在线*/
    public final static int CALL_END_PEER_OFFLINE = 1004;
    /**通话结束:被叫无响应*/
    public final static int CALL_END_PEER_NO_RESPONSE = 1005;
    /**通话结束:被叫无应答*/
    public final static int CALL_END_PEER_NO_ANSWER = 1006;
    /**通话结束:呼叫取消*/
    public final static int CALL_END_CALL_CANCEL = 1007;
    /**通话结束:空号或用户不在线*/
    public final static int CALL_END_SPACE_OR_USER_NO_ONLINE = 1008;
    /**通话结束:被叫用户忙*/
    public final static int CALL_END_PEER_BUSY = 1009;
    /**通话结束:呼叫被拦截*/
    public final static int CALL_END_INTERCEPT = 1010;
    /**通话结束:心跳中断*/
    public final static int CALL_END_HEARTBEAT_TIMEOUT = 1011;
    /**通话结束:呼叫超时*/
    public final static int CALL_END_TIMEOUT = 1012;
    /** 通话结束: 呼叫已存在   */
    public static final int CALL_END_EXISTED = 1013;
    /** 通话结束:没有权限    */
    public static final int CALL_END_FORBID = 1014;
    /** 通话结束:呼叫释放 */
    public final static int CALL_END_HUANGUP = 1015;
    /** 通话结束:网络异常 */
    public final static int CALL_END_OFFLINE = 1016;
    /** 通话结束:免打扰模式 */
    public final static int CALL_END_DND = 1017;
    /**通话结束：主席释放*/
    public final static int CALL_END_PRESIDENT_RELEASE = 1018;

    /** 会议成员离会:加入失败    */
    public static final int CONF_MEMBER_END_FAILED = 2001;
    /** 会议成员离会:成员离会    */
    public static final int CONF_MEMBER_END_OFFLINE = 2002;

    /**呼叫失败原因**********************************************************************/
}
