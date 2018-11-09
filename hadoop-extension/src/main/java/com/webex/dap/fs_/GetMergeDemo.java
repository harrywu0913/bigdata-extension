package com.webex.dap.fs_;

import org.apache.hadoop.fs.FsShell;

public class GetMergeDemo {
    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "hewewu");

//        System.setProperty("fs.defaultFS", "hdfs://nameservice1x");
//        System.setProperty("dfs.nameservices", "nameservice1x");
//        System.setProperty("dfs.ha.namenodes.nameservice1x", "namenode109xxx,namenode141xxx");
//        System.setProperty("dfs.namenode.rpc-address.nameservice1x.namenode109xxx", "rphf1hmn001.qa.webex.com:8020");
//        System.setProperty("dfs.namenode.rpc-address.nameservice1x.namenode141xxx", "rphf1hmn002.qa.webex.com:8020");
//        System.setProperty("dfs.client.failover.proxy.provider.nameservice1x",
//                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");


        FsShell.main(new String[]{"-getmerge","/kafka/logstash_meeting_hdfs/mjssvr/joinmeeting-meeting/day=2018-10-28/hfqa1_logstash_meeting_hdfs-rphf1kaf003.qa.webex.com.1540710455068.lzo","/kafka/logstash_meeting_hdfs/mjssvr/joinmeeting-meeting/day=2018-10-28/hfqa1_logstash_meeting_hdfs-rphf1kaf003.qa.webex.com.1540714847806.lzo","/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/hadoop-extension/src/main/resources/getmerge_.lzo"});
    }
}
