package com.webex.dap.hive.serde_.csv_;

import org.apache.hadoop.hive.cli.CliDriver;

/**
 * Created by harry on 5/23/18.
 */
public class CsvMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");

//        String[] cmd = new String[]{"hive","-e","explain select count(*) from sample_07 where code = '53-7072'"};
//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select count(*) from sample_07 where code = '53-7072'"};
//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select * from csv_table_1"};

//        String[] cmd = new String[]{"hive","-e","set role admin"};

//        String[] cmd = new String[]{"hive", "-e", "show databases"};
        String[] cmd = new String[]{"hive", "-e", "select * from type_demo;"};


        CliDriver.main(cmd);

    }
}
