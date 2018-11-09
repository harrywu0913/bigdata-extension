package com.webex.dap.data.metrics_.yammer_.core_;

import java.util.EventListener;

/**
 * Created by harry on 8/7/18.
 */
public interface MetricsRegistryListener extends EventListener {
    void onMetricAdded(MetricName name, Metric metric);
    void onMetricRemoved(MetricName name);
}
