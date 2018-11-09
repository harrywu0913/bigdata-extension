package com.webex.dap.hive.metastore_;

import org.apache.hadoop.hive.metastore.HiveMetaStore;

/**
 * Created by harry on 5/23/18.
 */
public class MetastoreMain {
    public static void main(String[] args) throws Throwable {
        HiveMetaStore.main(new String[]{"9083"});
    }
}
