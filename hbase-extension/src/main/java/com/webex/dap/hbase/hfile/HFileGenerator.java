package com.webex.dap.hbase.hfile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by harry on 6/19/18.
 */
public class HFileGenerator {
    public static void main(String[] args){
        String tablename = "taglog";
        byte[] family = Bytes.toBytes("logs");

        //
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.master","");


        String outputdir = "";
        Path dir = new Path(outputdir);
        Path familydir = new Path(outputdir,Bytes.toString(family));

        System.out.println(Bytes.toString(Bytes.toBytes("cf")));


//        StoreFile.Writer writer = new StoreFile.WriterBuilder(conf,new CacheConfig(conf),fs,b)
    }
}
