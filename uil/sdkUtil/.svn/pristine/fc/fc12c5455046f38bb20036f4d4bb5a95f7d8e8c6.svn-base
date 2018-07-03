package com.jiaxun.sdk.util.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jiaxun
 * Date: 12-5-17
 * Time: ÉÏÎç11:24
 */
public class Element {
    String id = null;
    String value = null;
    ArrayList<Attribute> attrList = new ArrayList<Attribute>(0);
    ArrayList<Element> elements = new ArrayList<Element>();
    String content = null;
    String reason = null;

    public void addReason(String reason)
    {
    	this.reason = reason;
    }
    public String getReason()
    {
    	return reason;
    }
    public Element(String msg) {

    }

    public Element(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public Element(String id, String value, List<Attribute> attributes) {
        this.id = id;
        this.value = value;

        if(attributes != null) {
            attrList.addAll(attributes);
        }
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public Attribute getAttribute(String attr) {
        for(Attribute one : attrList) {
            if(one.name.equals(attr)) {
                return one;
            }
        }

        return null;
    }

    public List<Attribute> getAttributes() {
        return attrList;
    }
    
    public ArrayList<Element> getElements()
    {
        return elements;
    }
    public void addElements(ArrayList<Element> elements)
    {
        this.elements = elements;
    }

    public void addAttribute(Attribute attr) {
        attrList.add(attr);
    }

    public void addAttribute(String name, String value) {
        Attribute attr = new Attribute();
        attr.name = name;
        attr.value = value;

        attrList.add(attr);
    }

    static public class Attribute {
        public String name = null;
        public String value = null;

        public Attribute(){

        }

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
