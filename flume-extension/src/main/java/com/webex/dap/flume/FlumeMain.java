package com.webex.dap.flume;

import com.google.common.collect.Maps;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.formatter.output.BucketPath;
import org.apache.flume.interceptor.RegexExtractorInterceptor;
import org.apache.flume.node.Application;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;

/**
 * Created by harry on 6/6/18.
 */
public class FlumeMain {
    public static void main(String[] args) {
//        String x = "x:x#x$x%x^x&x*x x(x)x@x+x_xTxY";
//        System.out.println(x.replaceAll("[^a-z0-9._]", "_"));
//        Application.main(new String[]{"-n", "tier1", "-f", "/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/flume-extension/src/main/resources/flume.conf"});

//        testRegexExtractor();


//        Map<String,String> headers = Maps.newHashMap();
//        BucketPath.escapeString("/sdf/%y-%m-%d",headers);

//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//
//        DateTime time =formatter.parseDateTime("2018-08-19T23:56:12Z");
//        System.out.println(time.getHourOfDay());
//        System.out.println(time.getMillis());
//
//        time =formatter.parseDateTime("2018-08-19T23:56:12Z");
//        System.out.println(time.getHourOfDay());
//        System.out.println(time.getMillis());

        System.out.println("yyyy-MM-dd'T'HH:mm:ss'Z'|yyyy-MM-dd'T'HH:mm:ss.sss'Z'".split("\\|").length);
    }

    public static void testRegexExtractor(){
        RegexExtractorInterceptor.Builder builder = new RegexExtractorInterceptor.Builder();
//
        Map<String, String> paramters = Maps.newHashMap();
//
        paramters.put("regex","(\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d)");
        paramters.put("serializers","s1");
        paramters.put("serializers.s1.type","org.apache.flume.interceptor.RegexExtractorInterceptorMillisSerializer");
        paramters.put("serializers.s1.name","timestamp");
        paramters.put("serializers.s1.pattern","yyyy-MM-dd HH:mm:ss");
//        paramters.put("default.type","no_type");
//        paramters.put("default.featureName","no_featurename");
//        paramters.put("default.metricsName","no_metricsname");

        Context context = new Context(paramters);
        builder.configure(context);
        RegexExtractorInterceptor interceptor = (RegexExtractorInterceptor) builder.build();

        String xx = "a=b,c=2,f=(2018-09-28 10:07:23),e=f";
        Event e = EventBuilder.withBody(xx.getBytes());
        interceptor.intercept(e);
    }
}

