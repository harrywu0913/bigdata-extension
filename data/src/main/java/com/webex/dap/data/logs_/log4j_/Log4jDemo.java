package com.webex.dap.data.logs_.log4j_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jDemo {
    static Logger logger = LoggerFactory.getLogger(Log4jDemo.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.debug("debug");
    }
}
