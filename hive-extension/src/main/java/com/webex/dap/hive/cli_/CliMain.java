package com.webex.dap.hive.cli_;

import org.apache.hadoop.hive.cli.CliDriver;

/**
 * Created by harry on 5/23/18.
 */
public class CliMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "u3");

//        String[] cmd = new String[]{"hive","-e","explain select count(*) from sample_07 where code = '53-7072'"};
//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select count(*) from sample_07 where code = '53-7072'"};
//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select a from csv_table_1 group by a"};

//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select substring(description,0,2) from sample_07"};
//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select substring(description,0,2) from sample_07"};

//        String[] cmd = new String[]{"hive","-e","set role admin"};

//        String[] cmd = new String[]{"hive", "-e", "show databases"};

//        String[] cmd = new String[]{"hive","-e","set hive.plan.serialization.format=javaXML;select concat(description,\"xx\") , total_emp + 1 ,salary + 20 from sample_07"};

//        String[] cmd = new String[]{"hive","-e","select salary,description from sample_07 where code = '53-7072'"};
//        String[] cmd = new String[]{"hive","-e","select * from lzo_test"};
//        String[] cmd = new String[]{"hive","-e","select a,count(1) from lzo_test where a = 1 group by a"};

//        String[] cmd = new String[]{"hive", "-e", "create database test8"};
//        String[] cmd = new String[]{"hive", "-e", "show functions"};

//        String[] cmd = new String[]{"hive","-e","create role r2"};

//        String[] cmd = new String[]{"hive","-e","grant role r1 to user u1"};
//        String[] cmd = new String[]{"hive", "-e", "grant role r1 to group g1"};
//        String[] cmd = new String[]{"hive","-e","grant role r2 to user u1"};
//        String[] cmd = new String[]{"hive","-e","grant role r2 to user u2"};

//        String[] cmd = new String[]{"hive", "-e", "grant create to group staff"};
//        String[] cmd = new String[]{"hive", "-e", "grant create to user u3"};

        String[] cmd = new String[]{"hive","-e","select c1,count(c1) from hivedemo.format_test1 group by c1"};

        CliDriver.main(cmd);

    }
}
