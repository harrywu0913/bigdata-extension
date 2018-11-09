package com.webex.dap.flume.interceptor;

import com.google.common.collect.Maps;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * JsonpathMappingInterceptor Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 18, 2018</pre>
 */
public class JsonpathMappingInterceptorTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: initialize()
     */
    @Test
    public void testInitialize() throws Exception {
    }

    /**
     * Method: intercept(Event event)
     */
    @Test
    public void testInterceptEvent_1() throws Exception {
        JsonpathMappingInterceptor.Builder builder = new JsonpathMappingInterceptor.Builder();
//
        Map<String, String> paramters = Maps.newHashMap();
//
        paramters.put("field.type","$.type");
        paramters.put("field.featureName","$.message.featureName");
        paramters.put("field.metricName","$.message.metricName");
//        paramters.put("default.type","no_type");
//        paramters.put("default.featureName","no_featurename");
//        paramters.put("default.metricsName","no_metricsname");

        Context context = new Context(paramters);
        builder.configure(context);
        JsonpathMappingInterceptor interceptor = (JsonpathMappingInterceptor) builder.build();

        String xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        Event e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();
    }

    @Test
    public void testInterceptEvent_2() throws Exception {
        JsonpathMappingInterceptor.Builder builder = new JsonpathMappingInterceptor.Builder();
//
        Map<String, String> paramters = Maps.newHashMap();
//
        paramters.put("field.type","$.type");
        paramters.put("field.featureName","$.message.featureName");
        paramters.put("field.metricName","$.message.metricName");
        paramters.put("default.type","no_type");
        paramters.put("default.featureName","no_featurename");
        paramters.put("default.metricName","no_metricname");

        Context context = new Context(paramters);
        builder.configure(context);
        JsonpathMappingInterceptor interceptor = (JsonpathMappingInterceptor) builder.build();

        String xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        Event e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();
    }

    @Test
    public void testInterceptEvent_3() throws Exception {
        JsonpathMappingInterceptor.Builder builder = new JsonpathMappingInterceptor.Builder();
//
        Map<String, String> paramters = Maps.newHashMap();
//
        paramters.put("field.type","$.type");
        paramters.put("field.featureName","$.message.featureName");
        paramters.put("field.metricName","$.message.metricName");
        paramters.put("default.type","no_type");
        paramters.put("default.featureName","no_featurename");
        paramters.put("default.metricName","no_metricname");

        paramters.put("merge.pattern","type:metricName:featureName");

        paramters.put("merge.value","j2eeapp:m:,xxj2eeappxx:mx:,xxj2eeappxx:m:");

        paramters.put("merge.field.separator",",");
        paramters.put("merge.collection.separator",":");

        Context context = new Context(paramters);
        builder.configure(context);
        JsonpathMappingInterceptor interceptor = (JsonpathMappingInterceptor) builder.build();

        String xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        Event e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"metricName\": \"mx\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"xxj2eeappxx\",\"message\": {\"featureName\": \"fx\",\"metricName\": \"mx\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"j2eeapp\",\"message\": {\"featureName\": \"f\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();

        xx = "{\"type\": \"xxj2eeappxx\",\"message\": {\"featureName\": \"f\",\"metricName\": \"m\"}}";
        e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);

        System.out.println();
    }

    /**
     * Method: build()
     */
    @Test
    public void testBuild() throws Exception {
    }

    /**
     * Method: configure(Context context)
     */
    @Test
    public void testConfigure() throws Exception {
    }


} 
