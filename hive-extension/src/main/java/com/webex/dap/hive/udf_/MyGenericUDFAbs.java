package com.webex.dap.hive.udf_;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.hive.serde2.io.HiveDecimalWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

/**
 * Created by harry on 9/10/18.
 */
@Description(name = "abs",
        value = "_FUNC_(x) - returns the absolute value of x",
        extended = "Example:\n" +
                " > SELECT _FUNC_(0) FROM src LIMIT 1;\n" +
                " 0\n" +
                " > SELECT _FUNC_(-5) FROM src LIMIT 1;\n" +
                " 5\n")
public class MyGenericUDFAbs extends GenericUDF {
    private transient PrimitiveObjectInspector.PrimitiveCategory inputType;
    private transient PrimitiveObjectInspector argumentOI;
    private transient ObjectInspectorConverters.Converter inputConverter;

    private final DoubleWritable resultDouble = new DoubleWritable();
    private final LongWritable resultLong = new LongWritable();
    private final IntWritable resultInt = new IntWritable();
    private final HiveDecimalWritable resultDecimal = new HiveDecimalWritable();

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 1) {
            throw new UDFArgumentLengthException("ABS() requires 1 argument, got " + arguments.length);
        }

        if (arguments[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("ABS only takes primitive types, got " + arguments[0].getTypeName());
        }

        argumentOI = (PrimitiveObjectInspector) arguments[0];

        inputType = argumentOI.getPrimitiveCategory();

        ObjectInspector outputOI = null;
        switch (inputType) {
            case SHORT:
            case BYTE:
            case INT:
                inputConverter = ObjectInspectorConverters.getConverter(arguments[0], PrimitiveObjectInspectorFactory.writableIntObjectInspector);
                outputOI = PrimitiveObjectInspectorFactory.writableIntObjectInspector;
                break;
            case LONG:
                inputConverter = ObjectInspectorConverters.getConverter(arguments[0],
                        PrimitiveObjectInspectorFactory.writableLongObjectInspector);
                outputOI = PrimitiveObjectInspectorFactory.writableLongObjectInspector;
            case FLOAT:
            case STRING:
            case DOUBLE:
                inputConverter = ObjectInspectorConverters.getConverter(arguments[0],
                        PrimitiveObjectInspectorFactory.writableDoubleObjectInspector);
                outputOI = PrimitiveObjectInspectorFactory.writableDoubleObjectInspector;
                break;
            case DECIMAL:
                outputOI = PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(
                        ((PrimitiveObjectInspector) arguments[0]).getTypeInfo());
                inputConverter = ObjectInspectorConverters.getConverter(arguments[0],
                        outputOI);
                break;
            default:
                throw  new UDFArgumentException("ABS only takes SHORT/BYTE/INT/LONG/DOUBLE/FLOAT/STRING/DECIMAL types, got " + inputType);
        }

        return outputOI;
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        Object valObject = arguments[0].get();
        if (valObject == null){
            return null;
        }

        switch (inputType){
            case SHORT:
            case BYTE:
            case INT:
                valObject = inputConverter.convert(valObject);
                resultInt.set(Math.abs(((IntWritable) valObject).get()));
                return resultInt;
        }

        return null;
    }

    @Override
    public String getDisplayString(String[] children) {
        return getStandardDisplayString("abs", children);
    }
}
