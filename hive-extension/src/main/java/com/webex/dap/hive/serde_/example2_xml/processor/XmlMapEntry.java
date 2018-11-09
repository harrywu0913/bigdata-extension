package com.webex.dap.hive.serde_.example2_xml.processor;

/**
 * Created by harry on 8/30/18.
 */
public class XmlMapEntry {
    private XmlMapFacet key = null;
    private XmlMapFacet value = null;

    public XmlMapEntry(XmlMapFacet key, XmlMapFacet value) {
        this.key = key;
        this.value = value;
    }

    public XmlMapFacet getKey() {
        return key;
    }

    public XmlMapFacet getValue() {
        return value;
    }

}
