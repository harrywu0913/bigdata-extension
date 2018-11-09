package com.webex.dap.hive.serde_.example2_xml.objectinspector;

import com.webex.dap.hive.serde_.example2_xml.processor.XmlProcessor;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StandardListObjectInspector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 8/30/18.
 */
public class XmlListObjectInspector extends StandardListObjectInspector {
    private XmlProcessor xmlProcessor = null;

    public XmlListObjectInspector(ObjectInspector listElementObjectInspector, XmlProcessor xmlProcessor) {
        super(listElementObjectInspector);
        this.xmlProcessor = xmlProcessor;
    }

    @Override
    public List<?> getList(Object data) {
        List list = this.xmlProcessor.getList(data);
        ObjectInspector listElementObjectInspector = getListElementObjectInspector();
        Category category = listElementObjectInspector.getCategory();

        if (category == Category.PRIMITIVE) {
            PrimitiveObjectInspector primitiveObjectInspector = (PrimitiveObjectInspector) listElementObjectInspector;
            PrimitiveObjectInspector.PrimitiveCategory primitiveCategory = primitiveObjectInspector.getPrimitiveCategory();

            if (list != null) {
                List result = new ArrayList();
                for (Object primitive : list) {
                    result.add(this.xmlProcessor.getPrimitiveObjectValue(primitive,primitiveCategory));
                }
            }
        }
        return list;
    }

    @Override
    public int getListLength(Object data) {
        List<?> list = getList(data);
        return list == null ? -1 : list.size();
    }

    @Override
    public Object getListElement(Object data, int index) {
        List<?> list = getList(data);
        return list == null ? null : list.get(index);
    }
}
