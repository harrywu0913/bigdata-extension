package com.webex.dap;

import org.apache.sentry.SentryMain;

/**
 * Hello world!
 */
public class SentryMainDemo {
    public static void main(String[] args) throws Exception {
        SentryMain.main(new String[]{"--command","service","-conffile","/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/sentry-extension/src/main/resources/sentry-site.xml"});
    }
}
