package com.webex.dap.flume.interceptor;

import com.google.common.collect.Maps;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EventTimestampInterceptorTest {

    @Test
    public void intercept() {
        EventTimestampInterceptor.Builder builder = new EventTimestampInterceptor.Builder();

        Map<String, String> paramters = Maps.newHashMap();
        paramters.put("pattern","$.message.timestamp");
        paramters.put("format","yyyy-MM-dd'T'HH:mm:ss'Z'|yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Context context = new Context(paramters);
        builder.configure(context);
        EventTimestampInterceptor interceptor = (EventTimestampInterceptor) builder.build();

        interceptor.initialize();

        String xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        Event e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println(e.getHeaders().get("timestamp"));

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"timestamp\": \"f\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println(e.getHeaders().get("timestamp"));

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"timestamp\": \"2018-09-29T22:22:22Z\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println(e.getHeaders().get("timestamp"));

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"timestamp\": \"2018-09-29T22:22:22.023Z\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println(e.getHeaders().get("timestamp"));
    }
}