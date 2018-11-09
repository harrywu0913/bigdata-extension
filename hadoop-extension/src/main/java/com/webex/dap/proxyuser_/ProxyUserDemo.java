package com.webex.dap.proxyuser_;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.security.PrivilegedExceptionAction;

public class ProxyUserDemo {
    public static void main(String[] args) throws Exception {
//        System.setProperty("HADOOP_USER_NAME", "oozie");

//        System.setProperty("fs.defaultFS", "hdfs://nameservice1x");
//        System.setProperty("dfs.nameservices", "nameservice1x");
//        System.setProperty("dfs.ha.namenodes.nameservice1x", "namenode109xxx,namenode141xxx");
//        System.setProperty("dfs.namenode.rpc-address.nameservice1x.namenode109xxx", "rphf1hmn001.qa.webex.com:8020");
//        System.setProperty("dfs.namenode.rpc-address.nameservice1x.namenode141xxx", "rphf1hmn002.qa.webex.com:8020");
//        System.setProperty("dfs.client.failover.proxy.provider.nameservice1x",
//                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        final Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://nameservice1x");
        conf.set("dfs.nameservices", "nameservice1x");
        conf.set("dfs.ha.namenodes.nameservice1x", "namenode109xxx,namenode141xxx");
        conf.set("dfs.namenode.rpc-address.nameservice1x.namenode109xxx", "rphf1hmn001.qa.webex.com:8020");
        conf.set("dfs.namenode.rpc-address.nameservice1x.namenode141xxx", "rphf1hmn002.qa.webex.com:8020");
        conf.set("dfs.client.failover.proxy.provider.nameservice1x", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        UserGroupInformation superuser = UserGroupInformation.getCurrentUser();
        UserGroupInformation proxyuser = UserGroupInformation.createProxyUser("proxyuser", superuser);

        proxyuser.doAs(new PrivilegedExceptionAction<Object>() {
            @Override
            public Object run() throws Exception {
                FileSystem fs = FileSystem.get(conf);
                fs.mkdirs(new Path("/tmp/proxyuser_dir2"));
                return null;
            }
        });

        //hadoop.proxyuser.$superuser.hosts     ->  配置该superuser运行代理的访问的主机
        //hadoop.proxyuser.$superuser.groups    ->  配置该superuser运行代理的用户所属组
        //hadoop.proxyuser.$superuser.users     ->  配置该superuser运行代理的用户
        // hosts 必须配置， users/groups 至少配置一个
    }
}
