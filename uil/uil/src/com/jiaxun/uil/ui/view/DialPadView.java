package com.jiaxun.uil.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.InputFilterUtil;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.UilConstantEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：拨号盘的布局
 *
 * @author  chaimb
 *
 * @Date 2015-6-29
 */
public class DialPadView extends RelativeLayout implements OnClickListener, OnLongClickListener
{

    private static final String TAG = DialPadView.class.getName();
    private RelativeLayout dialLayout;
    private EditText dialEditText;
    private ImageView dialDelImageView;
    private ImageView dialShowImageView;
    private LinearLayout dialLinearLayout;

    private Button clearButton;
    private int currentIndex;
    private Editable editable;

    private Context context;

    public boolean isCall = false;
    private ArrayList<CallListItem> currentCallList;

    public interface callCallBack
    {
        void shutSownDial();
    }

    public DialPadView(Context context)
    {
        super(context);
        initView(context);
        this.context = context;
    }

    public DialPadView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
        this.context = context;
    }

    public DialPadView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView(context);
        this.context = context;
    }

    private void initView(Context context)
    {
        if (dialLayout == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialLayout = (RelativeLayout) inflater.inflate(R.layout.activity_dial, this);
        }

        // 拨号盘业务
        dialLinearLayout = (LinearLayout) dialLayout.findViewById(R.id.dialed_linearlayout);
        dialShowImageView = (ImageView) dialLayout.findViewById(R.id.show_dial_imageview);
        dialEditText = (EditText) dialLayout.findViewById(R.id.dial_edittext);
        dialDelImageView = (ImageView) dialLayout.findViewById(R.id.dial_del_imageview);
        clearButton = (Button) dialLayout.findViewById(R.id.cleardial_button);

        dialLinearLayout.setVisibility(View.VISIBLE);

        // 拨号盘界面隐藏软件盘
        UiUtils.hideSoftInputMethod(context, dialEditText);

        // 号码字符限制
        dialEditText.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(UilConstantEntry.NUMBER_SIGN_LIMIT_REGEX) });

        // 得到焦点
//        dialEditText.requestFocus();

        setListener(dialLayout);

        currentCallList = ServiceUtils.getCurrentCallList();

    }

    // 设置监听事件
    private void setListener(RelativeLayout dialLayout)
    {
        dialLayout.findViewById(R.id.id0).setOnClickListener(this);
        dialLayout.findViewById(R.id.id1).setOnClickListener(this);
        dialLayout.findViewById(R.id.id2).setOnClickListener(this);
        dialLayout.findViewById(R.id.id3).setOnClickListener(this);
        dialLayout.findViewById(R.id.id4).setOnClickListener(this);
        dialLayout.findViewById(R.id.id5).setOnClickListener(this);
        dialLayout.findViewById(R.id.id6).setOnClickListener(this);
        dialLayout.findViewById(R.id.id7).setOnClickListener(this);
        dialLayout.findViewById(R.id.id8).setOnClickListener(this);
        dialLayout.findViewById(R.id.id9).setOnClickListener(this);
        dialLayout.findViewById(R.id.id_x).setOnClickListener(this);
        dialLayout.findViewById(R.id.id_j).setOnClickListener(this);
        dialLayout.findViewById(R.id.start_call).setOnClickListener(this);
        dialLayout.findViewById(R.id.again_start_call).setOnClickListener(this);

        dialDelImageView.setOnClickListener(this);
        dialShowImageView.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        dialDelImageView.setOnLongClickListener(this);
        clearButton.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.show_dial_imageview:
                if (dialLinearLayout.getVisibility() == View.GONE)
                {
                    dialShowImageView.setImageResource(R.drawable.close_dialed_imageview);
                    dialLinearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    dialShowImageView.setImageResource(R.drawable.open_dialed_imageview);
                    dialLinearLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.id0:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();

                editable.insert(currentIndex, "0");
                sendDtmf(currentCallList, '0');
                break;
            case R.id.id1:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();

                editable.insert(currentIndex, "1");
                sendDtmf(currentCallList, '1');
                break;
            case R.id.id2:

                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();
                editable.insert(currentIndex, "2");
                sendDtmf(currentCallList, '2');
                break;
            case R.id.id3:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();

                editable.insert(currentIndex, "3");
                sendDtmf(currentCallList, '3');

                break;
            case R.id.id4:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();
                editable.insert(currentIndex, "4");
                sendDtmf(currentCallList, '4');

                break;
            case R.id.id5:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();

                editable.insert(currentIndex, "5");
                sendDtmf(currentCallList, '5');
                break;
            case R.id.id6:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();
                editable.insert(currentIndex, "6");
                sendDtmf(currentCallList, '6');

                break;
            case R.id.id7:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();
                editable.insert(currentIndex, "7");
                sendDtmf(currentCallList, '7');
                break;
            case R.id.id8:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();

                editable.insert(currentIndex, "8");
                sendDtmf(currentCallList, '8');
                break;
            case R.id.id9:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();

                editable.insert(currentIndex, "9");
                sendDtmf(currentCallList, '9');
                break;
            case R.id.id_x:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();
                editable.insert(currentIndex, "*");
                sendDtmf(currentCallList, '*');

                break;
            case R.id.id_j:
                editable = dialEditText.getText();
                currentIndex = dialEditText.getSelectionStart();
                editable.insert(currentIndex, "#");
                sendDtmf(currentCallList, '#');
                break;
            case R.id.start_call:
                // 左手柄拨号
                String callNum = dialEditText.getText().toString();

                callNum(callNum);

                break;
            case R.id.again_start_call:
                // 重播
                for (CallRecord callRecord : UiApplication.getCallRecordService().getAllCallRecords())
                {
                    int callType = callRecord.getCallType();
                    boolean isChairMan = callRecord.isChairman();
                    if (callRecord.isOutGoing() && callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
                    {// 视频监控
                        UiApplication.getVsService().addVsUser(callRecord.getPeerNum());
                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);
                        break;
                    }
                    else if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                    {// 判断最近发起的会议
                        if (isChairMan)
                        {// 主席
                            String confId = callRecord.getConfId();
                            // 重播会议成员列表
                            ArrayList<String> numberList = new ArrayList<String>();
                            ArrayList<CallRecord> confRecord = UiApplication.getCallRecordService().getConfCallRecordList(confId);
                            for (CallRecord record : confRecord)
                            {
                                String number = record.getPeerNum();
                                if (!TextUtils.isEmpty(number))
                                {
                                    if (!(numberList.contains(number)))
                                    {// 去除重复数据
                                        numberList.add(number);
                                    }
                                }
                            }
                            ServiceUtils.makeConf(context, callRecord.getConfName(), numberList);
                            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);
                            break;
                        }
                    }
                    else
                    {// 普通拨号
                        if (callRecord.isOutGoing())
                        {
                            ServiceUtils.makeCall(context, callRecord.getPeerNum());
                            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);
                            break;
                        }
                    }
                }

                break;
//            case R.id.right_start_call:
//                // 右手柄拨号
//                callNum = dialEditText.getText().toString();
//                if (!TextUtils.isEmpty(callNum))
//                {
//                    ServiceUtils.makeCall(callNum);
////                    ((DialActivity)context).finish();
//                    
//                }
//                break;
            case R.id.cleardial_button:
            case R.id.dial_del_imageview:
                editable = dialEditText.getText();

                currentIndex = dialEditText.getSelectionStart();
                if (currentIndex > 0)
                {
                    editable.delete(currentIndex - 1, currentIndex);
                }
                break;

            default:
                break;
        }

    }

    private void callNum(String callNum)
    {
        if (!TextUtils.isEmpty(callNum))
        {
            ContactModel contactModel = UiApplication.getContactService().getContactByPhoneNum(callNum);

            if (contactModel != null)
            {
                if (ContactUtil.isVsByContactType(contactModel.getTypeName()))
                {
                    UiApplication.getVsService().addVsUser(callNum);
                }
                else
                {
                    ServiceUtils.makeCall(context, callNum);
                }
            }
            else
            {

                ServiceUtils.makeCall(context, callNum);
            }
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);
            dialEditText.setText("");
        }
        else
        {
            ToastUtil.showToast("号码为空，不能拨号！");
        }
    }

    /**
     * 方法说明 :二次拨号
     * @param currentCallList 当前通话列表
     * @param num
     * @author chaimb
     * @Date 2015-10-9
     */
    private void sendDtmf(ArrayList<CallListItem> currentCallList, char num)
    {
        if (currentCallList != null && currentCallList.size() > 0)
        {
            CallListItem callListItem = currentCallList.get(0);
            int status = callListItem.getStatus();
            if (status == CommonConstantEntry.SCALL_STATE_CONNECT || status == CommonConstantEntry.SCALL_STATE_REMOTE_HOLD)
            {
                dialEditText.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(UilConstantEntry.NUMBER_SIGN_LIMIT_NOLENGTH_REGEX) });
                UiApplication.getSCallService().sCallDtmfSend(callListItem.getCallModel().getSessionId(), num);
            }
            else
            {
            }
        }
        else
        {
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cleardial_button:
            case R.id.dial_del_imageview:
                dialEditText.setText("");
                break;

            default:
                break;
        }
        return false;
    }

}
