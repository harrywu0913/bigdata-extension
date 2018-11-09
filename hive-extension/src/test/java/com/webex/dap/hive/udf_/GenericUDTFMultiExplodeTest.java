package com.webex.dap.hive.udf_;

import com.webex.dap.hive.udtf_.GenericUDTFMultiExplode;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericUDTFMultiExplodeTest {
    @Test
    public void testSameLength() throws HiveException {
        GenericUDTFMultiExplode example = new GenericUDTFMultiExplode();

        ObjectInspector[] inputOI = {
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.INT)),
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.STRING)),
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.LONG))

        };

        // create the actual UDF arguments
        List<Integer> aInt = new ArrayList<Integer>();
        aInt.add(1);
        aInt.add(2);
        aInt.add(3);

        List<String> aString = new ArrayList<String>();
        aString.add("a");
        aString.add("b");
        aString.add("c");

        List<Long> aLong = new ArrayList<Long>();
        aLong.add(1L);
        aLong.add(2L);
        aLong.add(3L);

        // the value exists
        example.initialize(inputOI);

        List<Object[]> records = example.processRecords(new Object[]{aInt, aString, aLong});
        Assert.assertEquals(3, records.size());
    }

    @Test
    public void testDifferentLength() throws HiveException {
        GenericUDTFMultiExplode example = new GenericUDTFMultiExplode();

        ObjectInspector[] inputOI = {
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.INT)),
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.STRING)),
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.LONG))

        };

        // create the actual UDF arguments
        List<Integer> aInt = new ArrayList<Integer>();
        aInt.add(1);
        aInt.add(2);

        List<String> aString = new ArrayList<String>();
        aString.add("a");
        aString.add("b");
        aString.add("c");

        List<Long> aLong = new ArrayList<Long>();
        aLong.add(1L);
        aLong.add(2L);
        aLong.add(3L);

        // the value exists
        example.initialize(inputOI);

        List<Object[]> records = example.processRecords(new Object[]{aInt, aString, aLong});
        Assert.assertEquals(2, records.size());

        // create the actual UDF arguments
        aInt = new ArrayList<Integer>();
        aInt.add(1);
        aInt.add(2);
        aInt.add(3);

        aString = new ArrayList<String>();
        aString.add("a");
        aString.add("b");

        aLong = new ArrayList<Long>();
        aLong.add(1L);
        aLong.add(2L);
        aLong.add(3L);


        records = example.processRecords(new Object[]{aInt, aString, aLong});
        Assert.assertEquals(3, records.size());
        Assert.assertNull(records.get(2)[1]);
    }

    @Test
    public void testNull() throws HiveException {
        GenericUDTFMultiExplode example = new GenericUDTFMultiExplode();

        ObjectInspector[] inputOI = {
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.INT)),
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.STRING)),
                ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.getPrimitiveJavaObjectInspector(PrimitiveObjectInspector.PrimitiveCategory.LONG))

        };

        // create the actual UDF arguments
        List<Integer> aInt = new ArrayList<Integer>();

        List<String> aString = new ArrayList<String>();
        aString.add("a");
        aString.add("b");
        aString.add("c");

        List<Long> aLong = new ArrayList<Long>();
        aLong.add(1L);
        aLong.add(2L);
        aLong.add(3L);

        // the value exists
        example.initialize(inputOI);

        List<Object[]> records = example.processRecords(new Object[]{aInt, aString, aLong});
        Assert.assertEquals(0, records.size());

        // create the actual UDF arguments
        aInt = new ArrayList<Integer>();
        aInt.add(1);
        aInt.add(2);
        aInt.add(3);

        aString = null;

        aLong = new ArrayList<Long>();
        aLong.add(1L);
        aLong.add(2L);
        aLong.add(3L);


        records = example.processRecords(new Object[]{aInt, aString, aLong});
        Assert.assertEquals(3, records.size());
        Assert.assertNull(records.get(0)[1]);
        Assert.assertNull(records.get(1)[1]);
        Assert.assertNull(records.get(2)[1]);

        // create the actual UDF arguments
        aInt = null;

        aString = new ArrayList<String>();

        aLong = new ArrayList<Long>();
        aLong.add(1L);
        aLong.add(2L);
        aLong.add(3L);


        try {
            records = example.processRecords(new Object[]{aInt, aString, aLong});
        } catch (Exception e) {
            records = new ArrayList<Object[]>();
        }
        Assert.assertEquals(0, records.size());
    }
}