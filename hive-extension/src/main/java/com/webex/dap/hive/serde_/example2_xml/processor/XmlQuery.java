package com.webex.dap.hive.serde_.example2_xml.processor;

/**
 * Created by harry on 8/30/18.
 */
public class XmlQuery {
    private String query = null;
    private String name = null;

    public XmlQuery(String query, String name) {
        this.query = query;
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
