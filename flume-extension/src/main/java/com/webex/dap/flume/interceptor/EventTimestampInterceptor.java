package com.webex.dap.flume.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class   EventTimestampInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(EventTimestampInterceptor.class);
    private String charset = "UTF-8";

    private final boolean preserveExisting;
    private final List<String> formats;
    private final String pattern;
    private List<DateTimeFormatter> formatters;

    private EventTimestampInterceptor(boolean preserveExisting, List<String> formats, String pattern) {
        this.preserveExisting = preserveExisting;
        this.formats = formats;
        this.pattern = pattern;
    }

    @Override
    public void initialize() {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(formats) && formats.size() > 0, "Must configure with a valid format");
        formatters = new ArrayList<DateTimeFormatter>();
        for (String format : this.formats) {
            formatters.add(DateTimeFormat.forPattern(format));
        }
    }

    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        if (preserveExisting && headers.containsKey(Constants.TIMESTAMP)) {
            //
        } else {
            String now = Long.toString(System.currentTimeMillis());
            try {
                JSONObject jsonObject = JSON.parseObject(new String(event.getBody(), charset));
                String timestampValue = (String) JSONPath.eval(jsonObject, pattern);

                if (StringUtils.isNotEmpty(timestampValue)) {
                    for (DateTimeFormatter formatter : this.formatters) {
                        try {
                            DateTime dateTime = formatter.parseDateTime(timestampValue);
                            now = Long.toString(dateTime.getMillis());
                            break;
                        } catch (Exception e) {
                            //Ignore
                        }
                    }
                }
            } catch (Exception e) {
                //Ignore
//                logger.error("Parse failed in event:{} with format:{}", event, this.formats);
            }

            headers.put(Constants.TIMESTAMP, now);
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    @Override
    public void close() {
        // no-op
    }

    public static class Builder implements Interceptor.Builder {
        private boolean preserveExisting = Constants.PRESERVE_DEFAULT;
        private List<String> formats;
        private String pattern;

        @Override
        public Interceptor build() {
            return new EventTimestampInterceptor(preserveExisting, formats, pattern);
        }

        @Override
        public void configure(Context context) {
            preserveExisting = context.getBoolean(Constants.PRESERVE, Constants.PRESERVE_DEFAULT);
            pattern = context.getString(Constants.TIMESTAMP_PATTERN);

            Preconditions.checkArgument(!StringUtils.isEmpty(pattern), "Must configure with a valid pattern");

            String formatValue = context.getString(Constants.TIMESTAMP_FORMAT);
            Preconditions.checkArgument(!StringUtils.isEmpty(formatValue), "Must configure with a valid format");

            String collectionSeparator = context.getString(Constants.FIELDS_COLLECTIONS_SEPARATOR, Constants.FIELDS_COLLECTIONS_SEPARATOR_DEFAULT);
            formats = Arrays.asList(formatValue.split(collectionSeparator));
            Preconditions.checkArgument(!CollectionUtils.isEmpty(formats) && formats.size() > 0, "Must configure with a valid pattern");
        }
    }

    public static class Constants {
        public static String TIMESTAMP = "timestamp";
        public static String TIMESTAMP_PATTERN = "pattern";
        public static String TIMESTAMP_PATTERN_DEFAULT = "$.message.timestamp";
        public static String PRESERVE = "preserveExisting";
        public static boolean PRESERVE_DEFAULT = false;
        public static String TIMESTAMP_FORMAT = "format";

        public static String FIELDS_COLLECTIONS_SEPARATOR = "collections.separator";
        public static String FIELDS_COLLECTIONS_SEPARATOR_DEFAULT = "\\|";
    }
}
