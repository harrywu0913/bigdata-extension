package com.webex.dap.data.metrics_.yammer_;

import com.webex.dap.data.metrics_.yammer_.core_.MetricsRegistry;

/**
 * Created by harry on 8/7/18.
 */
public class Metrics {
    private static final MetricsRegistry DEFAULT_REGISTRY = new MetricsRegistry();

    private static final Thread SHUTDOWN_HOOK = new Thread() {
        public void run() {

        }
    };

    static {
    }

    private Metrics(){

    }

    public static MetricsRegistry defaultRegistry() {
        return DEFAULT_REGISTRY;
    }
}
