package com.webex.dap.hive.serde_.example2_xml.processor;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

/**
 * Created by harry on 8/30/18.
 */
public class JavaXmlQuery extends XmlQuery {
    private XPathExpression expression = null;

    public JavaXmlQuery(XmlQuery xmlQuery) {
        super(xmlQuery.getQuery(), xmlQuery.getName());
    }

    public JavaXmlQuery compile(XPath xpath){
        try{
            this.expression = xpath.compile(getQuery());
        }catch (XPathExpressionException e){

        }
        return this;
    }

    public XPathExpression getExpression(){
        return this.expression;
    }

}
