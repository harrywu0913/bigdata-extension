package com.webex.dap.hive.serde_.example2_xml.objectinspector;

import com.webex.dap.hive.serde_.example2_xml.processor.XmlProcessor;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 8/30/18.
 */
public class XmlObjectInspectorFactory {
    public static ObjectInspector getStandardJavaObjectInspectorFromTypeInfo(TypeInfo typeInfo, XmlProcessor xmlProcessor){
        switch (typeInfo.getCategory()){
            case PRIMITIVE:
                return PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(((PrimitiveTypeInfo)typeInfo).getPrimitiveCategory());
            case LIST:
                ObjectInspector listElementObjectInspector = getStandardJavaObjectInspectorFromTypeInfo(((ListTypeInfo)typeInfo).getListElementTypeInfo(),xmlProcessor);

                return new XmlListObjectInspector(listElementObjectInspector,xmlProcessor);
            case MAP:
                MapTypeInfo mapTypeInfo = (MapTypeInfo) typeInfo;
                ObjectInspector mapKeyObjectInspector = getStandardJavaObjectInspectorFromTypeInfo(mapTypeInfo.getMapKeyTypeInfo(),xmlProcessor);
                ObjectInspector mapValueObjectInspector = getStandardJavaObjectInspectorFromTypeInfo(mapTypeInfo.getMapValueTypeInfo(),xmlProcessor);

                return new XmlMapObjectInspector(mapKeyObjectInspector,mapValueObjectInspector,xmlProcessor);

            case STRUCT:
                StructTypeInfo structTypeInfo = (StructTypeInfo) typeInfo;
                List<String> structFieldNames = structTypeInfo.getAllStructFieldNames();
                List<TypeInfo> fieldTypeInfos = structTypeInfo.getAllStructFieldTypeInfos();
                List<ObjectInspector> structFieldObjectInspectors = new ArrayList<ObjectInspector>(fieldTypeInfos.size());
                for (int fieldIndex = 0;fieldIndex < fieldTypeInfos.size();fieldIndex++){
                    structFieldObjectInspectors.add(getStandardJavaObjectInspectorFromTypeInfo(fieldTypeInfos.get(fieldIndex),xmlProcessor));
                }

                return getStandardStructObjectInspector(structFieldNames,structFieldObjectInspectors,xmlProcessor);
            default:
                throw new IllegalStateException();
        }
    }

    public static ObjectInspector getStandardStructObjectInspector(List<String> structFieldNames, List<ObjectInspector> structFieldObjectInspectors, XmlProcessor xmlProcessor) {
        return new XmlStructObjectInspector(structFieldNames,structFieldObjectInspectors,xmlProcessor);
    }
}
