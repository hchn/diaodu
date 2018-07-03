package com.jiaxun.sdk.scl.session.vs;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.module.vs.callback.AclVsEventListener;
import com.jiaxun.sdk.acl.module.vs.itf.AclVsService;
import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;
import com.jiaxun.sdk.scl.module.vs.impl.SclVsServiceImpl;

/**
 * ˵��������״̬����
 *
 * @author  hubin
 *
 * @Date 2015-4-13
 */
public interface VsStateHandler extends AclVsEventListener
{
    AclVsService vsService = AclServiceFactory.getAclVsService();
    SclVsEventListener callback = SclVsServiceImpl.getInstance().getSclVsEventListener();
    
    /**
     * ����˵�� : �򿪼��
     * @param sessionId
     * @param videoNum
     * @param priority
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void openVs(String sessionId, String videoNum, int priority) throws Exception;

    /**
     * ����˵�� : �رռ��
     * @param sessionId
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void closeVs(String sessionId) throws Exception;
    
}
