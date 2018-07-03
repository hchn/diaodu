package com.jiaxun.uil.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.CallRecordSortAdapter;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.ui.view.DialPadView;
import com.jiaxun.uil.util.SearchContacts;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 
 * 说明：拨号盘窗口
 *
 * @author  chaimb
 *
 * @Date 2015-9-8
 */
public class DialPadWindow extends PopupWindow implements NotificationCenterDelegate
{

    private final static String TAG = DialPadWindow.class.getName();
    private final static String ACTION_LEFTMODULE_NUMBER_DOWN = "jiaxun.action.leftmodule.number.down";
    private final static String ACTION_RIGHTMODULE_NUMBER_DOWN = "jiaxun.action.rightmodule.number.down";
    private final static String ACTION_MODULE_NUMBER_DOWN = "jiaxun.action.module.number.down";

    private int currentIndex = 0;
    private Editable editable;

    private String action = "";
    private String leftNumber = "";
    private String rightNumber = "";

    private DialPadView leftLayout;
    private DialPadView rightLayout;

    private ListView leftListView;
    private ListView rightListView;

    private EditText leftDialEditText;
    private EditText rightDialEditText;

    private CallRecordSortAdapter sortLeftAdapter;
    private CallRecordSortAdapter sortRightAdapter;
    private SearchContacts sc = null;
    private List<BaseListItem> searchLeftList;
    private List<BaseListItem> searchRightList;

    private View contentView;
    private Context mContext;

    public DialPadWindow(Context context)
    {
        super(context);
        Log.info(TAG, "DialPopupWindow::");
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_dial_main, null);

        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.ACTION_MOUBLE_NUMBER);

        Bundle bundle = ((HomeActivity) mContext).getIntent().getExtras();

        leftLayout = (DialPadView) contentView.findViewById(R.id.left_dial_relativelayout);
        rightLayout = (DialPadView) contentView.findViewById(R.id.right_dial_relativelayout);

        leftDialEditText = (EditText) leftLayout.findViewById(R.id.dial_edittext);
        rightDialEditText = (EditText) rightLayout.findViewById(R.id.dial_edittext);

        leftListView = (ListView) leftLayout.findViewById(R.id.dial_listview);
        rightListView = (ListView) rightLayout.findViewById(R.id.dial_listview);

        // 左拨号盘
        sortLeftAdapter = new CallRecordSortAdapter(mContext, leftListView, new ArrayList<BaseListItem>());
        leftListView.setAdapter(sortLeftAdapter);

        // 右拨号盘
        sortRightAdapter = new CallRecordSortAdapter(mContext, rightListView, new ArrayList<BaseListItem>());
        rightListView.setAdapter(sortRightAdapter);

        if (bundle != null)
        {
            action = bundle.getString(ACTION_MODULE_NUMBER_DOWN);
            leftNumber = bundle.getString("leftnumber");
            rightNumber = bundle.getString("rightnumber");
            leftDialEditText.requestFocus();
            if (ACTION_LEFTMODULE_NUMBER_DOWN.equals(action))
            {// 左模块拨号
                leftLayout.setVisibility(View.VISIBLE);
                editable = leftDialEditText.getText();
                currentIndex = leftDialEditText.getSelectionStart();
                editable.insert(currentIndex, leftNumber);
                leftDialEditText.requestFocus();
            }
            else if (ACTION_RIGHTMODULE_NUMBER_DOWN.equals(action))
            {// 右模块拨号
                rightLayout.setVisibility(View.VISIBLE);
                editable = rightDialEditText.getText();
                currentIndex = rightDialEditText.getSelectionStart();
                editable.insert(currentIndex, rightNumber);
                rightDialEditText.requestFocus();
            }
            else
            {
            }
        }
        else
        {// 通过终端进入拨号盘
            leftLayout.setVisibility(View.VISIBLE);
            leftDialEditText.requestFocus();
        }

        setListener();

        TextView leftTextView = (TextView) leftLayout.findViewById(R.id.dial_textview);
        leftTextView.setText("左拨号盘");
        leftTextView.setVisibility(View.GONE);
        leftTextView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showRightDial();
            }
        });

        TextView rightTextView = (TextView) rightLayout.findViewById(R.id.dial_textview);
        rightTextView.setText("右拨号盘");

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setAnimationStyle(R.style.AnimationPopup);
//        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
//        this.setOutsideTouchable(true);
        this.setFocusable(true);
        
        setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                Log.info(TAG, "onDismiss:: ");
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_UPDATE_RIGHT_TAB);
            }
        });
    }
    
    /**
     * 设置监听
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-2
     */

    private void setListener()
    {
        leftListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem baseItem = searchLeftList.get(position);
                if (baseItem.getType() == BaseListItem.TYPE_CONTACT)
                {
                    ContactModel ContactModel = UiApplication.getContactService().getContactById(baseItem.getId());
                    ServiceUtils.makeCall(mContext, ContactModel.getPhoneNumList().get(0).getNumber());
                }

                // 拨打电话后清空EditText
                leftDialEditText.setText("");
                leftListView.setVisibility(View.GONE);
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);
            }
        });
        rightListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                BaseListItem baseItem = searchRightList.get(position);
                if (baseItem.getType() == BaseListItem.TYPE_CONTACT)
                {
                    ContactModel ContactModel = UiApplication.getContactService().getContactById(baseItem.getId());
                    ServiceUtils.makeCall(mContext, ContactModel.getPhoneNumList().get(0).getNumber());
                }
                // 拨打电话后清空EditText
                rightDialEditText.setText("");
                rightListView.setVisibility(View.GONE);
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);

            }
        });

        // 根据输入框输入值的改变来过滤搜索
        leftDialEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if (!TextUtils.isEmpty(s))
                {
                    leftListView.setVisibility(View.VISIBLE);
                    String number = s.toString();
                    searchData(number, true);
                    ServiceUtils.callNumber(mContext, number);
                }
                else
                {
                    leftListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        rightDialEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if (!TextUtils.isEmpty(s))
                {
                    rightListView.setVisibility(View.VISIBLE);
                    searchData(s.toString(), false);
                }
                else
                {
                    rightListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    /**
     * edittext数据变化后，更新左边listview
     * 方法说明 :
     * @param searchString
     * @author chaimb
     * @Date 2015-6-1
     */
    private void searchData(String searchString, boolean isLeft)
    {
        // TODO 搜索
        List<ContactModel> contactLeftList = new ArrayList<ContactModel>();
        List<ContactModel> contactRightList = new ArrayList<ContactModel>();
        searchLeftList = new ArrayList<BaseListItem>();
        searchRightList = new ArrayList<BaseListItem>();

        if (sc == null)
            sc = new SearchContacts();

        sc.clear();
        sc.setContactAll(UiApplication.getContactService().getContactList());
        if ("".equals(searchString))
        {
            sc.clear();
        }
        else
        {

            if (isLeft)
            {
                contactLeftList = sc.onDialSearch(searchString);
                if (contactLeftList != null && contactLeftList.size() > 0)
                {
                    for (ContactModel contact : contactLeftList)
                    {
                        BaseListItem baseListItem = new BaseListItem();
                        baseListItem.setId(contact.getId());
                        baseListItem.setName(contact.getName());
                        baseListItem.setType(BaseListItem.TYPE_CONTACT);
                        searchLeftList.add(baseListItem);
                    }
                }

                sortLeftAdapter.updateListView(searchLeftList);
            }
            else
            {
                contactRightList = sc.onDialSearch(searchString);
                if (contactRightList != null && contactRightList.size() > 0)
                {
                    for (ContactModel contact : contactRightList)
                    {
                        BaseListItem baseListItem = new BaseListItem();
                        baseListItem.setId(contact.getId());
                        baseListItem.setName(contact.getName());
                        baseListItem.setType(BaseListItem.TYPE_CONTACT);
                        searchRightList.add(baseListItem);
                    }
                }

                sortRightAdapter.updateListView(searchRightList);
            }
        }

    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {

        if (id == UiEventEntry.ACTION_MOUBLE_NUMBER)
        {
            Bundle data = (Bundle) args[0];

            if (leftLayout != null && rightLayout != null)
            {
                leftNumber = data.getString("leftnumber");
                rightNumber = data.getString("rightnumber");
                action = data.getString(ACTION_MODULE_NUMBER_DOWN);

                if (ACTION_LEFTMODULE_NUMBER_DOWN.equals(action))
                {// 左模块拨号
                    leftLayout.setVisibility(View.VISIBLE);
                    editable = leftDialEditText.getText();
                    currentIndex = leftDialEditText.getSelectionStart();
                    editable.insert(currentIndex, leftNumber);
                    leftDialEditText.requestFocus();
                }
                else if (ACTION_RIGHTMODULE_NUMBER_DOWN.equals(action))
                {// 右模块拨号
                    rightLayout.setVisibility(View.VISIBLE);
                    editable = rightDialEditText.getText();
                    currentIndex = rightDialEditText.getSelectionStart();
                    editable.insert(currentIndex, rightNumber);
                    rightDialEditText.requestFocus();
                }
                else
                {

                }

            }
        }
    }

    public void hideRightDial()
    {
        rightLayout.setVisibility(View.GONE);
        leftLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 
     * 方法说明 :显示左拨号盘
     * @author chaimb
     * @Date 2015-9-8
     */
    private void showLeftDial()
    {
        leftLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 方法说明 :显示右拨号盘
     * @author chaimb
     * @Date 2015-9-8
     */
    private void showRightDial()
    {
        rightLayout.setVisibility(View.VISIBLE);
    }

    public void setEditNum(String dialNum)
    {

        editable = leftDialEditText.getText();
        currentIndex = leftDialEditText.getSelectionStart();

        if (TextUtils.isEmpty(dialNum))
        {
            leftDialEditText.setText("");
        }
        else
        {
            editable.insert(currentIndex, dialNum);
        }

    }

}
