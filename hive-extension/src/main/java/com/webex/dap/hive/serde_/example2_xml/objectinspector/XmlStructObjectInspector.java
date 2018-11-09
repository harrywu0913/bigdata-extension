package com.webex.dap.hive.serde_.example2_xml.objectinspector;

import com.webex.dap.hive.serde_.example2_xml.processor.XmlProcessor;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StandardStructObjectInspector;

import java.util.List;

/**
 * Created by harry on 8/30/18.
 */
public class XmlStructObjectInspector extends StandardStructObjectInspector {
    public XmlStructObjectInspector(List<String> structFieldNames, List<ObjectInspector> structFieldObjectInspectors, XmlProcessor xmlProcessor) {

    }
}
