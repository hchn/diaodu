package com.jiaxun.uil.ui.widget.sticky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.uil.R;

/**
 * 说明：自定义 Gridview 实现 通讯录 根据标题首字母排序 网格显示
 *
 * @author  HeZhen
 *
 * @Date 2015-4-17
 */
public class StickyGridHeadersSimpleArrayAdapter extends BaseAdapter implements SectionIndexer, StickyGridHeadersSimpleAdapter
{
    protected static final String TAG = StickyGridHeadersSimpleArrayAdapter.class.getSimpleName();

    private int mHeaderResId;

    private LayoutInflater mInflater;

    private int mItemResId;

    private List<ContactModel> mItems = new ArrayList<ContactModel>();

    private List<ContactModel> selectList;

    private List<Character> charList;

    private Map<Integer, HeaderContentCount> charMap;

    private int gridClumns = 1;

    private int selectPosition;

    private EditOptions editOptions;

    public void setSelectPosition(int selectPosition)
    {
        this.selectPosition = selectPosition;
    }

    public StickyGridHeadersSimpleArrayAdapter(Context context, int headerResId, int itemResId)
    {
        init(context, headerResId, itemResId);
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public int getCount()
    {
        int size = mItems.size();
        return size;
    }

    @Override
    public long getHeaderId(int position)
    {
        ContactModel contact = getItem(position);
        if (contact == null)
        {
            return 0l;
        }
        Character value = contact.getSortLetters().charAt(0);
        if (!charList.contains(value))
        {
            charList.add(value);
        }
        return value;
    }

    public List<Character> getCharList()
    {
        if (charList == null)
        {
            charList = new ArrayList<Character>();
        }
        return charList;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        HeaderViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        else
        {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        ContactModel contact = getItem(position);
        if (contact != null)
        {
            CharSequence string = contact.getSortLetters();

            holder.textView.setText(string.subSequence(0, 1));
        }

        return convertView;
    }

    @Override
    public ContactModel getItem(int position)
    {
        return mItems == null ? null : mItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.checkButton = (ImageView) convertView.findViewById(R.id.check_btn);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
//        boolean isContactEdit = UiApplication.getInstance().isContactEdit;

        ContactModel contact = getItem(position);
        if (contact == null)
        {
            return convertView;
        }
        holder.textView.setText(contact.getName());
        boolean isContactEdit = false;
        if (isContactEdit)
        {
            holder.checkButton.setVisibility(View.VISIBLE);
            boolean isSelected = holder.checkButton.isSelected();
            holder.checkButton.setOnClickListener(onClickListener);
            if (isSelected)
            {
                if (!selectList.contains(contact))
                {
                    selectList.add(contact);
                }
                holder.checkButton.setImageResource(R.drawable.btn_check_on_holo_light);
            }
            else
            {
                if (selectList.contains(contact))
                {
                    selectList.remove(contact);
                }
                holder.checkButton.setImageResource(R.drawable.btn_check_off_holo_light);
            }
        }
        else
        {
            holder.checkButton.setVisibility(View.GONE);
        }

        if (selectPosition == position)
        {
//            convertView.setBackgroundColor(Color.GRAY);
        }
        else
        {
//            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }

    private void init(Context context, int headerResId, int itemResId)
    {
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        mInflater = LayoutInflater.from(context);
        charList = new ArrayList<Character>();
        charMap = new HashMap<Integer, HeaderContentCount>();
        selectList = new ArrayList<ContactModel>();
    }

    /**重置数据引用*/
    public void initData(List<ContactModel> dataSrc)
    {
        if (dataSrc != null)
        {
            mItems = dataSrc;
        }
        charMap.clear();
        for (int i = 0; i < mItems.size(); i++)
        {
            ContactModel ContactModel = mItems.get(i);
            String sortStr = ContactModel == null ? "#" : ContactModel.getSortLetters();
            int firstChar = sortStr.toUpperCase().charAt(0);
            if (charMap.containsKey(firstChar))
            {
                int count = charMap.get(firstChar).count;
                count++;
                charMap.get(firstChar).count = count;
            }
            else
            {
                HeaderContentCount hc = new HeaderContentCount();
                hc.count++;
                charMap.put(firstChar, hc);
            }
        }
        notifyDataSetChanged();
    }

    protected class HeaderContentCount
    {
        public int count;
    }

    protected class HeaderViewHolder
    {
        public TextView textView;
    }

    protected class ViewHolder
    {
        public TextView textView;
        public ImageView checkButton;
    }

    @Override
    public Object[] getSections()
    {
        return null;
    }

    @Override
    public int getPositionForSection(int section)
    {
        if (charList.contains((char) section))
        {
            int count = 0;
            for (int i = 0; i < charList.size(); i++)
            {
                int firstChar = charList.get(i);
                if (firstChar == section)
                {
                    return count;
                }
                int c = charMap.get(firstChar).count;
                int hang = 1 + (c / gridClumns) + ((c % gridClumns) > 0 ? 1 : 0);
                count += hang * gridClumns;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position)
    {
        if (mItems != null)
        {
            ContactModel contact = mItems.get(position);
            return contact.getSortLetters().charAt(0);
        }
        return 0;
    }

    public void setGridClumns(int gridClumns)
    {
        this.gridClumns = gridClumns;
    }

    public void setEditOptions(EditOptions editOptions)
    {
        this.editOptions = editOptions;
    }

    public interface EditOptions
    {
        public void deleteContact(int contactId);
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            v.setSelected(!v.isSelected());
            notifyDataSetChanged();
        }
    };

}
