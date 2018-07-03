package com.jiaxun.uil.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.uil.model.CallRecordListItem;

/**
 * 联系人搜索功能
 */
public class SearchContacts
{
    // 按键类型：字母
    private final static int TYPE_LETTER = 1;
    // 按键类型：数字
    private final static int TYPE_NUMBER = 2;
    // 按键类型：字母，数字
    private int type;
    // 拨号盘按键对照表
    private static HashMap<String, String> dialMap;
    static
    {// 初始化数据
        if (dialMap == null)
        {
            dialMap = new HashMap<String, String>();
            dialMap.put("2", "[abc]");
            dialMap.put("3", "[def]");
            dialMap.put("4", "[ghi]");
            dialMap.put("5", "[jkl]");
            dialMap.put("6", "[mno]");
            dialMap.put("7", "[pqrs]");
            dialMap.put("8", "[tuv]");
            dialMap.put("9", "[wxyz]");
        }
    }
    private final static String DIGITS = "[0-9]*";// 数字字符串
    private final static String LETTERS = "[a-z]*|[A-Z]*";// 字母字符串
    private final static String DX = ".*";// 字母字符串

    // 关键字：结果列表
    private HashMap<String, ArrayList<ContactModel>> resultContactMap = new HashMap<String, ArrayList<ContactModel>>();
    // 联系人原始数据源列表
    private ArrayList<ContactModel> contactAll;
    // 联系人搜索数据源列表
    private ArrayList<ContactModel> contacts;
    // 联系人原始数据源列表
    private ArrayList<CallRecordListItem> recordAll;
    // 联系人搜索数据源列表
    private ArrayList<CallRecordListItem> records;
    // 关键字：结果列表
    private HashMap<String, ArrayList<CallRecordListItem>> resultRecordMap = new HashMap<String, ArrayList<CallRecordListItem>>();

    public void setContactAll(ArrayList<ContactModel> contactAll)
    {
        for (int i = 0; i < contactAll.size(); i++)
        {
            for (int j = (contactAll.size() - 1); j > i; j--)
            {
                if (contactAll.get(i).getPhoneNumList().size() > 0 && contactAll.get(j).getPhoneNumList().size() > 0)
                {
                    String number1 = contactAll.get(i).getPhoneNumList().get(0).getNumber();
                    String number2 = contactAll.get(j).getPhoneNumList().get(0).getNumber();
                    if (!(TextUtils.isEmpty(number1)) && !(TextUtils.isEmpty(number2)))
                    {
                        if (number1.equals(number2))
                        {
                            contactAll.remove(contactAll.get(j));
                        }
                    }
                }
            }
        }

        this.contactAll = contactAll;
    }

    public void setRecordAll(ArrayList<CallRecordListItem> recordAll)
    {
//        for (int i = 0; i < recordAll.size(); i++)
//        {
//            for (int j = (recordAll.size() - 1); j > i; j--)
//            {
//                String peer1 = recordAll.get(i).getCallRecord().getPeerNum();
//                String peer2 = recordAll.get(j).getCallRecord().getPeerNum();
//                if (!(TextUtils.isEmpty(peer1)) && !(TextUtils.isEmpty(peer2)))
//                {
//                    if (peer1.equals(peer2))
//                    {
//                        recordAll.remove(recordAll.get(j));
//                    }
//                }
//            }
//        }
        this.recordAll = recordAll;
    }

    /**
     * 方法说明 : 联系人搜索
     * 
     * @param searchStr
     *              关键字
     * 
     * @return 结果列表
     */
    public ArrayList<ContactModel> onContactSearch(String searchStr)
    {
        if (TextUtils.isEmpty(searchStr) || contactAll == null || contactAll.size() == 0)
        {
            return null;
        }

        if (contacts == null || searchStr.length() == 1)
            contacts = contactAll;

        // 如果是退格则获取历史搜索结果
        ArrayList<ContactModel> result = resultContactMap.get(searchStr);
        if (result == null)
        {
            result = new ArrayList<ContactModel>();
            resultContactMap.put(searchStr, result);
        }
        else
        {
            contacts = result;// 当前查询结果
            return result;// 返回结果
        }

        if (searchStr.matches(DIGITS))
            type = TYPE_NUMBER;
        else if (searchStr.matches(LETTERS))
            type = TYPE_LETTER;

        Character c;
        String[] letters;
        String key = "";
        int k = 0;
        boolean isMatch = false;
        searchStr = searchStr.replace("*", "[*]");
        String numMa = DIGITS + searchStr + DIGITS;
        ArrayList<ContactNum> phoneNumList;
        for (ContactModel contactEntity : contacts)
        {// 遍历通讯录
            if (contactEntity.getName().startsWith(searchStr))// 匹配名称
            {// 匹配名称
                result.add(contactEntity);
                continue;
            }

            if (type == TYPE_NUMBER)
            {// 数字
                phoneNumList = contactEntity.getPhoneNumList();
                for (ContactNum contactNum : phoneNumList)
                {// 遍历号码
                    if (contactNum.getNumber().matches(numMa))
                    {
                        result.add(contactEntity);
                        break;
                    }
                }
            }
            else if (type == TYPE_LETTER)
            {// 字母
                letters = contactEntity.getNameLettersArray();
                if (letters == null || letters.length == 0)
                    continue;

                if (searchStr.length() == 1)
                {// 单个关键字
                    if (letters[0].startsWith(searchStr))// 匹配名称
                        result.add(contactEntity);
                }
                else
                {
                    for (int i = 0; i < searchStr.length(); i++)
                    {// 便利搜索关键字
                        c = searchStr.charAt(i);
                        key += c.toString();
                        for (int j = k; j < letters.length; j++)
                        {// 便利拼音
                            if (letters[j].startsWith(key))
                            {
                                isMatch = true;
                                break;
                            }
                            else
                            {
                                isMatch = false;
                                k++;
                                key = c.toString();
                            }
                        }
                    }

                    if (isMatch)
                        result.add(contactEntity);

                    key = "";
                    k = 0;
                    isMatch = false;
                }
            }
        }

        contacts = result;// 当前查询结果
        return result;
    }

    /**
     * 方法说明 : 拨号盘搜索
     * 
     * @param searchStr
     *              关键字
     * 
     * @return 结果列表
     */
    public ArrayList<ContactModel> onDialSearch(String searchStr)
    {
        if (TextUtils.isEmpty(searchStr) || contactAll == null || contactAll.size() == 0)
        {
            return null;
        }

        if (contacts == null || searchStr.length() == 1)
            contacts = contactAll;

        // 如果是退格则获取历史搜索结果
        ArrayList<ContactModel> result = resultContactMap.get(searchStr);
        if (result == null)
        {
            result = new ArrayList<ContactModel>();
            resultContactMap.put(searchStr, result);
        }
        else
        {
            contacts = result;// 当前查询结果
            return result;// 返回结果
        }

        searchStr = searchStr.replace("*", "[*]");
        String numMa = DIGITS + searchStr + DIGITS;
        ArrayList<ContactNum> phoneNumList;
        String namMa = searchStr + DX;

        Character c;
        String[] letters;
        String key = "";
        int k = 0;
        boolean isMatch = false;
        for (ContactModel contactEntity : contacts)
        {// 遍历联系人
            if (contactEntity.getName().matches(namMa))
            {// 匹配名称
                result.add(contactEntity);
                continue;
            }

            // 匹配号码
            phoneNumList = contactEntity.getPhoneNumList();
            for (ContactNum contactNum : phoneNumList)
            {// 遍历号码
                if (contactNum.getNumber().matches(numMa))
                {// 匹配号码
                    result.add(contactEntity);
                    break;
                }
            }

            // 匹配字母
            letters = contactEntity.getNameLettersArray();
            if (letters == null || letters.length == 0)
                continue;

            for (int i = 0; i < searchStr.length(); i++)
            {// 遍历搜索关键字
                c = searchStr.charAt(i);
                key += dialMap.get(c.toString());
                if (searchStr.length() == 1)
                {// 单个关键字
                    if (letters[0].matches(key + DX))// 匹配首字拼音
                        result.add(contactEntity);
                }
                else
                {
                    for (int j = k; j < letters.length; j++)
                    {// 遍历拼音
                        if (letters[j].matches(key + DX))
                        {
                            isMatch = true;
                            break;
                        }
                        else
                        {
                            isMatch = false;
                            k++;
                            key = dialMap.get(c.toString());
                        }
                    }
                }
            }

            if (isMatch)
                result.add(contactEntity);

            key = "";
            k = 0;
            isMatch = false;
        }

        contacts = result;// 当前查询结果
        return result;
    }

    /**
     * 方法说明 : 通话记录搜索
     * 
     * @param searchStr
     *              关键字
     * @param begin
     *              开始时间
     * @param end
     *             结束时间
     * 
     * @return 结果列表
     */
    public ArrayList<CallRecordListItem> onRecordSearch(String searchStr, long begin, long end)
    {
        if (TextUtils.isEmpty(searchStr) || recordAll == null || recordAll.size() == 0)
        {
            return null;
        }

        records = onRecordSearch(begin, end);
        return onRecordSearch(searchStr, records);
    }

    /**
     * 方法说明 : 通话记录搜索
     * 
     * @param begin
     *              开始时间
     * @param end
     *             结束时间
     * 
     * @return 结果列表
     */
    public ArrayList<CallRecordListItem> onRecordSearch(long begin, long end)
    {
        if (recordAll == null || recordAll.size() == 0)
        {
            return null;
        }

        ArrayList<CallRecordListItem> result = new ArrayList<CallRecordListItem>();
        long startTime;
        for (CallRecordListItem record : recordAll)
        {// 遍历通话记录
            startTime = record.getCallRecord().getStartTime();
            if (startTime >= begin && startTime <= end)
                result.add(record);
        }
        return result;
    }

    /**
     * 方法说明 : 通话记录搜索
     * 
     * @param searchStr
     *              关键字
     * 
     * @return 结果列表
     */
    public ArrayList<CallRecordListItem> onRecordSearch(String searchStr)
    {
        if (TextUtils.isEmpty(searchStr) || recordAll == null || recordAll.size() == 0)
        {
            return null;
        }

        if (records == null || searchStr.length() == 1)
            records = recordAll;

        return onRecordSearch(searchStr, records);
    }

    /**
     * 方法说明 : 从已有通话记录搜索
     * 
     * @param searchStr
     *              关键字
     * @param records
     *              已有通话记录
     * 
     * @return 结果列表
     */
    private ArrayList<CallRecordListItem> onRecordSearch(String searchStr, ArrayList<CallRecordListItem> records)
    {
        // 如果是退格则获取历史搜索结果
        ArrayList<CallRecordListItem> result = resultRecordMap.get(searchStr);
        if (result == null || result.size() == 0)
        {
            result = new ArrayList<CallRecordListItem>();
            resultRecordMap.put(searchStr, result);
        }
        else
        {
            records = result;// 当前查询结果
            return result;// 返回结果
        }

        if (searchStr.matches(DIGITS))
            type = TYPE_NUMBER;
        else if (searchStr.matches(LETTERS))
            type = TYPE_LETTER;

        Character c;
        String[] letters;
        String key = "";
        int k = 0;
        boolean isMatch = false;
        searchStr = searchStr.replace("*", "[*]");
        String numMa = DIGITS + searchStr + DIGITS;
        String namMa = searchStr + DX;
        String peerName;
        String peerNum;
        String callerName;
        String callerNum;
        for (CallRecordListItem record : records)
        {// 遍历通话记录
            peerName = record.getCallRecord().getPeerName();
            if (peerName != null && peerName.matches(namMa))
            {
                result.add(record);
                continue;
            }
            peerNum = record.getCallRecord().getPeerNum();
            // 会议主席没有得到peernum
            if (peerNum != null && peerNum.matches(numMa))
            {
                result.add(record);
                continue;
            }

            //本地号码
            callerName = record.getCallRecord().getCallerName();
            if (callerName != null && callerName.matches(namMa))
            {
                result.add(record);
                continue;
            }
            callerNum = record.getCallRecord().getCallerNum();
            // 会议主席没有得到peernum
            if (callerNum != null && callerNum.matches(numMa))
            {
                result.add(record);
                continue;
            }

            letters = record.getNameLettersArray();
            if (letters == null)
                continue;

            if (searchStr.length() == 1)
            {// 单个关键字
                if (letters[0].startsWith(searchStr))// 匹配首字拼音
                    result.add(record);
            }
            else
            {
                for (int i = 0; i < searchStr.length(); i++)
                {// 遍历关键字
                    c = searchStr.charAt(i);
                    key += c.toString();
                    for (int j = k; j < letters.length; j++)
                    {// 遍历拼音
                        if (letters[j].startsWith(key))
                        {
                            isMatch = true;
                            break;
                        }
                        else
                        {
                            isMatch = false;
                            k++;
                            key = c.toString();
                        }
                    }
                }
            }

            if (isMatch)
                result.add(record);

            key = "";
            k = 0;
            isMatch = false;
        }

        records = result;// 当前查询结果
        return result;
    }

    /**
     * 清空
     */
    public void clear()
    {
        for (ArrayList<ContactModel> contactEntities : resultContactMap.values())
        {
            contactEntities.clear();
        }
        resultContactMap.clear();
        contacts = null;

        for (ArrayList<CallRecordListItem> records : resultRecordMap.values())
        {
            records.clear();
        }
        resultRecordMap.clear();
        records = null;
    }

}
