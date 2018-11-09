package com.webex.dap.data.logs_.common_;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonLogDemo {
    private static final Log logger = LogFactory.getLog(CommonLogDemo.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.debug("debug");
    }
}
