package com.webex.dap.hive.serde_.sc_;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by harry on 8/30/18.
 */
public class LazySerDeParameters {
    private Properties tableProperties;
    private String serdeName;
    private String nullString;
    private Text nullSequence;
    private List<String> columnNames;
    private List<TypeInfo> columnTypes;

    public LazySerDeParameters(Configuration job, Properties tbl, String serdeName) throws SerDeException {
        this.tableProperties = tbl;
        this.serdeName = serdeName;
        this.nullString = tbl.getProperty("serialization.null.format", "\\N");
        this.nullSequence = new Text(this.nullString);
        this.extractColumnInfo();

    }

    public void extractColumnInfo() throws SerDeException {
        String columnNameProperty = this.tableProperties.getProperty("columns");
        String columnTypeProperty = this.tableProperties.getProperty("columns.types");

        if (columnNameProperty != null && columnNameProperty.length() > 0) {
            this.columnNames = Arrays.asList(columnNameProperty.split(","));
        } else {
            this.columnNames = new ArrayList<String>();
        }

        if (columnTypeProperty == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.columnNames.size(); i++) {
                if (i > 0) {
                    sb.append(":");
                }

                sb.append("string");
            }

            columnTypeProperty = sb.toString();
        }

        this.columnTypes = TypeInfoUtils.getTypeInfosFromTypeString(columnTypeProperty);

        if (this.columnNames.size() != this.columnTypes.size()) {
            throw new SerDeException("");
        }
    }
}
