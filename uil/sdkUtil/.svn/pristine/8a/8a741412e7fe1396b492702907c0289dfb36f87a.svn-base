package com.jiaxun.sdk.util.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.TextUtils;

import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：Dom解析Xml
 *
 * @author  wangsl
 *
 * @Date 2015-1-20
 */
public class XmlDomTools
{
    private static String TAG = XmlDomTools.class.getName();
    private Document document;
    private String fileName;
    private String xmlStr;

    public void parseXmlFile(String fileName)
    {
        Log.info(TAG, "parseXmlStr::fileName:" + fileName);
        if(TextUtils.isEmpty(fileName))
        {
            return;
        }
        try
        {
            this.fileName = fileName;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(new File(fileName));
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }
    
    public void parseXmlStr(String xmlStr, String encode)
    {
        Log.info(TAG, "parseXmlStr::xmlStr:" + xmlStr);
        if(TextUtils.isEmpty(xmlStr))
        {
            return;
        }
        try
        {
            this.xmlStr = xmlStr;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(new ByteArrayInputStream(xmlStr.getBytes(encode)));
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * 取节点内容
     * 
     * @param path 目录
     * @return
     */
    public String getValueByName(String path)
    {
        String value = null;
        Node node = getNode(path);
        if (node != null)
        {
            value = node.getTextContent();
        }
        return value;
    }
    
    /**
     * 取节点属性内容
     * 
     * @param path 目录
     * @return
     */
    public String getValueByAttribute(String path, String attribute)
    {
        String value = null;
        Node node = getNode(path);
        NamedNodeMap namedNodeMap;
        if (node != null)
        {
            namedNodeMap = node.getAttributes();
            value = namedNodeMap.getNamedItem(attribute).getNodeValue();
        }
        return value;
    }

    /**
     * 取xml节点下子节点列表
     * 
     * @param path 目录
     * @return 
     */
    public NodeList getNodeList(String path)
    {
        NodeList nodeList = null;
        nodeList = selectNodes(path, document.getDocumentElement());
        return nodeList;
    }

    /**
     * 取节点
     * 
     * @param path 目录
     * @return
     */
    public Node getNode(String path)
    {
        Node node = null;
        node = selectSingleNode(path, document.getDocumentElement());
        return node;
    }

    // 查找所有的节点
    private NodeList selectNodes(String expression, Object source)
    {
        try
        {
            return (NodeList) XPathFactory.newInstance().newXPath().evaluate(expression, source, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e)
        {
            Log.exception(TAG, e);
            return null;
        }
    }

    // 查找一个单独的节点
    private static Node selectSingleNode(String expression, Object source)
    {
        try
        {
            return (Node) XPathFactory.newInstance().newXPath().evaluate(expression, source, XPathConstants.NODE);
        }
        catch (XPathExpressionException e)
        {
            Log.exception(TAG, e);
            return null;
        }
    }

}
