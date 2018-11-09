package com.webex.dap.data.phoenix.model;

import com.google.common.collect.Maps;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.sql.*;
import java.util.Map;

/**
 * Created by harry on 4/23/18.
 */
public class PhoenixDBWritable implements DBWritable {
    Map<String,Object> result = Maps.newHashMap();

    public void write(PreparedStatement preparedStatement) throws SQLException {

    }

    public void readFields(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        int columns = resultSetMetaData.getColumnCount();

        for (int i = 1; i <= columns; i++) {
            switch (resultSetMetaData.getColumnType(i)){
                case Types.BIGINT:
                    result.put(resultSetMetaData.getColumnName(i).toLowerCase(),resultSet.getInt(i));
                    break;
                case Types.DOUBLE:
                    result.put(resultSetMetaData.getColumnName(i).toLowerCase(),resultSet.getDouble(i));
                    break;
                case Types.VARCHAR:
                    String value = resultSet.getString(i);
                    if (value == null || "".equals(value)){
                        break;
                    }

                    result.put(resultSetMetaData.getColumnName(i).toLowerCase(),value);
                    break;
            }
        }
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
