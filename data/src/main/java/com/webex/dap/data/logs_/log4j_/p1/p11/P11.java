package com.webex.dap.data.logs_.log4j_.p1.p11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P11 {
    static Logger logger = LoggerFactory.getLogger(P11.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.debug("debug");
    }
}
