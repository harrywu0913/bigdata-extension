package com.webex.dap.data.metrics_.yammer_.core_;

/**
 * Created by harry on 8/7/18.
 */
public abstract class Gauge<T> implements Metric {

    public abstract T value();

    @Override
    public <U> void processWith(MetricProcessor<U> processor, MetricName name, U context) throws Exception {
        processor.processGauge(name, this, context);
    }
}
