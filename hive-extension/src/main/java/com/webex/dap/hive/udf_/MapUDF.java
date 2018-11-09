package com.webex.dap.hive.udf_;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.lazy.LazyMap;
import org.apache.hadoop.hive.serde2.lazy.LazyString;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;

/*

    hive> describe tb_test2;
        OK
        name	string
        score_list	array<map<string,int>>

    hive> select * from tb_test2;

        A	[{"math":100,"english":90,"history":85}]
        B	[{"math":95,"english":80,"history":100}]
        C	[{"math":80,"english":90,"histroy":100}]

    hive> select hellonew(tb_test2.name,tb_test2.score_list) from tb_test2;
        {"people":"A","totalscore":275}
        {"people":"B","totalscore":275}
        {"people":"C","totalscore":270}

 */
public class MapUDF extends GenericUDF {
    // 1.输入变量定义
    private ObjectInspector nameObj;
    private ListObjectInspector listoi;
    private MapObjectInspector mapOI;
    private ArrayList<Object> valueList = new ArrayList<Object>();


    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        nameObj = (ObjectInspector) arguments[0];
        listoi = (ListObjectInspector) arguments[1];
        mapOI = ((MapObjectInspector) listoi.getListElementObjectInspector());

        ArrayList structFieldNames = new ArrayList();
        ArrayList structFieldObjectInspectors = new ArrayList();
        structFieldNames.add("name");
        structFieldNames.add("totalScore");

        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableIntObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(structFieldNames, structFieldObjectInspectors);
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        LazyString LName = (LazyString) (arguments[0].get());
        String strName = ((StringObjectInspector) nameObj).getPrimitiveJavaObject(LName);

        int nelements = listoi.getListLength(arguments[1].get());

        int nTotalScore = 0;
        valueList.clear();

        for (int i = 0; i < nelements; i++) {
            LazyMap LMap = (LazyMap) listoi.getListElement(arguments[1].get(), i);
            //获取map中的所有value值
            valueList.addAll(mapOI.getMap(LMap).values());
            for (int j = 0; j < valueList.size(); j++) {
                nTotalScore += Integer.parseInt(valueList.get(j).toString());
            }
        }

        Object[] e;
        e = new Object[2];
        e[0] = new Text(strName);
        e[1] = new IntWritable(nTotalScore);

        return e;
    }

    @Override
    public String getDisplayString(String[] children) {
        return null;
    }
}
