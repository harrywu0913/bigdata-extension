package com.webex.dap.hive.serde_.example3_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde2.AbstractSerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Writable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by harry on 9/3/18.
 */
public class DemoSerDe extends AbstractSerDe {
    private Properties tableProperties;
    private String serdeName;
    private TypeInfo rowTypeInfo;
    private boolean lastColumnTakesRest;
    private List<String> columnNames;
    private List<TypeInfo> columnTypes;

    @Override
    public void initialize(@Nullable Configuration configuration, Properties properties) throws SerDeException {
        //create structInspector
        this.tableProperties = properties;
        this.serdeName = this.getClass().getName();
        String columnNameProperty = this.tableProperties.getProperty("columns");
        String columnTypeProperty = this.tableProperties.getProperty("columns.types");
        if (columnNameProperty != null && columnNameProperty.length() > 0) {
            this.columnNames = Arrays.asList(columnNameProperty.split(","));
        } else {
            this.columnNames = new ArrayList();
        }

        if (columnTypeProperty == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.columnNames.size(); ++i) {
                if (i > 0) {
                    sb.append(":");
                }
                sb.append("string");
            }
            columnTypeProperty = sb.toString();
        }

        this.columnTypes = TypeInfoUtils.getTypeInfosFromTypeString(columnTypeProperty);
        if (this.columnNames.size() != this.columnTypes.size()) {
            throw new SerDeException(this.serdeName + ": columns has " + this.columnNames.size() + " elements while columns.types has " + this.columnTypes.size() + " elements!");
        }

        ArrayList columnObjectInspectors = new ArrayList(columnTypes.size());

        for (int i = 0; i < columnTypes.size(); ++i) {
//            columnObjectInspectors.add(createLazyObjectInspector((TypeInfo) columnTypes.get(i), 1, lazyParams, ObjectInspectorFactory.ObjectInspectorOptions.JAVA));
        }
    }

//    public ObjectInspector createObjectInspector(TypeInfo typeInfo) {
//        ObjectInspector.Category category = typeInfo.getCategory();
//        switch (category) {
//            case PRIMITIVE:
//                PrimitiveObjectInspector.PrimitiveCategory primitiveCategory = ((PrimitiveTypeInfo) typeInfo).getPrimitiveCategory();
//                switch (primitiveCategory){
//                    case INT:
//
//                }
//
//
//        }
//    }

    @Override
    public Class<? extends Writable> getSerializedClass() {
        return null;
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
    public Object deserialize(Writable writable) throws SerDeException {
        return null;
    }

    @Override
    public ObjectInspector getObjectInspector() throws SerDeException {
        return null;
    }
}
