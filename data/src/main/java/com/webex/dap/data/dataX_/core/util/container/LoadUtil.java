package com.webex.dap.data.dataX_.core.util.container;

import com.webex.dap.data.dataX_.core.util.Configuration;

public class LoadUtil {
    private LoadUtil() {
    }

    private enum ContainerType {
        Job("Job"), Task("Task");
        private String type;

        private ContainerType(String type) {
            this.type = type;
        }

        public String value() {
            return type;
        }
    }

    public static void bind(Configuration allConf) {

    }
}
