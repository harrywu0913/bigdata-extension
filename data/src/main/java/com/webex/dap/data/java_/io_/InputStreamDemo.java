package com.webex.dap.data.java_.io_;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/*
    InputStream中字节编码取决于文件本身的编码
 */
public class InputStreamDemo{
    public static void main(String[] args) throws Exception {
        InputStream input = new FileInputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/gbk.txt"));

        byte[] contents = new byte[1024];

        int length = 0;
        while ((length = input.read(contents)) != -1){
            System.out.println(new String(contents,0,length));
//            System.out.println(new String(contents,0,length,"UTF-8"));
//            System.out.println(new String(contents,0,length,"GBK"));
        }

        input.close();
    }
}
