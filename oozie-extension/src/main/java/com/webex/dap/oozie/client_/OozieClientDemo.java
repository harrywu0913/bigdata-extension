package com.webex.dap.oozie.client_;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowJob;

import java.util.List;

public class OozieClientDemo {
    public static void main(String[] args) throws OozieClientException {
        OozieClient oozieClient = new OozieClient("http://rphf93rpd001.qa.webex.com:11000/oozie");

        WorkflowJob workflowJob = oozieClient.getJobInfo("0000033-180927081420146-oozie-oozi-W");
        List<WorkflowAction> workflowActions = workflowJob.getActions();

        for (WorkflowAction action : workflowActions) {
            System.out.println(action);
        }
    }
}
