package com.webex.dap.data.quartz_.demo_1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * Created by harry on 9/18/18.
 */
public class JobImpl implements Job {
    public void execute(JobExecutionContext context) {
        System.out.println("job impl running");
    }
}