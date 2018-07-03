/*
 * 
 * Author: 
 * ChenGang 20131012 add for ReasonHeader
 */

package org.zoolu.sip.header;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/** 
 * Reason header 释放原因 
 * 
 * Q.850;cause=8;text=Preemption  被抢占、强拆
 * Q.850;cause=46;text=Precedence call blocked  抢占失败
 * Q.850;cause=112;text= creater quit the VGCS call   发起者退出VGCS
 * 
 * */
public class ReasonHeader extends Header {
	public final static String Reason850Type = "Q.850";
	public final static String ReasonSipType = "SIP";
	public final static String cause = "cause";
	public final static String text = "text";
	
	public final static int REASONTYPE_INDEX=0;
	public final static int CAUSE_INDEX=1;
	public final static int TEXT_INDEX=2;
	
	public final static String PREEMPTION = String.valueOf(CommonConstantEntry.Q850_PREEMPTION);
	public final static String PRECEDENCE = String.valueOf(CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED);
	public final static String QUIT_GROUP_CALL = String.valueOf(CommonConstantEntry.Q850_QUIT_GROUP_CALL);
	
	public final static String Q850_8_Preemption = Reason850Type+";"+cause+"="+PREEMPTION+";"+text+"="+"\"Preemption\"";
	public final static String Q850_46_Precedence = Reason850Type+";"+cause+"="+PRECEDENCE+";"+text+"="+"\"Precedence call blocked\"";
	// Modified by hubin at 20131031 退出群组
	public final static String Q850_112_QUIT_GROUP_CALL = Reason850Type+";"+cause+"="+QUIT_GROUP_CALL+";"+text+"="+"\" creater quit the VGCS call \"";
	
	public final static String CELLID_NOTEXIST = String.valueOf(CommonConstantEntry.SIP_CELLID_NOTEXIST);
	public final static String FN_NOTEXIST = String.valueOf(CommonConstantEntry.SIP_FN_NOTEXIST);
	public final static String FN_FORBID = String.valueOf(CommonConstantEntry.SIP_FN_FORBID);
	public final static String GROUP_NOTEXIST = String.valueOf(CommonConstantEntry.SIP_GROUP_NOTEXIST);
	public final static String CALL_DND = String.valueOf(CommonConstantEntry.SIP_CALL_DND);
	
	public final static String SIP_CELLID_ERROR = ReasonSipType+";"+cause+"="+CELLID_NOTEXIST+";"+text+"="+"\"Cellid error\"";
	public final static String SIP_FN_NOTEXIST = ReasonSipType+";"+cause+"="+FN_NOTEXIST+";"+text+"="+"\"fn \"";
	public final static String SIP_FN_ERROR = ReasonSipType+";"+cause+"="+FN_FORBID+";"+text+"="+"\"Preemption\"";
	public final static String SIP_CREATERFN_NOTEXIST = ReasonSipType+";"+cause+"="+GROUP_NOTEXIST+";"+text+"="+"\"Preemption\"";
	public final static String SIP_CALL_DND = ReasonSipType+";"+cause+"="+CALL_DND+";"+text+"="+"\"Do not disturb\"";
	
//	public String arrays[] = null;
		
	public ReasonHeader(String info) {
		super(SipHeaders.Reason, info);
	}

	public ReasonHeader(Header hd) {
		super(hd);
	}

	/** Gets UAC information */
	public String getInfo() {
		return value;
	}

	/** Sets the UAC information */
	public void setInfo(String info) {
		value = info;
	}
	
	//获取reason的第一个字段。此处应为 Q850
	public String getReasonType(){
		String arrays[] = value.split(";");
		if(arrays != null && arrays.length>REASONTYPE_INDEX){
			return arrays[REASONTYPE_INDEX].trim();
		}
		return "";
	}
	
	public String getCause(){
		String arrays[] = value.split(";");
		if(arrays != null && arrays.length>CAUSE_INDEX){
			String strCause[] = arrays[CAUSE_INDEX].trim().split("=");
			if(strCause != null && strCause.length>1)
				return strCause[1].trim();
		}
		return "";
	}
	
	public String getText(){
		String arrays[] = value.split(";");
		if(arrays != null && arrays.length>TEXT_INDEX){
			return arrays[TEXT_INDEX].trim();
		}
		return "";
	}
	
	/**获取释放原因*/
	public int getReason(){
	    String reasonType = getReasonType();
	    String txtCause = getCause();
		if(Reason850Type.equals(reasonType)){
			if(PREEMPTION.equals(txtCause)){
				return CommonConstantEntry.Q850_PREEMPTION;
			}
			else if(PRECEDENCE.equals(txtCause)){
				return CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED;
			}
			// Modified by hubin at 20131031 退出群组
			else if (QUIT_GROUP_CALL.equals(txtCause)) {
                return CommonConstantEntry.Q850_QUIT_GROUP_CALL;
            }
		}
		else if(ReasonSipType.equals(reasonType))
		{
		    if(CELLID_NOTEXIST.equals(txtCause)){
                return CommonConstantEntry.SIP_CELLID_NOTEXIST;
            }
            else if(FN_NOTEXIST.equals(txtCause)){
                return CommonConstantEntry.SIP_FN_NOTEXIST;
            }
            else if(FN_FORBID.equals(txtCause)){
                return CommonConstantEntry.SIP_FN_FORBID;
            }
            else if(GROUP_NOTEXIST.equals(txtCause)){
                return CommonConstantEntry.SIP_GROUP_NOTEXIST;
            }
            else if(CALL_DND.equals(txtCause)){
                return CommonConstantEntry.SIP_CALL_DND;
            }
		}
		
		return CommonConstantEntry.Q850_NOREASON;
	}
}
