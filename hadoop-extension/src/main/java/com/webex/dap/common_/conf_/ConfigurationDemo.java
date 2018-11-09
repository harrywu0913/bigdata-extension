package com.webex.dap.common_.conf_;

import org.apache.hadoop.conf.Configuration;

/**
 * Created by harry on 9/4/18.
 */
public class ConfigurationDemo {
    public static void main(String[] args){
        Configuration configuration = new Configuration();
        System.out.println(configuration);

        configuration.set("x","x");
        configuration.get("x");
    }
}
