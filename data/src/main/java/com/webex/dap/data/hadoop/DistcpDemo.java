package com.webex.dap.data.hadoop;

import org.apache.hadoop.conf.Configuration;

public class DistcpDemo {
    public static void main(String[] args){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://nameservice1x");
        conf.set("dfs.nameservices", "nameservice1x");
        conf.set("dfs.ha.namenodes.nameservice1x", "namenode109xxx,namenode141xxx");
        conf.set("dfs.namenode.rpc-address.nameservice1x.namenode109xxx", "rphf1hmn001.qa.webex.com:8020");
        conf.set("dfs.namenode.rpc-address.nameservice1x.namenode141xxx", "rphf1hmn002.qa.webex.com:8020");
        conf.set("dfs.client.failover.proxy.provider.nameservice1x",
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

    }
}
