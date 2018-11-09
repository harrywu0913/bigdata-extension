package com.webex.dap;

import org.apache.sqoop.Sqoop;

/**
 * Hello world!
 */
public class SqoopMain {
    public static void main(String[] args) {
//        System.setProperty("SQOOP_CONF_DIR","/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/sqoop-extension/src/main/resources/conf");

//        Sqoop.main("eval##--connect##jdbc:mysql://10.224.243.124:3306/##--username##x##--password##x##--query##\"select * from test\"".split("##"));


//        Sqoop.main("import --connect jdbc:mysql://10.224.239.16:3306/test --username root --password 2012cisco2012 --table test_int --target-dir /tmp/test_int".split(" "));
        Sqoop.main("import --connect jdbc:mysql://10.224.239.16:3306/test --null-string ns --null-non-string nns --username root --password 2012cisco2012 --table test_int --target-dir /tmp/test_int".split(" "));
    }
}
