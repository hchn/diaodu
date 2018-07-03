package com.jiaxun.sdk.dcl.module.attendant.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.Attendant;

/**
 * ˵��������̨���ز����û�����ӿ�
 *
 * @author  hubin
 *
 * @Date 2015-3-16
 */
public interface DclAtdService
{
    /**
     * ����˵�� : �Ƿ��Ѿ���½
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdLogin();

    /**
     * ����˵�� : �Ƿ����Ա�Ѿ���½
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdAdminLogin();

    /**
     * ����˵�� : ��֤��½������
     * @param login
     * @return
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdNameValid(String login);

    /**
     * ����˵�� : ��֤��������
     * @param password
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdLoginedPasswordValid(String password);

    /**
     * ����˵�� : ��½����������֤
     * @param login
     * @param password
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean isAtdAuthorized(String login, String password);

    /**
     * ����˵�� : ����û�
     * @param user
     * @return int 
     * @author hubin
     * @Date 2015-9-10
     */
    int addAtd(Attendant user);

    /**
     * ����˵�� : ɾ���û�
     * @param login
     * @return boolean
     * @author hubin
     * @Date 2015-9-10
     */
    boolean removeAtd(String login);

    /**
     * ����˵�� : ɾ���û�
     * @param user
     * @return int
     * @author hubin
     * @Date 2015-9-10
     */
    int modifyAtdInfo(Attendant user);

    /**
     * ����˵�� : ��ȡ�û��б�
     * @return ArrayList<Attendant>
     * @author hubin
     * @Date 2015-9-10
     */
    ArrayList<Attendant> getAttendants();

    /**
     * ����˵�� : ��ȡ��ǰ��¼�û�
     * @return Attendant
     * @author hubin
     * @Date 2015-9-10
     */
    Attendant getLoginedAttendant();

}
