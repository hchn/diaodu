package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.ui.adapter.CallRecordPagerAdapter;
import com.jiaxun.uil.ui.adapter.CallRecordSortAdapter;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.InputFilterUtil;
import com.jiaxun.uil.util.SearchContacts;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.UilConstantEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**\
 * 
 * 说明：通话记录整体界面
 *
 * @author  chaimb
 *
 * @Date 2015-5-19
 */
public class CallRecordListFragment extends BaseFragment implements NotificationCenterDelegate, OnClickListener, OnLongClickListener
{
    private static final String TAG = CallRecordListFragment.class.getName();

    private int colorPressed;
    private int colorNormal;

    private ViewPager callRecordViewPager;

    private FragmentManager fragmentManager;

    private TextView allTextView;
    private TextView receivedTextView;
    private TextView noAnswerTextView;
    private TextView dialedTextView;

    // 四个Fragment 全部、未接、已接、已拨
    private List<BaseCallRecordFragment> mFragments;
    private CallRecordAllFragment allFragment;
    private CallRecordReceivedFragment receivedFragment;
    private CallRecordMissedFragment missedFragment;
    private CallRecordDialedFragment dialedFragment;

    private ArrayList<CallRecordListItem> allCallRecordListItems;
    private ArrayList<CallRecordListItem> receivedCallRecordListItems;
    private ArrayList<CallRecordListItem> missedCallRecordListItems;
    private ArrayList<CallRecordListItem> dialedCallRecordListItems;

    // viewpager的布局，当通话搜索框搜索时，隐藏该布局
    private LinearLayout callrecordLinearLayout;
    // 搜索
    private ListView searchListView;

    private CallRecordSortAdapter sortAdapter;

    // 拨号盘
    private RelativeLayout inputNumRelativeLayout;
    private LinearLayout dialedLinearLayout;
    private ImageView showDialImageView;
    private EditText inputNumberEditText;
    private Button clearButton;
    private ImageView delImageView;

    private int callRecordPressed;
    private int callRecordNormal;
    private int callRecordNormalLast;
    private int currentIndex = 0;
    private Editable editable;
    private SearchContacts sc = null;
    private List<BaseListItem> searchList;

    private int missedCount;

    private Button againButton;

    public CallRecordListFragment()
    {
        super();
        Log.info(TAG, "CallRecordListFragment()::onstruct");
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CALL_RECORD_ADD);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CLOSE_DIAL);
        delData();
        initData();
        initFragments();

    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_callrecord_list;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        Log.info(TAG, "onDestroy::");
//        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CALL_RECORD_ADD);
//        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CLOSE_DIAL);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.info(TAG, "onCreateView::");
        missedCount = 0;

        if (getArguments() != null)
        {
            int visiblePosition = getArguments().getInt(UiEventEntry.CALLRECORD_VISIBLEPOSITION);
            int currentType = getArguments().getInt(UiEventEntry.CALLRECORD_TYPE);

            if (visiblePosition != -1)
            {
                if (allFragment != null && currentType == 0)
                {
                    allFragment.changePosition(visiblePosition);
                }
                if (receivedFragment != null && currentType == 1)
                {
                    receivedFragment.changePosition(visiblePosition);
                }
                if (missedFragment != null && currentType == 2)
                {
                    missedFragment.changePosition(visiblePosition);
                }
                if (dialedFragment != null && currentType == 3)
                {
                    dialedFragment.changePosition(visiblePosition);
                }
            }
            else
            {
                Log.info(TAG, "visiblePosition::" + visiblePosition);
            }
        }
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_UPDATE_RIGHT_TAB, UiEventEntry.TAB_CALL_RECORD);

        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CALL_RECORD_MISSED_COUNT, missedCount);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");

        fragmentManager = getFragmentManager();

        colorPressed = getActivity().getResources().getColor(R.color.white);
        colorNormal = getActivity().getResources().getColor(R.color.call_record_normal_color);

        callRecordPressed = R.drawable.call_record_pressed;
        callRecordNormal = R.drawable.call_record_normal;
        callRecordNormalLast = R.drawable.call_record_normal_last;

        allTextView = (TextView) view.findViewById(R.id.all_textview);
        receivedTextView = (TextView) view.findViewById(R.id.received_textview);
        noAnswerTextView = (TextView) view.findViewById(R.id.no_textview);
        dialedTextView = (TextView) view.findViewById(R.id.dialed_textview);

        callRecordViewPager = (ViewPager) view.findViewById(R.id.call_record_viewpager);

        callrecordLinearLayout = (LinearLayout) view.findViewById(R.id.callrecord_linearlayout);
        searchListView = (ListView) view.findViewById(R.id.search_listview);

        // 拨号盘业务
        inputNumRelativeLayout = (RelativeLayout) view.findViewById(R.id.input_number_layout);
        dialedLinearLayout = (LinearLayout) view.findViewById(R.id.dialed_linearlayout);
        showDialImageView = (ImageView) view.findViewById(R.id.show_callrecord_dial_imageview);
        inputNumberEditText = (EditText) view.findViewById(R.id.input_number_edittext);
        delImageView = (ImageView) view.findViewById(R.id.delnum_imageview);
        clearButton = (Button) view.findViewById(R.id.cleardial_button);
        againButton = (Button) view.findViewById(R.id.again_start_call);

        // 得到焦点
        inputNumberEditText.requestFocus();

        // 禁止弹出软键盘
        UiUtils.hideSoftInputMethod(getActivity(), inputNumberEditText);

        // 号码字符限制
        inputNumberEditText.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(UilConstantEntry.NUMBER_SIGN_LIMIT_REGEX) });

        sortAdapter = new CallRecordSortAdapter(getActivity(), searchListView, new ArrayList<BaseListItem>());
        searchListView.setAdapter(sortAdapter);

        setListener(view);

        // 默认
        allTextView.setTextColor(colorPressed);
        receivedTextView.setTextColor(colorNormal);
        noAnswerTextView.setTextColor(colorNormal);
        dialedTextView.setTextColor(callRecordNormalLast);

        // 滑动listview隐藏拨号盘
        allFragment.setDialedLinearLayout(dialedLinearLayout);
        allFragment.setShowDialImageView(showDialImageView);

        receivedFragment.setDialedLinearLayout(dialedLinearLayout);
        receivedFragment.setShowDialImageView(showDialImageView);

        missedFragment.setDialedLinearLayout(dialedLinearLayout);
        missedFragment.setShowDialImageView(showDialImageView);

        dialedFragment.setDialedLinearLayout(dialedLinearLayout);
        dialedFragment.setShowDialImageView(showDialImageView);

        CallRecordPagerAdapter adapter = new CallRecordPagerAdapter(fragmentManager, mFragments);
        callRecordViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        callRecordViewPager.setAdapter(adapter);

    }

    /**
     * 切换字体删除之前配置
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-1
     */
    private void delData()
    {
        Log.info(TAG, "delData::");
//        UiApplication.getCallRecordService().removeAll();
    }

    /**
     * 
     * 
     * 方法说明 :手动设置联系人数据
     * @author chaimb
     * @Date 2015-5-18
     */
    private void initData()
    {
        // list 初始化
        Log.info(TAG, "initData");
        allCallRecordListItems = new ArrayList<CallRecordListItem>();
        ArrayList<CallRecordListItem> tempAllCall = new ArrayList<CallRecordListItem>();
        receivedCallRecordListItems = new ArrayList<CallRecordListItem>();
        missedCallRecordListItems = new ArrayList<CallRecordListItem>();
        dialedCallRecordListItems = new ArrayList<CallRecordListItem>();
        for (CallRecord callRecord : UiApplication.getCallRecordService().getAllCallRecords())
        {
            allCallRecordListItems.add(0, ServiceUtils.setCallRecordList(callRecord));
            tempAllCall.add(0, ServiceUtils.setCallRecordList(callRecord));
        }

        allCallRecordListItems = sortByTime(allCallRecordListItems);
        for (CallRecordListItem allCallRecordListItem : allCallRecordListItems)
        {
            CallRecord callRecord = allCallRecordListItem.getCallRecord();
            long durtion = 0;
            if (callRecord.getConnectTime() == 0)
            {
                durtion = 0;
            }
            else
            {
                durtion = callRecord.getReleaseTime() - callRecord.getConnectTime();
            }

            if (!(callRecord.isOutGoing()) && durtion > 0)
            {
                receivedCallRecordListItems.add(allCallRecordListItem);
            }

            if (!(callRecord.isOutGoing()) && durtion == 0)
            {
                missedCallRecordListItems.add(allCallRecordListItem);
            }

            if (callRecord.isOutGoing())
            {
                dialedCallRecordListItems.add(allCallRecordListItem);
            }

        }
        allCallRecordListItems = ServiceUtils.getCallRecordListItems(tempAllCall);
        receivedCallRecordListItems = ServiceUtils.getCallRecordListItems(receivedCallRecordListItems);
        missedCallRecordListItems = ServiceUtils.getCallRecordListItems(missedCallRecordListItems);
        dialedCallRecordListItems = ServiceUtils.getCallRecordListItems(dialedCallRecordListItems);

    }

    /**
     * 通话时间对通话记录排序
     * 方法说明 :
     * @param list
     * @return
     * @author chaimb
     * @Date 2015-8-18
     */
    private ArrayList<CallRecordListItem> sortByTime(ArrayList<CallRecordListItem> list)
    {
        if (list != null && list.size() > 0)
        {// 通过冒泡排序list

            for (int i = 0; i < list.size() - 1; i++)
            {

                for (int j = i + 1; j < list.size(); j++)
                {
                    if (list.get(i).getCallRecord().getStartTime() < list.get(j).getCallRecord().getStartTime())
                    {
                        CallRecordListItem temp = null;
                        temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }
        return list;

    }

    /**
     * 
     * 方法说明 :设置viewpager（全部、已接、未接、已拨）
     * @author chaimb
     * @Date 2015-5-6
     */
    private void initFragments()
    {

        mFragments = new ArrayList<BaseCallRecordFragment>();

        // 全部
        allFragment = new CallRecordAllFragment();
        allFragment.setCallRecordListItems(allCallRecordListItems);
        allFragment.setType(0);

        // 已接
        receivedFragment = new CallRecordReceivedFragment();
        receivedFragment.setCallRecordListItems(receivedCallRecordListItems);
        receivedFragment.setType(1);
        // 未接
        missedFragment = new CallRecordMissedFragment();
        missedFragment.setCallRecordListItems(missedCallRecordListItems);
        missedFragment.setType(2);

        // 已拨
        dialedFragment = new CallRecordDialedFragment();
        dialedFragment.setCallRecordListItems(dialedCallRecordListItems);
        dialedFragment.setType(3);

        mFragments.add(allFragment);
        mFragments.add(receivedFragment);
        mFragments.add(missedFragment);
        mFragments.add(dialedFragment);

        Log.info(TAG, "initFragments::mFragments" + mFragments.size());

    }

    /**
     * 
     * 方法说明 :设置控件监听
     * @author chaimb
     * @Date 2015-5-6
     */
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
        againButton.setOnClickListener(this);
        view.findViewById(R.id.start_call).setOnClickListener(this);

        clearButton.setOnClickListener(this);
        clearButton.setOnLongClickListener(this);
        delImageView.setOnClickListener(this);
        delImageView.setOnLongClickListener(this);
        showDialImageView.setOnClickListener(this);

        allTextView.setOnClickListener(new MyOnclickListener(0));
        receivedTextView.setOnClickListener(new MyOnclickListener(1));
        noAnswerTextView.setOnClickListener(new MyOnclickListener(2));
        dialedTextView.setOnClickListener(new MyOnclickListener(3));

        // 根据输入框输入值的改变来过滤搜索
        inputNumberEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if (!TextUtils.isEmpty(s))
                {
                    searchListView.setVisibility(View.VISIBLE);
                    inputNumRelativeLayout.setVisibility(View.VISIBLE);
                    callrecordLinearLayout.setVisibility(View.GONE);
                    searchData(s.toString());
                    ServiceUtils.callNumber(parentActivity, s.toString());
                }
                else
                {
                    inputNumRelativeLayout.setVisibility(View.GONE);
                    searchListView.setVisibility(View.GONE);
                    callrecordLinearLayout.setVisibility(View.VISIBLE);
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

        searchListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (searchList.get(position).getType() == BaseListItem.TYPE_CONTACT)
                {
                    int contactId = searchList.get(position).getId();
                    ContactModel ContactModel = UiApplication.getContactService().getContactById(contactId);
                    ServiceUtils.makeCall(parentActivity, ContactModel.getPhoneNumList().get(0).getNumber());
                }
                if (searchList.get(position) instanceof CallRecordListItem)
                {
                    CallRecordListItem callRecordListItem = (CallRecordListItem) searchList.get(position);
                    ServiceUtils.makeCall(parentActivity, callRecordListItem.getCallRecord().getPeerNum());
                }

                // 拨打电话后清空EditText
                inputNumberEditText.setText("");
                inputNumRelativeLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.GONE);
                callrecordLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * edittext数据变化后，更新listview
     * 方法说明 :
     * @param searchString
     * @author chaimb
     * @Date 2015-6-1
     */
    private void searchData(String searchString)
    {
        List<CallRecordListItem> callRecordList = new ArrayList<CallRecordListItem>();
        List<ContactModel> contactList = new ArrayList<ContactModel>();
        searchList = new ArrayList<BaseListItem>();

        if (sc == null)
            sc = new SearchContacts();

        sc.clear();
        sc.setContactAll(UiApplication.getContactService().getContactList());

        sc.setRecordAll(allCallRecordListItems);
        if ("".equals(searchString))
        {
            sc.clear();
        }
        else
        {

            callRecordList = sc.onRecordSearch(searchString);
            contactList = sc.onDialSearch(searchString);
            if (contactList != null && contactList.size() > 0)
            {
                for (ContactModel contact : contactList)
                {
                    BaseListItem baseItem = new BaseListItem();
                    baseItem.setId(contact.getId());
                    baseItem.setType(BaseListItem.TYPE_CONTACT);
                    baseItem.setName(contact.getName());
                    searchList.add(baseItem);
                }
            }
            if (callRecordList != null && callRecordList.size() > 0)
            {

                for (BaseListItem baseListItem : callRecordList)
                {
                    searchList.add(baseListItem);
                }
            }
            sortAdapter.updateListView(searchList);
        }

    }

    /**
     * 
     * 说明：实现选项卡的监听事件，并切换fragment
     *
     * @author  chaimb
     *
     * @Date 2015-5-8
     */
    public class MyOnclickListener implements OnClickListener
    {
        private int currentType = 0;

        MyOnclickListener(int i)
        {
            currentType = i;
        }

        @Override
        public void onClick(View v)
        {
            callRecordViewPager.setVisibility(View.VISIBLE);
            callRecordViewPager.setCurrentItem(currentType);

        }
    }

    /**
     * 
     * 说明：滑动切换时变换选项卡
     *
     * @author  chaimb
     *
     * @Date 2015-5-8
     */

    public class MyOnPageChangeListener implements OnPageChangeListener
    {

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
            dialedLinearLayout.setVisibility(View.GONE);
            showDialImageView.setImageResource(R.drawable.open_dialed_imageview);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }

        @Override
        public void onPageSelected(int position)
        {

            switch (position)
            {
                case 0:
                    allTextView.setTextColor(colorPressed);
                    receivedTextView.setTextColor(colorNormal);
                    noAnswerTextView.setTextColor(colorNormal);
                    dialedTextView.setTextColor(colorNormal);

                    allTextView.setBackgroundResource(callRecordPressed);
                    receivedTextView.setBackgroundResource(callRecordNormal);
                    noAnswerTextView.setBackgroundResource(callRecordNormal);
                    dialedTextView.setBackgroundResource(callRecordNormalLast);

                    break;
                case 1:
                    receivedTextView.setTextColor(colorPressed);
                    allTextView.setTextColor(colorNormal);
                    noAnswerTextView.setTextColor(colorNormal);
                    dialedTextView.setTextColor(colorNormal);

                    receivedTextView.setBackgroundResource(callRecordPressed);
                    allTextView.setBackgroundResource(callRecordNormal);
                    noAnswerTextView.setBackgroundResource(callRecordNormal);
                    dialedTextView.setBackgroundResource(callRecordNormalLast);

                    break;
                case 2:
                    noAnswerTextView.setTextColor(colorPressed);
                    allTextView.setTextColor(colorNormal);
                    receivedTextView.setTextColor(colorNormal);
                    dialedTextView.setTextColor(colorNormal);

                    noAnswerTextView.setBackgroundResource(callRecordPressed);
                    allTextView.setBackgroundResource(callRecordNormal);
                    receivedTextView.setBackgroundResource(callRecordNormal);
                    dialedTextView.setBackgroundResource(callRecordNormalLast);
                    break;
                case 3:
                    dialedTextView.setTextColor(colorPressed);
                    allTextView.setTextColor(colorNormal);
                    receivedTextView.setTextColor(colorNormal);
                    noAnswerTextView.setTextColor(colorNormal);

                    dialedTextView.setBackgroundResource(callRecordPressed);
                    allTextView.setBackgroundResource(callRecordNormal);
                    receivedTextView.setBackgroundResource(callRecordNormal);
                    noAnswerTextView.setBackgroundResource(callRecordNormal);
                    break;

                default:
                    break;
            }
        }

    }

    public void changeData(CallRecord callRecord)
    {
        // 刷新fragment的UI

        if (allFragment != null/* && all.isAdded() */)
        {
            CallRecordListItem allRecordListItem = ServiceUtils.setCallRecordList(callRecord);
            setCallRecordListItem(allRecordListItem, allCallRecordListItems);
            allFragment.changeData(allCallRecordListItems);
            Log.info(TAG, "run::allCallRecordListItems.size():" + allCallRecordListItems.size() + "allCallRecordListItems.get(0).getCount()::"
                    + allCallRecordListItems.get(0).getCount());
        }
        if (dialedFragment != null && callRecord.isOutGoing())
        {
            CallRecordListItem dialedRecordListItem = ServiceUtils.setCallRecordList(callRecord);
            setCallRecordListItem(dialedRecordListItem, dialedCallRecordListItems);
            Log.info(TAG, "run::dialedCallRecordListItems.size():" + dialedCallRecordListItems.size() + "dialedCallRecordListItems.get(0).getCount()::"
                    + dialedCallRecordListItems.get(0).getCount());
            dialedFragment.changeData(dialedCallRecordListItems);
        }
        long durtion = 0;
        if (callRecord.getConnectTime() == 0)
        {
            durtion = 0;
        }
        else
        {
            durtion = callRecord.getReleaseTime() - callRecord.getConnectTime();
        }
        if (receivedFragment != null && !(callRecord.isOutGoing()) && durtion > 0)
        {
            CallRecordListItem receRecordListItem = ServiceUtils.setCallRecordList(callRecord);
            ;
            setCallRecordListItem(receRecordListItem, receivedCallRecordListItems);
            Log.info(TAG, "run::receivedCallRecordListItems.size():" + receivedCallRecordListItems.size() + "receivedCallRecordListItems.get(0).getCount()::"
                    + receivedCallRecordListItems.get(0).getCount());
            receivedFragment.changeData(receivedCallRecordListItems);
        }
        if (missedFragment != null && !(callRecord.isOutGoing()) && durtion == 0)
        {
            CallRecordListItem missedRecordListItem = ServiceUtils.setCallRecordList(callRecord);
            setCallRecordListItem(missedRecordListItem, missedCallRecordListItems);
            Log.info(TAG, "run::missedCallRecordListItems.size():" + missedCallRecordListItems.size() + "missedCallRecordListItems.get(0).getCount()::"
                    + missedCallRecordListItems.get(0).getCount());
            missedFragment.changeData(missedCallRecordListItems);
        }
    }

    private void setCallRecordListItem(CallRecordListItem callRecordListItem, List<CallRecordListItem> callRecordListItems)
    {
        int callType = callRecordListItem.getCallRecord().getCallType();
        CallRecord callRecord = callRecordListItem.getCallRecord();
        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {
            if (!(callRecord.isChairman()))
            {// 不是主席
            }
            else
            {// 是会议加入记录内存列表
                callRecordListItems.add(0, callRecordListItem);
            }
        }
        else
        {
            String peerNum = callRecord.getPeerNum();

            if (callRecordListItems != null && callRecordListItems.size() > 0)
            {
                if (peerNum.equals(callRecordListItems.get(0).getCallRecord().getPeerNum()))
                {// 与上一个号码相同
                    int count = callRecordListItems.get(0).getCount();
                    count++;
                    callRecordListItem.setCount(count);
                    callRecordListItems.set(0, callRecordListItem);
                }
                else
                {// 与上一个号码不相同
                    callRecordListItems.add(0, callRecordListItem);
                }
            }
            else
            {
                callRecordListItems.add(0, callRecordListItem);
            }

        }
    }

    @Override
    public void release()
    {
        Log.info(TAG, "release()::");
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CALL_RECORD_ADD);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CLOSE_DIAL);
        super.release();
    }

    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.CALL_RECORD_ADD)
        {
            CallRecord callRecord = (CallRecord) args[0];

            changeData(callRecord);

            Log.info(TAG, "isVisible()==>>" + isVisible());
            if (isVisible())
            {
                Log.info(TAG, "no add motify::missedCount==" + missedCount);
                missedCount = 0;

            }
            else
            {
                long durtion = 0;
                if (callRecord.getConnectTime() == 0)
                {
                    durtion = 0;
                }
                else
                {
                    durtion = callRecord.getReleaseTime() - callRecord.getConnectTime();
                }
                if (durtion == 0 && !(callRecord.isOutGoing()))
                {// 未接
                    String release = SdkUtil.parseReleaseReason(callRecord.getReleaseReason());
                    if (!("被叫释放".equals(release)))
                    {
                        Log.info(TAG, "add missed call::missedCount==" + missedCount);
                        missedCount++;
                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CALL_RECORD_MISSED_COUNT, missedCount);
                    }
                    else
                    {
                        Log.info(TAG, "call end peer release");
                    }
                }

            }
        }
        else if (id == UiEventEntry.CLOSE_DIAL)
        {
            if (isVisible())
            {
                inputNumberEditText.setText("");
                inputNumRelativeLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.GONE);
                callrecordLinearLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.show_callrecord_dial_imageview:
                if (dialedLinearLayout.getVisibility() == View.GONE)
                {
                    showDialImageView.setImageResource(R.drawable.close_dialed_imageview);
                    dialedLinearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    showDialImageView.setImageResource(R.drawable.open_dialed_imageview);
                    dialedLinearLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.id0:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();

                editable.insert(currentIndex, "0");
                break;
            case R.id.id1:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();

                editable.insert(currentIndex, "1");
                break;
            case R.id.id2:

                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();
                editable.insert(currentIndex, "2");
                break;
            case R.id.id3:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();

                editable.insert(currentIndex, "3");

                break;
            case R.id.id4:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();
                editable.insert(currentIndex, "4");

                break;
            case R.id.id5:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();

                editable.insert(currentIndex, "5");
                break;
            case R.id.id6:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();
                editable.insert(currentIndex, "6");

                break;
            case R.id.id7:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();
                editable.insert(currentIndex, "7");
                break;
            case R.id.id8:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();

                editable.insert(currentIndex, "8");
                break;
            case R.id.id9:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();

                editable.insert(currentIndex, "9");
                break;
            case R.id.id_x:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();
                editable.insert(currentIndex, "*");

                break;
            case R.id.id_j:
                editable = inputNumberEditText.getText();
                currentIndex = inputNumberEditText.getSelectionStart();
                editable.insert(currentIndex, "#");
                break;
            case R.id.start_call:
                // 左手柄拨号
                String callNum = inputNumberEditText.getText().toString();
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

                            ServiceUtils.makeCall(parentActivity, callNum);
                        }
                    }
                    else
                    {

                        ServiceUtils.makeCall(parentActivity, callNum);
                    }

                }
                else
                {
                    Log.info(TAG, "input num is null");
                }
                inputNumberEditText.setText("");
                inputNumRelativeLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.GONE);
                callrecordLinearLayout.setVisibility(View.VISIBLE);
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
                            ServiceUtils.makeConf(parentActivity, callRecord.getConfName(), numberList);
                            break;
                        }
                    }
                    else
                    {// 普通拨号
                        if (callRecord.isOutGoing())
                        {
                            ServiceUtils.makeCall(parentActivity, callRecord.getPeerNum());
                            break;
                        }
                    }
                }

                break;
            case R.id.cleardial_button:
            case R.id.delnum_imageview:
                editable = inputNumberEditText.getText();

                currentIndex = inputNumberEditText.getSelectionStart();
                if (currentIndex > 0)
                {
                    editable.delete(currentIndex - 1, currentIndex);
                }
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onLongClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cleardial_button:
            case R.id.delnum_imageview:
                inputNumberEditText.setText("");
                break;

            default:
                break;
        }
        return false;
    }

//    /**
//     * 方法说明 :返回所有单呼通话记录
//     * @return
//     * @author chaimb
//     * @Date 2015-7-15
//     */
//    private ArrayList<CallRecordListItem> getSCallRecordList()
//    {
//        ArrayList<CallRecordListItem> sCallRecordListItems = new ArrayList<CallRecordListItem>();
//        for (CallRecordListItem callRecordListItem : callRecordListItems)
//        {
//            int callType = callRecordListItem.getCallRecord().getCallType();
//            if (callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
//            {
//                sCallRecordListItems.add(callRecordListItem);
//            }
//        }
//        return sCallRecordListItems;
//    }

}
