package com.webex.dap.data.metrics_.yammer_.core_;

/**
 * Created by harry on 8/7/18.
 */
public interface Metric {
    <T> void processWith(MetricProcessor<T> processor, MetricName name, T context) throws Exception;
}
