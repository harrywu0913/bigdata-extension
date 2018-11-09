package com.webex.dap.data.metrics_.yammer_.reporting_;

import com.webex.dap.data.metrics_.yammer_.core_.MetricsRegistry;

/**
 * Created by harry on 8/7/18.
 */
public abstract class AbstractReporter {
    private final MetricsRegistry metricsRegistry;

    protected AbstractReporter(MetricsRegistry registry) {
        this.metricsRegistry = registry;
    }

    public void shutdown() {
        // nothing to do here
    }

    protected MetricsRegistry getMetricsRegistry() {
        return metricsRegistry;
    }
}