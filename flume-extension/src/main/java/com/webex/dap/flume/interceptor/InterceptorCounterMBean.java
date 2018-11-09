package com.webex.dap.flume.interceptor;

/**
 * Created by harry on 8/3/18.
 */
public interface InterceptorCounterMBean {
    public long getTimerInterceptorParseTotal();

    public long getTimerInterceptorParse();

    public long getInterceptorRecords();
}
