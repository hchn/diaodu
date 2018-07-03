package com.jiaxun.sdk.scl.media;

import java.net.URI;

import android.view.Surface;

/**
 * ˵����
 *
 * @author  jiaxun
 *
 * @Date 2015-1-30
 */
public interface MediaChannel
{
    // audio����
//    int audioLocalRecord( RecordInfo info); // ����¼��
    int audioLocaRecordStop();      // ֹͣ����¼��
    int audioLocalPlay(URI uri,  boolean loop); // ���ز����������Ƿ�ѭ����
    int audioLocalPlay(int toneId);         // �������ź���
    int audioLocalPlayStop();           // ֹͣ���ط���
    int audioRemotePlay(URI uri,  boolean loop);    // 
    int audioRemotePlay(int toneId);
    int audioRemotePlayStop();
//    int audioSetStream(Stream stream);  //  ����RTP / ʱ϶����
    int audioStreamControl(boolean tx, boolean rx);  // ����RTP / ʱ϶�շ�״̬
    int setTalkVolume(int mic, int handfree, int handset, int headset, int bluetooth) ;  // 0-100,  -1 = auto
    int setMusicVolume( int handfree, int handset, int headset, int bluetooth) ; // //0-100,  -1=auto
    int setRingVolume(int loudRing, int softRing);   // ���ô�С��������0-100, 
    int sendDtmf(String dtmf);
    boolean isBusy();   // �Ƿ���ͨ����
    boolean sendRing(int ringId);   // ������
    boolean stopRing();     // ֹͣ����
    
    
    // video����
    int openLocalVideo(Surface surface);
    int closeLocalVideo(Surface surface);
//    int setStream(Stream stream);
    int openRemoteVideo(Surface surface);
    int closeRemoteVideo(Surface surface);
    int videoPlay(Surface surface, URI uri);
    int stopPlay(Surface surface);


}
