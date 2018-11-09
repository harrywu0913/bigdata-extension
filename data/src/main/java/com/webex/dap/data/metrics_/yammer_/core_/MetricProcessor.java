package com.webex.dap.data.metrics_.yammer_.core_;

/**
 * Created by harry on 8/7/18.
 */
public interface MetricProcessor<T> {
    void processGauge(MetricName name, Gauge<?> gauge, T context) throws Exception;

    void processCounter(MetricName name, Counter counter, T context) throws Exception;
}
