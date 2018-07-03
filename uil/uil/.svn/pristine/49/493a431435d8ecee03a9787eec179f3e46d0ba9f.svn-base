package com.jiaxun.test;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;

/**
 * 说明：测试功能
 *
 * @author  fuluo
 *
 * @Date 2015-6-5
 */
public class TestFragment extends BaseFragment implements OnClickListener,OnTouchListener
{
    private static final String TAG = TestFragment.class.getName();

    @Override
    public void initComponentViews(View view)
    {
        view.findViewById(R.id.l_0).setOnClickListener(this);
        view.findViewById(R.id.l_1).setOnClickListener(this);
        view.findViewById(R.id.l_2).setOnClickListener(this);
        view.findViewById(R.id.l_3).setOnClickListener(this);
        view.findViewById(R.id.l_4).setOnClickListener(this);
        view.findViewById(R.id.l_5).setOnClickListener(this);
        view.findViewById(R.id.l_6).setOnClickListener(this);
        view.findViewById(R.id.l_7).setOnClickListener(this);
        view.findViewById(R.id.l_8).setOnClickListener(this);
        view.findViewById(R.id.l_9).setOnClickListener(this);
        view.findViewById(R.id.l_x).setOnClickListener(this);
        view.findViewById(R.id.l_j).setOnClickListener(this);
        view.findViewById(R.id.l_sb).setOnTouchListener(this);
        view.findViewById(R.id.l_mk).setOnTouchListener(this);
        view.findViewById(R.id.l_mt).setOnTouchListener(this);
        view.findViewById(R.id.l_em).setOnTouchListener(this);
        view.findViewById(R.id.l_jy).setOnTouchListener(this);
        
        view.findViewById(R.id.r_0).setOnClickListener(this);
        view.findViewById(R.id.r_1).setOnClickListener(this);
        view.findViewById(R.id.r_2).setOnClickListener(this);
        view.findViewById(R.id.r_3).setOnClickListener(this);
        view.findViewById(R.id.r_4).setOnClickListener(this);
        view.findViewById(R.id.r_5).setOnClickListener(this);
        view.findViewById(R.id.r_6).setOnClickListener(this);
        view.findViewById(R.id.r_7).setOnClickListener(this);
        view.findViewById(R.id.r_8).setOnClickListener(this);
        view.findViewById(R.id.r_9).setOnClickListener(this);
        view.findViewById(R.id.r_x).setOnClickListener(this);
        view.findViewById(R.id.r_j).setOnClickListener(this);
        
        view.findViewById(R.id.r_j).setOnClickListener(this);
        view.findViewById(R.id.r_sb).setOnTouchListener(this);
        view.findViewById(R.id.r_mk).setOnTouchListener(this);
        view.findViewById(R.id.r_mt).setOnTouchListener(this);
        view.findViewById(R.id.r_em).setOnTouchListener(this);
        view.findViewById(R.id.r_jy).setOnTouchListener(this);
        
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_test;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.l_0:
                sendLeftNumberBroadcast("0");
                break;
            case R.id.l_1:
                sendLeftNumberBroadcast("1");
                break;
            case R.id.l_2:
                sendLeftNumberBroadcast("2");
                break;
            case R.id.l_3:
                sendLeftNumberBroadcast("3");
                break;
            case R.id.l_4:
                sendLeftNumberBroadcast("4");
                break;
            case R.id.l_5:
                sendLeftNumberBroadcast("5");
                break;
            case R.id.l_6:
                sendLeftNumberBroadcast("6");
                break;
            case R.id.l_7:
                sendLeftNumberBroadcast("7");
                break;
            case R.id.l_8:
                sendLeftNumberBroadcast("8");
                break;
            case R.id.l_9:
                sendLeftNumberBroadcast("9");
                break;
            case R.id.l_x:
                sendLeftNumberBroadcast("*");
                break;
            case R.id.l_j:
                sendLeftNumberBroadcast("#");
                break;
            case R.id.l_sb:
                sendBroadcast("");
                break;
            case R.id.l_mk:

                break;
            case R.id.l_mt:

                break;
            case R.id.l_em:

                break;
            case R.id.l_jy:

                break;
            case R.id.l_ptt:

                break;
            case R.id.r_0:
                sendRightNumberBroadcast("0");
                break;
            case R.id.r_1:
                sendRightNumberBroadcast("1");
                break;
            case R.id.r_2:
                sendRightNumberBroadcast("2");
                break;
            case R.id.r_3:
                sendRightNumberBroadcast("3");
                break;
            case R.id.r_4:
                sendRightNumberBroadcast("4");
                break;
            case R.id.r_5:
                sendRightNumberBroadcast("5");
                break;
            case R.id.r_6:
                sendRightNumberBroadcast("6");
                break;
            case R.id.r_7:
                sendRightNumberBroadcast("7");
                break;
            case R.id.r_8:
                sendRightNumberBroadcast("8");
                break;
            case R.id.r_9:
                sendRightNumberBroadcast("9");
                break;
            case R.id.r_x:
                sendRightNumberBroadcast("*");
                break;
            case R.id.r_j:
                sendRightNumberBroadcast("#");
                break;
            case R.id.r_sb:

                break;
            case R.id.r_mk:

                break;
            case R.id.r_mt:

                break;
            case R.id.r_em:

                break;
            case R.id.r_jy:

                break;
            case R.id.r_ptt:

                break;

            default:
                break;
        }
    }

    
    private void sendBroadcast(String action)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        this.getActivity().sendBroadcast(intent);
    }
    
    private void sendRightNumberBroadcast(String value)
    {
        sendBroadcast("jiaxun.action.rightmodule.number.down", value);
    }
    
    private void sendLeftNumberBroadcast(String value)
    {
        sendBroadcast("jiaxun.action.leftmodule.number.down", value);
    }
    
    private void sendBroadcast(String action, String value)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("value", value);
        this.getActivity().sendBroadcast(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (v.getId() == R.id.l_sb)
                {
                    sendBroadcast("jiaxun.action.leftmodule.handle.insert");
                }
                else if (v.getId() == R.id.l_mk)
                {
                    sendBroadcast("jiaxun.action.leftmodule.insert");
                }
                else if (v.getId() == R.id.l_mt)
                {
                    sendBroadcast("jiaxun.action.leftmodule.handsfree.down");
                }
                else if (v.getId() == R.id.l_em)
                {
                    sendBroadcast("jiaxun.action.leftmodule.cableheadset.insert");
                }
                else if (v.getId() == R.id.l_jy)
                {
                    sendBroadcast("jiaxun.action.leftmodule.mute.down");
                }
                else if (v.getId() == R.id.r_sb)
                {
                    sendBroadcast("jiaxun.action.rightmodule.handle.insert");
                }
                else if (v.getId() == R.id.r_mk)
                {
                    sendBroadcast("jiaxun.action.rightmodule.insert");
                }
                else if (v.getId() == R.id.r_mt)
                {
                    sendBroadcast("jiaxun.action.rightmodule.handsfree.down");
                }
                else if (v.getId() == R.id.r_em)
                {
                    sendBroadcast("jiaxun.action.rightmodule.cableheadset.insert");
                }
                else if (v.getId() == R.id.r_jy)
                {
                    sendBroadcast("jiaxun.action.rightmodule.mute.down");
                }
                break;
            case MotionEvent.ACTION_UP:
                if (v.getId() == R.id.l_sb)
                {
                    sendBroadcast("jiaxun.action.leftmodule.handle.pullout");
                }
                else if (v.getId() == R.id.l_mk)
                {
                    sendBroadcast("jiaxun.action.leftmodule.pullout");
                }
                else if (v.getId() == R.id.l_mt)
                {
                    sendBroadcast("jiaxun.action.leftmodule.handsfree.up");
                }
                else if (v.getId() == R.id.l_em)
                {
                    sendBroadcast("jiaxun.action.leftmodule.cableheadset.pullout");
                }
                else if (v.getId() == R.id.l_jy)
                {
                    sendBroadcast("jiaxun.action.leftmodule.mute.up");
                }
                else if (v.getId() == R.id.r_sb)
                {
                    sendBroadcast("jiaxun.action.rightmodule.handle.pullout");
                }
                else if (v.getId() == R.id.r_mk)
                {
                    sendBroadcast("jiaxun.action.rightmodule.pullout");
                }
                else if (v.getId() == R.id.r_mt)
                {
                    sendBroadcast("jiaxun.action.rightmodule.handsfree.up");
                }
                else if (v.getId() == R.id.r_em)
                {
                    sendBroadcast("jiaxun.action.rightmodule.cableheadset.pullout");
                }
                else if (v.getId() == R.id.r_jy)
                {
                    sendBroadcast("jiaxun.action.rightmodule.mute.up");
                }

                break;
            default:
                break;
        }
        return false;
    }

}
