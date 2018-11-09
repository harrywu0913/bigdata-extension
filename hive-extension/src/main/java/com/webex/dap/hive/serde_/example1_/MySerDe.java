package com.webex.dap.hive.serde_.example1_;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.AbstractSerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by harry on 8/30/18.
 */
public class MySerDe extends AbstractSerDe {
    // params
    private List<String> columnNames = null;
    private List<TypeInfo> columnTypes = null;
    private ObjectInspector objectInspector = null;

    // seperator
    private String nullString = null;
    private String lineSep = null;
    private String kvSep = null;

    @Override
    public void initialize(@Nullable Configuration configuration, Properties tbl) throws SerDeException {
        this.lineSep = "\n";
        this.kvSep = "=";
        this.nullString = tbl.getProperty(serdeConstants.SERIALIZATION_NULL_FORMAT, "");

        String columnNameProp = tbl.getProperty(serdeConstants.LIST_COLUMNS);
        if (columnNameProp != null && columnNameProp.length() > 0) {
            this.columnNames = Arrays.asList(columnNameProp.split(","));
        } else {
            this.columnNames = new ArrayList<String>();
        }

        String columnTypeProp = tbl.getProperty(serdeConstants.LIST_COLUMN_TYPES);
        if (columnTypeProp == null) {
            String[] types = new String[columnNames.size()];
            Arrays.fill(types, 0, types.length, serdeConstants.STRING_TYPE_NAME);
            columnTypeProp = StringUtils.join(types, ":");
        }

        this.columnTypes = TypeInfoUtils.getTypeInfosFromTypeString(columnTypeProp);

        if (this.columnNames.size() != this.columnTypes.size()) {
            throw new SerDeException("columnNames.size() != columnTypes.size()");
        }

        List<ObjectInspector> columnOIs = new ArrayList<ObjectInspector>();
        ObjectInspector oi;
        for (int i = 0; i < this.columnNames.size(); i++) {
            oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(this.columnTypes.get(i));
            columnOIs.add(oi);
        }

        this.objectInspector = ObjectInspectorFactory.getStandardStructObjectInspector(this.columnNames, columnOIs);
    }

    @Override
    public Class<? extends Writable> getSerializedClass() {
        return Text.class;
    }

    @Override
    public Writable serialize(Object o, ObjectInspector objectInspector) throws SerDeException {
        return null;
    }

    @Override
    public SerDeStats getSerDeStats() {
        return null;
    }

    @Override
    public Object deserialize(Writable wr) throws SerDeException {
        if (wr == null)
            return null;

        Map<String, String> kvMap = new HashMap<String, String>();
        Text text = (Text) wr;

        for (String kv : text.toString().split(this.lineSep)) {
            String[] pair = kv.split(this.kvSep);
            if (pair.length == 2) {
                kvMap.put(pair[0], pair[1]);
            }
        }

        ArrayList<Object> row = new ArrayList<Object>();
        String colName = null;
        TypeInfo typeInfo = null;
        Object obj = null;
        for (int i = 0; i < this.columnNames.size(); i++) {
            colName = this.columnNames.get(i);
            typeInfo = this.columnTypes.get(i);
            obj = null;

            if (typeInfo.getCategory() == ObjectInspector.Category.PRIMITIVE){
                PrimitiveTypeInfo primitiveTypeInfo = (PrimitiveTypeInfo) typeInfo;
                switch (primitiveTypeInfo.getPrimitiveCategory()){
                    case STRING:
                        obj = StringUtils.defaultString(kvMap.get(colName),"");
                        break;
                    case LONG:
                    case INT:
                        try{
                            obj = Long.parseLong(kvMap.get(colName));
                        }catch (Exception e){

                        }
                }
            }

            row.add(obj);
        }

        return row;
    }

    @Override
    public ObjectInspector getObjectInspector() throws SerDeException {
        return this.objectInspector;
    }
}
