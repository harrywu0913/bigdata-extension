package com.webex.dap.flume.interceptor;

import org.apache.flume.instrumentation.MonitoredCounterGroup;

/**
 * Created by harry on 8/2/18.
 */
public class InterceptorCounter extends MonitoredCounterGroup implements InterceptorCounterMBean{
    private static final String INTERCEPTOR_RECORDS = "interceptor.parse.records";
    private static final String TIMER_INTERCEPTOR_PARSE = "interceptor.parse.time";
    private static final String TOTAL_TIMER_INTERCEPTOR_PARSE = "interceptor.parse.time.total";

    private static final String[] ATTRIBUTES = {INTERCEPTOR_RECORDS,TIMER_INTERCEPTOR_PARSE,TOTAL_TIMER_INTERCEPTOR_PARSE};

    public InterceptorCounter(String name) {
        super(Type.INTERCEPTOR, name, ATTRIBUTES);
    }

    public void setTimerInterceptorParse(long delta) {
        set(TIMER_INTERCEPTOR_PARSE, delta);
    }

    public long addAndGetTotalTimerInterceptorParse(long delta) {
        return addAndGet(TOTAL_TIMER_INTERCEPTOR_PARSE,delta);
    }

    public long addAndGetInterceptorRecords(long delta) {
        return addAndGet(INTERCEPTOR_RECORDS,delta);
    }


    @Override
    public long getTimerInterceptorParseTotal() {
        return get(TOTAL_TIMER_INTERCEPTOR_PARSE);
    }

    @Override
    public long getTimerInterceptorParse() {
        return get(TIMER_INTERCEPTOR_PARSE);
    }

    @Override
    public long getInterceptorRecords() {
        return get(INTERCEPTOR_RECORDS);
    }
}
