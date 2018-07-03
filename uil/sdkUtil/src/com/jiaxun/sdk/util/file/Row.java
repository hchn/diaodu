package com.jiaxun.sdk.util.file;

/**
 * csv文件格式行处理类
 */
public class Row
{
    private String rowString;

    public Row(String csvRow)
    {
        rowString = csvRow;
    }

    public String toString()
    {
        return rowString.toString();
    }

    public String[] rebuild()
    {
        return rowString.split(",");
    }

    public boolean equals(Object other)
    {
        if (other instanceof Row)
        {
            Row foreign = (Row) other;
            return rowString.equals(foreign.rowString);
        }
        return false;
    }
}
