package com.webex.dap.data.metrics_.yammer_.reporting_;

import com.webex.dap.data.metrics_.yammer_.core_.MetricsRegistry;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by harry on 8/7/18.
 */
public abstract class AbstractPollingReporter extends AbstractReporter implements Runnable{
    private final ScheduledExecutorService executor;

    protected AbstractPollingReporter(MetricsRegistry registry,String name){
        super(registry);
        this.executor = registry.newScheduledThreadPool(1,name);
    }

    public void start(long period, TimeUnit unit){
        executor.scheduleWithFixedDelay(this,period,period,unit);
    }
}
