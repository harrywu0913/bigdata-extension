package com.webex.dap.hbase.htable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by harry on 6/20/18.
 */
public class CURDExample {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "sap-zookeeper-1,sap-zookeeper-2,sap-zookeeper-3");

        Connection connection = ConnectionFactory.createConnection(conf);

        Table table = connection.getTable(TableName.valueOf("t1"));


        RegionLocator regionLocator = connection.getRegionLocator(TableName.valueOf("t1"));
        regionLocator.getRegionLocation(Bytes.toBytes("r1"),true);

        table.get(new Get(Bytes.toBytes("r1")));

        table.close();

        connection.close();
    }
}
