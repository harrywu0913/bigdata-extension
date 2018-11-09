package com.webex.dap.hive.udf_;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
/*
https://www.cnblogs.com/en-heng/p/5462796.html
 */

public class READMEUDF extends GenericUDF {
    //
    //
    //1. 输入变量定义
    private ObjectInspector peopleObj;
    private ObjectInspector timeObj;
    private ObjectInspector placeObj;

    // 之前保存的记录
    String strPrePeople = "";
    String strPreTime = "";
    String strPrePlace = "";

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        peopleObj = (ObjectInspector) arguments[0];
        timeObj = (ObjectInspector) arguments[1];
        placeObj = (ObjectInspector) arguments[2];

        //2.校验

        //3.输出类型定义
        ArrayList<String> structFieldNames = new ArrayList<String>();
        ArrayList structFieldObjectInspectors = new ArrayList();
        structFieldNames.add("people");
        structFieldNames.add("day");
        structFieldNames.add("from_time");
        structFieldNames.add("from_place");
        structFieldNames.add("to_time");
        structFieldNames.add("to_place");

        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(structFieldNames, structFieldObjectInspectors);
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        return null;
    }

    @Override
    public String getDisplayString(String[] children) {
        return null;
    }
}
