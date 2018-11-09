package com.webex.dap.hive.inspector_.list_;

import org.apache.hadoop.hive.cli.CliDriver;

public class ArrayMain {
    public static void main(String[] args) throws Exception {
        String[] cmd = new String[]{"hive", "-e", "select * from hivedemo.array_test1"};
        CliDriver.main(cmd);
    }
}
