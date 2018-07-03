package com.jiaxun.uil.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 说明：对输入进行限制
 *
 * @author  zhangxd
 *
 * @Date 2015-9-1
 */
public class InputFilterUtil
{

    public static InputFilter getInputFilter(final String regular)
    {
        // 车号
        InputFilter mInputft_cheHao = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                return returnInputChar(source, dest, dstart, regular);
            }
        };

        return mInputft_cheHao;
    }

    /**对输入字符针对相应的正则表达式进行判断*/
    public static CharSequence returnInputChar(CharSequence source, Spanned dest, int dstart, String regularString)
    {
        String resultString = getResultString(source, dest, dstart);
        if (resultString.matches(regularString))
            return source;
        else
            return "";
    }

    /**将输入的字母进行组合，预先获取edittext的整个字符串*/
    public static String getResultString(CharSequence source, Spanned dest, int dstart)
    {
        if (source == null || dest == null)
            return "";
        StringBuilder strBuilder = new StringBuilder(dest.toString());
        strBuilder.insert(dstart, source);
        return strBuilder.toString();
    }
}
