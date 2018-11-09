package com.webex.dap.data.metrics_.yammer_.core_;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by harry on 8/7/18.
 */
public class Counter implements Metric {
    private final AtomicLong count;

    Counter() {
        this.count = new AtomicLong(0);
    }

    @Override
    public <T> void processWith(MetricProcessor<T> processor, MetricName name, T context) throws Exception {
        processor.processCounter(name, this, context);
    }

    public long count() {
        return count.get();
    }
}
