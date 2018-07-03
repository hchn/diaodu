package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactBaseAdapter;
import com.jiaxun.uil.ui.view.GroupLevelBtn;
import com.jiaxun.uil.ui.view.GroupLevelBtn.OnGroupBtnClickListener;
import com.jiaxun.uil.util.UiUtils;

/**
 * 说明：ContactHotKeyFragment ContactGridFragment
 *
 * @author  HeZhen
 *
 * @Date 2015-7-6
 */
public class BaseContactGridFragment extends BaseFragment implements View.OnClickListener
{
    int mGroupId = GroupModel.DEFAULT_PARENT_ID;
//    GroupModel groupModel;
    LinearLayout groupTitle;
    GridView groupGrid;
    EditText searchEditT;
    ImageView switchBtn;
    ImageView editClearBtn;
    ContactBaseAdapter contactAdapter;
    HorizontalScrollView hsview;
    /**
     * 进入组深度
     */
    ArrayList<Integer> groupLevelList;

    ArrayList<BaseListItem> dataAdapterList;

    public BaseContactGridFragment()
    {
        dataAdapterList = new ArrayList<BaseListItem>();
        groupLevelList = new ArrayList<Integer>();
    }

    @Override
    public int getLayoutView()
    {
        return 0;
    }

    @Override
    public void initComponentViews(View view)
    {
        groupTitle = (LinearLayout) view.findViewById(R.id.groups_level_title);
        groupGrid = (GridView) view.findViewById(R.id.group_grid);
        searchEditT = (EditText) view.findViewById(R.id.et_search);
        switchBtn = (ImageView) view.findViewById(R.id.iv_switch);
        editClearBtn = (ImageView) view.findViewById(R.id.iv_edit_clear);
        hsview = (HorizontalScrollView) view.findViewById(R.id.hscroll_grouplevel);
        
        contactAdapter = new ContactBaseAdapter(getActivity());
        groupGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        groupGrid.setAdapter(contactAdapter);

        LinearLayout.LayoutParams conRightLayoutParams = (LayoutParams) searchEditT.getLayoutParams();
        conRightLayoutParams.width = UiUtils.homeLeftContainerW / 4;
        conRightLayoutParams.height = LayoutParams.WRAP_CONTENT;
        searchEditT.setLayoutParams(conRightLayoutParams);
        
    }

    public void initData(int groupId, boolean removeLevelTile, Context context)
    {
//        groupModel = UiApplication.getContactService().getDepById(groupId);
        contactAdapter.initData(groupId, dataAdapterList);
        if (removeLevelTile)
        {
            groupLevelList.clear();
        }
        if (!groupLevelList.contains(groupId))
        {
            groupLevelList.add(groupId);
        }
        groupTitle.removeAllViews();
        for (Integer gId : groupLevelList)
        {
            GroupModel groupModel = UiApplication.getContactService().getDepById(gId);
            GroupLevelBtn groupLevelBtn = new GroupLevelBtn(context, groupModel.getParentId() == GroupModel.DEFAULT_PARENT_ID);
            groupTitle.addView(groupLevelBtn.initData(gId), groupLevelBtn.getLayoutParams());
            groupLevelBtn.setOnClickListener(new OnGroupBtnClickListener()
            {
                @Override
                public void onClick(View v, int groupId)
                {
                    turnToPreGroup(groupId, false);
                }
            });
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    hsview.scrollTo(groupTitle.getChildAt(groupTitle.getChildCount()-1).getRight(), 0);
                }
            }, 100l);
        }
    }

    public void initData(int groupId, boolean removeLevelTile)
    {
        initData(groupId, removeLevelTile, parentActivity);
    }

    public void initTopGroupTabs(int groupId)
    {
        groupLevelList.clear();
        groupTitle.removeAllViews();
        dataAdapterList.clear();
        initData(groupId, false);
    }

    /**
     * 方法说明 :跳转到上一组
     * @param GroupModel
     * @author HeZhen
     * @Date 2015-5-13
     */
    public void turnToPreGroup(int groupIdParam, boolean removeLevelTile)
    {
        boolean start = false;
        int startPos = 0;
        ArrayList<Integer> grList = new ArrayList<Integer>();
        for (Integer groupId : groupLevelList)
        {
            if (groupId == groupIdParam)
            {
                start = true;
            }
            if (start)
            {
                grList.add(groupId);
            }
            else
            {
                startPos++;
            }

        }
        groupLevelList.removeAll(grList);
        groupTitle.removeViews(startPos, groupTitle.getChildCount() - startPos);
        initData(groupIdParam, removeLevelTile);
    }

    @Override
    public void onClick(View v)
    {

    }

}
