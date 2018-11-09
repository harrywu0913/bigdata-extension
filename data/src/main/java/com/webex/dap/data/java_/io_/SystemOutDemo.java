package com.webex.dap.data.java_.io_;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SystemOutDemo {
    public static void main(String[] args) throws Exception {
        System.out.println(new String("我们".getBytes(),"GBK"));
        System.out.println(new String("我们".getBytes("GBK")));
    }
}
