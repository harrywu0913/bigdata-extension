package com.webex.dap.hive.serde_.example2_xml.processor;

import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;

import java.util.List;
import java.util.Map;

/**
 * Created by harry on 8/30/18.
 */
public interface XmlProcessor {
    public void initialize(XmlProcessorContext xmlProcessorContext);

    Map<String, NodeArray> parse(String s);

    List getList(Object data);

    Object getPrimitiveObjectValue(Object primitive, PrimitiveCategory primitiveCategory);
}
