package com.webex.dap.hive.serde_.example2_xml.objectinspector;

import com.webex.dap.hive.serde_.example2_xml.processor.XmlProcessor;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StandardMapObjectInspector;

/**
 * Created by harry on 8/30/18.
 */
public class XmlMapObjectInspector extends StandardMapObjectInspector {
    public XmlMapObjectInspector(ObjectInspector mapKeyObjectInspector, ObjectInspector mapValueObjectInspector, XmlProcessor xmlProcessor) {

    }
}
