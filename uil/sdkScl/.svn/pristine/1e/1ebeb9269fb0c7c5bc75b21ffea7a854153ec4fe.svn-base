package com.jiaxun.sdk.scl.media;

import java.net.URI;

import android.view.Surface;

/**
 * 说明：
 *
 * @author  jiaxun
 *
 * @Date 2015-1-30
 */
public interface MediaChannel
{
    // audio方法
//    int audioLocalRecord( RecordInfo info); // 本地录音
    int audioLocaRecordStop();      // 停止本地录音
    int audioLocalPlay(URI uri,  boolean loop); // 本地播放语音（是否循环）
    int audioLocalPlay(int toneId);         // 本地送信号音
    int audioLocalPlayStop();           // 停止本地放音
    int audioRemotePlay(URI uri,  boolean loop);    // 
    int audioRemotePlay(int toneId);
    int audioRemotePlayStop();
//    int audioSetStream(Stream stream);  //  设置RTP / 时隙参数
    int audioStreamControl(boolean tx, boolean rx);  // 设置RTP / 时隙收发状态
    int setTalkVolume(int mic, int handfree, int handset, int headset, int bluetooth) ;  // 0-100,  -1 = auto
    int setMusicVolume( int handfree, int handset, int headset, int bluetooth) ; // //0-100,  -1=auto
    int setRingVolume(int loudRing, int softRing);   // 设置大小铃音量，0-100, 
    int sendDtmf(String dtmf);
    boolean isBusy();   // 是否在通话中
    boolean sendRing(int ringId);   // 送振铃
    boolean stopRing();     // 停止振铃
    
    
    // video方法
    int openLocalVideo(Surface surface);
    int closeLocalVideo(Surface surface);
//    int setStream(Stream stream);
    int openRemoteVideo(Surface surface);
    int closeRemoteVideo(Surface surface);
    int videoPlay(Surface surface, URI uri);
    int stopPlay(Surface surface);


}
