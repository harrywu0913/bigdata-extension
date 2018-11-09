package com.webex.dap.data.java_.jmx_.example_1;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by harry on 9/19/18.
 */
public class HelloAgent {
    public static void main(String[] args) throws JMException, Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("jmxBean:name=hello");
        //create mbean and register mbean
        server.registerMBean(new Hello(), helloName);
        Thread.sleep(60 * 60 * 1000);
    }
}
