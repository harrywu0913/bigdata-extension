package com.webex.dap.hive.udtf_;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.TaskExecutionException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;

import java.util.ArrayList;
import java.util.List;

@Description(name = "multiexplode",
        value = "_FUNC_(arr1,arr2,...) - separates the elements of array(s) into multiple rows(each row contains one element from each array)")
public class GenericUDTFMultiExplode extends GenericUDTF {
    private transient List<ObjectInspector> inputOI = new ArrayList<ObjectInspector>();
    private int arrayLength = 0;

    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        this.arrayLength = argOIs.length;
        for (int i = 0; i < this.arrayLength; i++) {
            switch (argOIs[i].getCategory()) {
                case LIST:
                    inputOI.add(argOIs[i]);
                    fieldNames.add("col_" + i);
                    fieldOIs.add(((ListObjectInspector) argOIs[i]).getListElementObjectInspector());
                    break;
                default:
                    throw new UDFArgumentException("explode() takes array(s) as a parameter");
            }
        }

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    private transient Object[] forwardListObj;


    @Override
    public void process(Object[] args) throws HiveException {
        List<Object[]> records = processRecords(args);

        for (Object[] record: records) {
            forward(record);
        }
    }

    public List<Object[]> processRecords(Object[] args) throws HiveException{
        List<Object[]> forwardListObjs = new ArrayList<Object[]>();
        //Simply,take the first array's length as return size
        int elementLength = 0;
        switch (inputOI.get(0).getCategory()) {
            case LIST:
                ListObjectInspector listOI = (ListObjectInspector) inputOI.get(0);
                List list = listOI.getList(args[0]);
                if (list == null) {
                    return forwardListObjs;
                }

                elementLength = list.size();
                break;
            default:
                throw new TaskExecutionException("explode() can only operate on array(s)");
        }

        for (int i = 0; i < elementLength; i++) {
            forwardListObj = new Object[arrayLength];
            for (int j = 0; j < arrayLength; j++) {
                List<?> list = ((ListObjectInspector) inputOI.get(j)).getList(args[j]);
                if (list == null || list.size() < i + 1) {
                    forwardListObj[j] = null;
                } else {
                    forwardListObj[j] = list.get(i);
                }
            }

            forwardListObjs.add(forwardListObj);
        }

        return forwardListObjs;
    }

    @Override
    public void close() throws HiveException {

    }
}
