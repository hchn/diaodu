package com.jiaxun.sdk.dcl.model;

/**
 * @Description 数据类型 ：用户类型，号码类型
 * @author hz
 */
public class DataType {
	public static final int CONTACT_IDENT = 0;
	public static final int PHONE_IDENT = 1;
	
	
//	public static final String CONTACT_TYPE_1 = "调度用户";
//	public static final String CONTACT_TYPE_2 = "监控用户";
//	public static final String PHONE_TYPE_1 = "调度号码";
//	public static final String PHONE_TYPE_2 = "监控号码";
	
	/**
	 * 
	 */
	private int id;
	/**
	 * 数据标识 用户，号码
	 */
	private int dataIdent;
	/**
	 * 数据类型名称
	 */
	private String typeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDataIdent() {
		return dataIdent;
	}

	public void setDataIdent(int dataIdent) {
		this.dataIdent = dataIdent;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
