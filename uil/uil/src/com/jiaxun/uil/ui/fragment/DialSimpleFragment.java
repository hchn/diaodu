package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.view.SelectedContactView;
import com.jiaxun.uil.util.InputFilterUtil;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.UilConstantEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：精简版拨号盘
 *
 * @author  chaimb
 *
 * @Date 2015-9-9
 */
public class DialSimpleFragment extends BaseFragment implements OnClickListener, OnLongClickListener
{
    private EditText numEditText;
    private TextView clearBtn;

    private TextView cancelBtn;
    private TextView sureBtn;
    private TextView selectAgainBtn;

    private LinearLayout numAddLayout;

    private int currentIndex;
    private Editable editable;

//    private EnumContactSelectType eventType = EnumContactSelectType.TEMP_CONF;
    private int callBack;
    private List<String> numList = new ArrayList<String>();

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_dial_add_conf_member;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (getArguments() != null)
        {
            callBack = getArguments().getInt(CommonConstantEntry.DATA_TYPE, 0);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initComponentViews(View view)
    {
        numEditText = (EditText) view.findViewById(R.id.num_edittext);
        numAddLayout = (LinearLayout) view.findViewById(R.id.linelay_add_num);

        clearBtn = (TextView) view.findViewById(R.id.clear_num_textview);
        cancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
        sureBtn = (TextView) view.findViewById(R.id.sure_btn);
        selectAgainBtn = (TextView) view.findViewById(R.id.select_again_btn);
        selectAgainBtn.setVisibility(View.GONE);

        view.findViewById(R.id.dial_callrecord_layout).setVisibility(View.GONE);

        setListener(view);

        // 隐藏拨号盘
        UiUtils.hideSoftInputMethod(getActivity(), numEditText);

        // 号码字符限制
        numEditText.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(UilConstantEntry.NUMBER_LIMIT_REGEX) });
    }

    private void updateAddNumLayout()
    {
        numAddLayout.removeAllViews();
        for (String num : numList)
        {

            SelectedContactView textView = new SelectedContactView(parentActivity);
            textView.setTag(num);
            textView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String selNum = (String) v.getTag();
                    numList.remove(selNum);
                    updateAddNumLayout();
                }
            });
            textView.setText(num);
            numAddLayout.addView(textView);
        }
    }

    private void setListener(View view)
    {
        view.findViewById(R.id.id0).setOnClickListener(this);
        view.findViewById(R.id.id1).setOnClickListener(this);
        view.findViewById(R.id.id2).setOnClickListener(this);
        view.findViewById(R.id.id3).setOnClickListener(this);
        view.findViewById(R.id.id4).setOnClickListener(this);
        view.findViewById(R.id.id5).setOnClickListener(this);
        view.findViewById(R.id.id6).setOnClickListener(this);
        view.findViewById(R.id.id7).setOnClickListener(this);
        view.findViewById(R.id.id8).setOnClickListener(this);
        view.findViewById(R.id.id9).setOnClickListener(this);
        view.findViewById(R.id.id_x).setOnClickListener(this);
        view.findViewById(R.id.id_j).setOnClickListener(this);

        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        selectAgainBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        clearBtn.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.id0:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();

                editable.insert(currentIndex, "0");
                break;
            case R.id.id1:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();

                editable.insert(currentIndex, "1");
                break;
            case R.id.id2:

                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();
                editable.insert(currentIndex, "2");
                break;
            case R.id.id3:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();

                editable.insert(currentIndex, "3");

                break;
            case R.id.id4:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();
                editable.insert(currentIndex, "4");

                break;
            case R.id.id5:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();

                editable.insert(currentIndex, "5");
                break;
            case R.id.id6:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();
                editable.insert(currentIndex, "6");

                break;
            case R.id.id7:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();
                editable.insert(currentIndex, "7");
                break;
            case R.id.id8:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();

                editable.insert(currentIndex, "8");
                break;
            case R.id.id9:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();

                editable.insert(currentIndex, "9");
                break;
            case R.id.id_x:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();
                editable.insert(currentIndex, "*");

                break;
            case R.id.id_j:
                editable = numEditText.getText();
                currentIndex = numEditText.getSelectionStart();
                editable.insert(currentIndex, "#");
                break;
            case R.id.clear_num_textview:
                editable = numEditText.getText();

                currentIndex = numEditText.getSelectionStart();
                if (currentIndex > 0)
                {
                    editable.delete(currentIndex - 1, currentIndex);
                }
                break;

            case R.id.cancel_btn:
//                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_CONF_MEMBER_ADD, ConfControlFragment.SELECT_FROM_DIAL_PAD,null);
//                parentActivity.turnToFragmentStack(R.id.container_right_content, ConfControlFragment.class);
                parentActivity.backToPreFragment(R.id.container_right_content);
                break;
            case R.id.sure_btn:
                String inputNum = numEditText.getText().toString().trim();
                if (TextUtils.isEmpty(inputNum))
                {
                    ToastUtil.showToast("输入号码不能为空！");
                }
                else
                {
                    sureAdd(callBack, inputNum);
                }
                break;
            case R.id.select_again_btn:
                cancelSelect();
                break;

        }

    }

    private void sureAdd(int callback, String inputNum)
    {
//        if (callback == UiEventEntry.NOTIFY_CONF_MEMBER_ADD)
//        {
//            numEditText.setText("");
//            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_CONF_MEMBER_ADD, ConfControlFragment.SELECT_FROM_DIAL_PAD, inputNum);
//            parentActivity.removeFragmentFromBackStack(R.id.container_right_content, ContactSelectAddFragment.class);
//            parentActivity.removeFragmentFromBackStack(R.id.container_right_content, DialSimpleFragment.class);
//            parentActivity.turnToFragmentStack(R.id.container_right_content, ConfControlFragment.class);
//        }
//        else if (callback == UiEventEntry.NOTIFY_CONF_TEMP)
//        {
        numEditText.setText("");
        EventNotifyHelper.getInstance().postNotificationName(callback, inputNum);
//            parentActivity.removeFragmentFromBackStack(R.id.container_right_content, ContactSelectAddFragment.class);
//            parentActivity.removeFragmentFromBackStack(R.id.container_right_content, DialSimpleFragment.class);
//        }
//        // 添加监控用户列表
//        else if (callback == UiEventEntry.NOTIFY_VS_MEMBER_ADD)
//        {
//            numEditText.setText("");
//            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_VS_MEMBER_ADD, ConfControlFragment.SELECT_FROM_DIAL_PAD, inputNum);
//            parentActivity.turnToFragmentStack(R.id.container_right_content, VsListFragment.class);
////            parentActivity.removeFragmentFromBackStack(R.id.container_right_content, ContactSelectAddFragment.class);
////            parentActivity.removeFragmentFromBackStack(R.id.container_right_content, DialSimpleFragment.class);
//        }
    }

    /**
     * 
     * 方法说明 :取消选择
     * @author chaimb
     * @Date 2015-9-9
     */
    private void cancelSelect()
    {
        numList.clear();
        updateAddNumLayout();
    }

    @Override
    public boolean onLongClick(View v)
    {
        switch (v.getId())
        {
            case R.id.clear_num_textview:
                numEditText.setText("");
                break;

            default:
                break;
        }
        return false;
    }

}
