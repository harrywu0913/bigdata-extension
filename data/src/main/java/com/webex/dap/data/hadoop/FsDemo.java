package com.webex.dap.data.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;

/**
 * Created by harry on 6/8/18.
 */
public class FsDemo {
    public static void main(String[] args) throws IOException {
        System.setProperty("HADOOP_USER_NAME", "hewewu");

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://nameservice1x");
        conf.set("dfs.nameservices", "nameservice1x");
        conf.set("dfs.ha.namenodes.nameservice1x", "namenode109xxx,namenode141xxx");
        conf.set("dfs.namenode.rpc-address.nameservice1x.namenode109xxx", "rphf1hmn001.qa.webex.com:8020");
        conf.set("dfs.namenode.rpc-address.nameservice1x.namenode141xxx", "rphf1hmn002.qa.webex.com:8020");
        conf.set("dfs.client.failover.proxy.provider.nameservice1x",
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        final FileSystem fs = FileSystem.get(conf);

//        FileStatus[] fileStatuses = fs.listStatus(new Path("/tmp/install.log"));
//        FileStatus[] fileStatuses = fs.globStatus(new Path("/kafka-bak/*/*2018*"));


//        for (FileStatus fileStatus : fileStatuses) {
//            System.out.println(fileStatus);
//
//            BlockLocation[] locations = fs.getFileBlockLocations(fileStatus,0,100);
//            System.out.println(locations);
//        }

//        FSDataInputStream input = fs.open(new Path("/tmp/install.log"));
//
//        byte[] content = new byte[1024];
//        int count = 0;
//        while((count = input.read(content)) > 0){
//
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                FSDataOutputStream out = null;
                try {
                    out = fs.create(new Path("/tmp/write_test_8"), true, 4096, (short) 3, 1024 * 1024);
                    for (int i = 0; i < 1024; i++) {
                        out.write(i);
                    }
                } catch (IOException e) {
                    System.out.println("======= t1 error ======= ");
                    e.printStackTrace();
                    System.out.println("======= t1 error ======= ");
                }

//                out.close();

                System.out.println("t1 start");
                try {
                    Thread.sleep(1000 * 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("t1 end");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FSDataOutputStream out = null;
                try {
                    out = fs.create(new Path("/tmp/write_test_8"), true, 4096, (short) 3, 1024 * 1024);
                    for (int i = 0; i < 1024; i++) {
                        out.write(i);
                    }
                } catch (IOException e) {
                    System.out.println("======= t2 error ======= ");
                    e.printStackTrace();
                    System.out.println("======= t2 error ======= ");
                }

//                out.close();

                System.out.println("t2 start");
                try {
                    Thread.sleep(1000 * 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("t2 end");
            }
        });
        //.start();


//
//        fs.mkdirs(new Path("/tmp/t1"));
    }
}
