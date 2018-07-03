package com.jiaxun.setting.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.setting.model.PrefItemType;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsSeekBarItem;
import com.jiaxun.setting.model.PrefsSelectFileItem;
import com.jiaxun.uil.R;

/**
 * ����������
 * 
 * @author zhangxd
 * 
 * @Date 2015-5-22
 */
public class SettingAdapter extends BaseAdapter
{
    private static String TAG = SettingAdapter.class.getName();
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private List<PrefsBaseItem> mData = null;
    private ConfigHelper configHelper = null;

    public SettingAdapter(Context context, List<PrefsBaseItem> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        configHelper = ConfigHelper.getDefaultConfigHelper(getContext());
    }

    public Context getContext()
    {
        return mContext;
    }

    public void addItem(PrefsBaseItem newItem)
    {
        this.mData.add(newItem);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return mData.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount()
    {
        return PrefItemType.ITEM_TYPE_MAX_COUNT;
    }

    @Override
    public int getCount()
    {
        if (mData == null)
        {
            return 0;
        }
        return this.mData.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mData.get(i);
    }

    @Override
    public boolean isEnabled(int position)
    {
        if ((this.getItemViewType(position) == PrefItemType.GROUP) || (mData.get(position).isEnabled() == false))
        {
            return false;
        }
        return true;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        // int itemType = this.getItemViewType(position);
        PrefsBaseItem item = mData.get(position);

        switch (item.getItemType())
        {
            case PrefItemType.GROUP:
                convertView = groupItem(convertView, item);
                break;
            case PrefItemType.LEFT:
                convertView = leftItem(convertView, item);
                break;

            case PrefItemType.SERVER_ADDRESS:
                convertView = serverAddressItem(convertView, item);
                break;

            case PrefItemType.TEXT:
            case PrefItemType.SELECT_FILE:
                convertView = textItem(convertView, item);
                break;

            case PrefItemType.PASSWORD:
                convertView = passwordItem(convertView, item);
                break;

            case PrefItemType.SEEKBAR:
                convertView = seekBarItem(convertView, item);
                break;

            case PrefItemType.BOOLEAN:
                convertView = booleanItem(convertView, item);
                break;

            case PrefItemType.RADIO_GROUP:
                convertView = radioGroupItem(convertView, item);
                break;

            case PrefItemType.CONTACT:
                convertView = contactItem(convertView, item);
                break;

            default:
                break;
        }
        return convertView;
    }

    private class ViewHolderGroup
    {
        // public static final int ITEM_VIEW_TYPE = 0;
        public TextView textViewGroup = null;

    }

    public class ViewHolderText
    {
        // public static final int ITEM_VIEW_TYPE = 1;
        public TextView textViewName = null;
        public TextView textViewValue = null;
        public ImageView imageViewHead = null;
    }

    private class ViewHolderBoolean
    {
        // public static final int ITEM_VIEW_TYPE = 2;
        public TextView textViewName = null;
        public ImageView imageViewHead = null;
        public ToggleButton toggleButtonBoolean = null;
    }

    private View groupItem(View convertView, PrefsBaseItem item)
    {
        View group_view = null;
        ViewHolderGroup viewHolderGroup = null;
        if (convertView == null)
        {
            viewHolderGroup = new ViewHolderGroup();
            group_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_group, null, false);
            viewHolderGroup.textViewGroup = (TextView) group_view.findViewById(R.id.textViewGroup);
            group_view.setTag(viewHolderGroup);
            convertView = group_view;
        }
        else
        {
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }
        viewHolderGroup.textViewGroup.setText(item.getName());
        return convertView;
    }

    private View leftItem(View convertView, PrefsBaseItem item)
    {
        View left_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            left_view = this.mInflater.inflate(R.layout.adapter_setting_list_left, null, false);
            viewHolderTextItem.textViewName = (TextView) left_view.findViewById(R.id.textViewName);
            viewHolderTextItem.imageViewHead = (ImageView) left_view.findViewById(R.id.imageViewHead);
            left_view.setTag(viewHolderTextItem);
            convertView = left_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());

        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View serverAddressItem(View convertView, PrefsBaseItem item)
    {
        View ip_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            ip_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_text, null, false);
            viewHolderTextItem.textViewName = (TextView) ip_view.findViewById(R.id.textViewName);
            viewHolderTextItem.textViewValue = (TextView) ip_view.findViewById(R.id.textViewValue);

            viewHolderTextItem.imageViewHead = (ImageView) ip_view.findViewById(R.id.imageViewHead);
            ip_view.setTag(viewHolderTextItem);
            convertView = ip_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());

        if (configHelper.getString(item.getKey(), "").equals(""))
        {
            viewHolderTextItem.textViewValue.setVisibility(View.GONE);
        }
        else
        {
            viewHolderTextItem.textViewValue.setVisibility(View.VISIBLE);
            viewHolderTextItem.textViewValue.setText(configHelper.getString(item.getKey(), ""));
        }

        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View textItem(View convertView, PrefsBaseItem item)
    {
        View text_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            text_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_text, null, false);
            viewHolderTextItem.textViewName = (TextView) text_view.findViewById(R.id.textViewName);
            viewHolderTextItem.textViewValue = (TextView) text_view.findViewById(R.id.textViewValue);
            viewHolderTextItem.imageViewHead = (ImageView) text_view.findViewById(R.id.imageViewHead);
            text_view.setTag(viewHolderTextItem);
            convertView = text_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());

        String value = configHelper.getString(item.getKey(), "");

        if (TextUtils.isEmpty(value))
        {
            String defaultalues = "";
            if (item instanceof PrefsSelectFileItem)
            {
                defaultalues = ((PrefsSelectFileItem) item).getDefaultValue();
            }
            else
            {
                defaultalues = "";
            }
            if (TextUtils.isEmpty(defaultalues))
            {
                viewHolderTextItem.textViewValue.setVisibility(View.GONE);
            }
            else
            {
                viewHolderTextItem.textViewValue.setVisibility(View.VISIBLE);
                viewHolderTextItem.textViewValue.setText(defaultalues);
            }
        }
        else
        {
            viewHolderTextItem.textViewValue.setVisibility(View.VISIBLE);
            viewHolderTextItem.textViewValue.setText(configHelper.getString(item.getKey(), ""));
        }
        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View passwordItem(View convertView, PrefsBaseItem item)
    {
        View password_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            password_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_group_radio, null, false);
            viewHolderTextItem.textViewName = (TextView) password_view.findViewById(R.id.textViewName);
            viewHolderTextItem.imageViewHead = (ImageView) password_view.findViewById(R.id.imageViewHead);
            password_view.setTag(viewHolderTextItem);
            convertView = password_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());

        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View seekBarItem(View convertView, PrefsBaseItem item)
    {
        View seekBar_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            seekBar_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_group_radio, null, false);
            viewHolderTextItem.textViewName = (TextView) seekBar_view.findViewById(R.id.textViewName);
            viewHolderTextItem.imageViewHead = (ImageView) seekBar_view.findViewById(R.id.imageViewHead);
            seekBar_view.setTag(viewHolderTextItem);
            convertView = seekBar_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());
        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View booleanItem(View convertView, final PrefsBaseItem item)
    {
        View boolean_view = null;
        ViewHolderBoolean viewHolderBoolean = null;
        if (convertView == null)
        {
            viewHolderBoolean = new ViewHolderBoolean();
            boolean_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_boolean, null, false);
            viewHolderBoolean.textViewName = (TextView) boolean_view.findViewById(R.id.textViewName);
            viewHolderBoolean.imageViewHead = (ImageView) boolean_view.findViewById(R.id.imageViewHead);
            viewHolderBoolean.toggleButtonBoolean = (ToggleButton) boolean_view.findViewById(R.id.toggleButtonBoolean);
            boolean_view.setTag(viewHolderBoolean);
            convertView = boolean_view;
        }
        else
        {
            viewHolderBoolean = (ViewHolderBoolean) convertView.getTag();
        }
        viewHolderBoolean.textViewName.setText(item.getName());

        if (item.getImageId() > 0)
        {
            viewHolderBoolean.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderBoolean.imageViewHead.setVisibility(View.GONE);
        }
        // ViewHolderBoolean.toggleButtonBoolean.setChecked(((PrefsBooleanItem)
        // mData.get(position)).getChecked());

        viewHolderBoolean.toggleButtonBoolean.setChecked(configHelper.getBoolean(item.getKey(), false));
        viewHolderBoolean.toggleButtonBoolean.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1)
            {
                configHelper.putBoolean(item.getKey(), arg1);
                item.getItemCallBack().onCallBackResult(arg1);
            }
        });
        return convertView;
    }

    private View radioGroupItem(View convertView, PrefsBaseItem item)
    {
        View radio_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            radio_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_group_radio, null, false);
            viewHolderTextItem.textViewName = (TextView) radio_view.findViewById(R.id.textViewName);
            viewHolderTextItem.imageViewHead = (ImageView) radio_view.findViewById(R.id.imageViewHead);
            radio_view.setTag(viewHolderTextItem);
            convertView = radio_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());
        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View contactItem(View convertView, PrefsBaseItem item)
    {
        View contact_view = null;
        ViewHolderText viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolderText();
            contact_view = this.mInflater.inflate(R.layout.adapter_setting_list_item_group_radio, null, false);
            viewHolderTextItem.textViewName = (TextView) contact_view.findViewById(R.id.textViewName);
            viewHolderTextItem.imageViewHead = (ImageView) contact_view.findViewById(R.id.imageViewHead);
            contact_view.setTag(viewHolderTextItem);
            convertView = contact_view;
        }
        else
        {
            viewHolderTextItem = (ViewHolderText) convertView.getTag();
        }
        viewHolderTextItem.textViewName.setText(item.getName());
        if (item.getImageId() > 0)
        {
            viewHolderTextItem.imageViewHead.setBackgroundResource(item.getImageId());
        }else
        {
            viewHolderTextItem.imageViewHead.setVisibility(View.GONE);
        }
        return convertView;
    }
}
