package com.webex.dap.spark.history;

import org.apache.spark.deploy.history.HistoryServer;

/**
 * Created by harry on 6/4/18.
 */
public class HistoryServerMain {
    public static void main(String[] args) {
        HistoryServer.main(new String[]{"-d","/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/spark-extension/src/main/resources/spark-events"});
    }
}
