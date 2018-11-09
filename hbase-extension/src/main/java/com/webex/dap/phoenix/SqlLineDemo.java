package com.webex.dap.phoenix;

import sqlline.SqlLine;

import java.io.IOException;

/**
 * Created by harry on 8/21/18.
 */
public class SqlLineDemo {
    public static void main(String[] args) throws IOException {
        SqlLine.main(new String[]{"-d", "org.apache.phoenix.jdbc.PhoenixDriver", "-u", "jdbc:phoenix:rphf1hmn001.qa.webex.com:2181/hbase", "-n", "none", "-p", "none", " --incremental", "false", "-e", "!outputformat csv"});
    }
}
