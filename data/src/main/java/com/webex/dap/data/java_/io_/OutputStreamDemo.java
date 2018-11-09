package com.webex.dap.data.java_.io_;

import java.io.*;

public class OutputStreamDemo {
    public static void main(String[] args) throws Exception {
        OutputStream out = new FileOutputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/utf8_out_2.txt"));
//        out.write("我们".getBytes("UTF-8"));
        out.write("123".getBytes("GBK"));
        out.close();
    }
}
