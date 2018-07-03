package com.jiaxun.sdk.dcl.model;

/**
 * @Description �������� ���û����ͣ���������
 * @author hz
 */
public class DataType {
	public static final int CONTACT_IDENT = 0;
	public static final int PHONE_IDENT = 1;
	
	
//	public static final String CONTACT_TYPE_1 = "�����û�";
//	public static final String CONTACT_TYPE_2 = "����û�";
//	public static final String PHONE_TYPE_1 = "���Ⱥ���";
//	public static final String PHONE_TYPE_2 = "��غ���";
	
	/**
	 * 
	 */
	private int id;
	/**
	 * ���ݱ�ʶ �û�������
	 */
	private int dataIdent;
	/**
	 * ������������
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
