package com.webex.dap.hive.udtf_;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    https://blog.csdn.net/kent7306/article/details/50200061



    该table只有name一列，该列中一个或是多个名字
    ~$ cat ./people.txt
        John Smith
        John and Ann White
        Ted Green
        Dorothy

    新的table：
        1. 姓 和 名分为两列
        2. 所有的记录都包含姓名
        3. 每条记录或有包含多个人名

 */
public class NameSplitUDTF extends GenericUDTF {
    private PrimitiveObjectInspector stringOI = null;

    /*
        该方法中，我们制定输入输出参数：输入参数ObjectInspector和输出参数StructObjectInspector
     */
    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
        if (argOIs.length != 1) {
            throw new UDFArgumentException("NameSplitUDTF() takes exactly one argument");
        }

        if (argOIs[0].getCategory() != ObjectInspector.Category.PRIMITIVE && ((PrimitiveObjectInspector) argOIs[0]).getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
            throw new UDFArgumentException("NameSplitUDTF() takes a string as a parameter");
        }

        //输入格式
        stringOI = (PrimitiveObjectInspector) argOIs[0];

        //输出格式
        List<String> fieldNames = new ArrayList<String>(2);
        List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>(2);
        fieldNames.add("name");
        fieldNames.add("surname");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    /*
        我们将处理一条输入记录，输出若干结果记录
     */
    @Override
    public void process(Object[] args) throws HiveException {
        final String name = stringOI.getPrimitiveJavaObject(args[0]).toString();
        ArrayList<Object[]> results = processInputRecord(name);

        Iterator<Object[]> it = results.iterator();

        while (it.hasNext()){
            Object[] r = it.next();
            forward(r);
        }
    }

    public ArrayList<Object[]> processInputRecord(String name) {
        ArrayList<Object[]> result = new ArrayList<Object[]>();

        // 忽略null值与空值
        if (name == null || name.isEmpty()) {
            return result;
        }

        String[] tokens = name.split("\\s+");

        if (tokens.length == 2) {
            result.add(new Object[]{tokens[0], tokens[1]});
        } else if (tokens.length == 4 && tokens[1].equals("and")) {
            result.add(new Object[]{tokens[0], tokens[3]});
            result.add(new Object[]{tokens[2], tokens[3]});
        }

        return result;
    }

    /*
        当没有记录处理的时候该方法会被调用，用来清理代码或是产生额外的输出
     */
    @Override
    public void close() throws HiveException {

    }
}
