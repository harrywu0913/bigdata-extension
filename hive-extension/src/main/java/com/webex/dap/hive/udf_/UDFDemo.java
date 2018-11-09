package com.webex.dap.hive.udf_;

import org.apache.hadoop.hive.cli.CliDriver;

public class UDFDemo {
    public static void main(String[] args) throws Exception {
        String[] cmd = new String[]{"hive", "-e", "select concat(col1,\"-\",\"X\",\"-\",\"Y\") as col1,col2,col3 from hivedemo.array_test1"};
        CliDriver.main(cmd);
    }
}
