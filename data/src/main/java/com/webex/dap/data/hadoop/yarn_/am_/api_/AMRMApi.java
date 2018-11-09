package com.webex.dap.data.hadoop.yarn_.am_.api_;

import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.util.List;

public class AMRMApi {
    public static void main(String[] args) {
        YarnConfiguration yarnConfiguration = new YarnConfiguration();

        yarnConfiguration.set("yarn.resourcemanager.ha.enabled", "true");

        yarnConfiguration.set("yarn.resourcemanager.ha.rm-ids", "rm124,rm125");
        yarnConfiguration.set("yarn.resourcemanager.address.rm124", "rphf1hmn001.qa.webex.com:8032");
        yarnConfiguration.set("yarn.resourcemanager.address.rm125", "rphf1hmn002.qa.webex.com:8032");

        yarnConfiguration.set("yarn.resourcemanager.scheduler.address.rm124", "rphf1hmn001.qa.webex.com:8030");
        yarnConfiguration.set("yarn.resourcemanager.scheduler.address.rm125", "rphf1hmn002.qa.webex.com:8030");

        AMRMClient amrmClient = AMRMClient.createAMRMClient();
        amrmClient.init(yarnConfiguration);

        amrmClient.start();

        List resourceRequestInfoList = amrmClient.getMatchingRequests(Priority.newInstance(1), "*", Resource.newInstance(1024 + 384, 1));

        System.out.println(resourceRequestInfoList);
    }
}
