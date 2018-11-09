package com.webex.dap.hive.serde_.example2_xml.processor;

/**
 * Created by harry on 8/30/18.
 */
public class XmlMapFacet {
    private String name = null;
    private Type type = null;

    public XmlMapFacet(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ELEMENT,
        CONTENT,
        ATTRIBUTE
    }
}
