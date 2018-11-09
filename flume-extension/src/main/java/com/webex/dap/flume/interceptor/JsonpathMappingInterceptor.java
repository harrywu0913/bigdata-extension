package com.webex.dap.flume.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.webex.dap.flume.interceptor.JsonpathMappingInterceptor.Constants.*;

/**
 * Created by harry on 5/25/18.
 */
public class JsonpathMappingInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(JsonpathMappingInterceptor.class);

    private String charset = CHARSET_DEFAULT;
    private boolean headerOverwrite = HEADER_OVERWRITE_DEFAULT;
    private boolean fieldValueLowerCase = FIELD_VALUE_LOWERCASE_DEFAULT;
    private List<String> fields;
    private Map<String, String> fieldsDefault;
    private Map<String, String> jsonpathsMappings;
    private Map<String, Map<String, String>> headermappings;
    private Set<String> merges;
    private boolean needMerge = false;

    private InterceptorCounter counter;

    public JsonpathMappingInterceptor() {
        if (counter == null) {
            counter = new InterceptorCounter(JsonpathMappingInterceptor.class.getSimpleName());
            counter.start();
        }
    }

    public JsonpathMappingInterceptor buildFields(List<String> fields) {
        this.fields = fields;
        return this;
    }

    public JsonpathMappingInterceptor buildFieldsDefault(Map<String, String> fieldsDefault) {
        this.fieldsDefault = fieldsDefault;
        return this;
    }

    public JsonpathMappingInterceptor buildJsonpathsMappings(Map<String, String> jsonpathsMappings) {
        this.jsonpathsMappings = jsonpathsMappings;
        return this;
    }

    public JsonpathMappingInterceptor buildHeadermappings(Map<String, Map<String, String>> headermappings) {
        this.headermappings = headermappings;
        return this;
    }

    public JsonpathMappingInterceptor buildMerges(Set<String> merges) {
        this.merges = merges;

        if (merges != null && merges.size() > 0) {
            this.needMerge = true;
        }
        return this;
    }

    public JsonpathMappingInterceptor buildHeaderOverwrite(boolean headerOverwrite) {
        this.headerOverwrite = headerOverwrite;
        return this;
    }

    public JsonpathMappingInterceptor buildFieldValueLowerCase(boolean fieldValueLowerCase) {
        this.fieldValueLowerCase = fieldValueLowerCase;
        return this;
    }

    public JsonpathMappingInterceptor buildCharset(String charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        long startTime = System.currentTimeMillis();
        try {
            String body = new String(event.getBody(), charset);
            Map<String, String> headers = event.getHeaders();

            JSONObject jsonObject = JSON.parseObject(body);

            StringBuilder key = new StringBuilder();
            boolean merge = false;
            for (String field : fields) {
                String value = null;
                try {
                    value = (String) JSONPath.eval(jsonObject, jsonpathsMappings.get(field));
                } catch (Exception e) {

                }

                if (StringUtils.isEmpty(value)) {
                    value = this.fieldsDefault.get(field);
                }
                if (fieldValueLowerCase) {
                    value = value.toLowerCase();
                }

//                if (headermappings.containsKey(field)) {
//                    Map<String, String> headerMapping = headermappings.get(field);
//
//                    if (headerMapping.containsKey(value)) {
//                        value = headerMapping.get(value);
//                    }
//                }

                value = value.replaceAll("[^a-z0-9._]", "_");

                headers.put(field, value);

                if (this.needMerge) {
                    if (key.length() == 0) {
                        key.append(value);
                    } else {
                        key.append(FIELDS_MERGE_KEY_SEPARATOR).append(value);
                    }
                    if (merges.contains(key.toString())) {
                        merge = true;
                        break;
                    }
                }
            }

            if (merge) {
                for (String field : fields) {
                    if (!headers.containsKey(field))
                        headers.put(field, this.fieldsDefault.get(field));
                }
            }

            key = null;
        } catch (Exception e) {
            Map<String, String> headers = event.getHeaders();
            for (String field : fields) {
                if (!headers.containsKey(field))
                    headers.put(field, this.fieldsDefault.get(field));
            }
//            logger.warn("Skipping event due to: unknown error.", e);
        }
        long endTime = System.currentTimeMillis();
        counter.setTimerInterceptorParse((endTime - startTime));
        counter.addAndGetInterceptorRecords(1);
        counter.addAndGetTotalTimerInterceptorParse((endTime - startTime));

//        if (logger.isDebugEnabled()){
        Map<String, String> headers = event.getHeaders();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println(header.getKey() + " -> " + header.getValue());
        }
//        }

        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        ArrayList intercepted = Lists.newArrayListWithCapacity(events.size());
        Iterator<Event> iterator = events.iterator();

        while (iterator.hasNext()) {
            Event event = iterator.next();
            Event interceptedEvent = this.intercept(event);
            if (interceptedEvent != null) {
                intercepted.add(interceptedEvent);
            }
        }
        return intercepted;
    }

    @Override
    public void close() {
        if (counter != null) {
            counter.stop();
        }
    }

    public static class Builder implements org.apache.flume.interceptor.Interceptor.Builder {
        private String charset = CHARSET_DEFAULT;
        private boolean headerOverwrite = HEADER_OVERWRITE_DEFAULT;
        private boolean fieldValueLowerCase = FIELD_VALUE_LOWERCASE_DEFAULT;

        private List<String> fields = new ArrayList<String>();
        private Map<String, String> fieldsDefault = new HashMap<String, String>();
        private Map<String, String> jsonpathsMappings = new HashMap<String, String>();
        private Map<String, Map<String, String>> headermappings = new HashMap<String, Map<String, String>>();
        private Set<String> merges = new HashSet<String>();

        @Override
        public Interceptor build() {
            return new JsonpathMappingInterceptor()
                    .buildFields(fields)
                    .buildFieldsDefault(fieldsDefault)
                    .buildJsonpathsMappings(jsonpathsMappings)
                    .buildHeadermappings(headermappings)
                    .buildMerges(merges)
                    .buildHeaderOverwrite(headerOverwrite)
                    .buildFieldValueLowerCase(fieldValueLowerCase)
                    .buildCharset(charset);
        }

        @Override
        public void configure(Context context) {
            if (logger.isInfoEnabled()) {
                logger.info("{}.configure() -> parameters: -> {}", JsonpathMappingInterceptor.Builder.class.getName(), context);
            }

            charset = context.getString(CHARSET, CHARSET_DEFAULT);
            headerOverwrite = context.getBoolean(HEADER_OVERWRITE, HEADER_OVERWRITE_DEFAULT);
            fieldValueLowerCase = context.getBoolean(FIELD_VALUE_LOWERCASE, FIELD_VALUE_LOWERCASE_DEFAULT);

//            String fieldParserOrder = context.getString(FIELDS_PARSE_ORDER);
//            if (StringUtils.isEmpty(fieldParserOrder)){
//                throw new IllegalArgumentException("parseorder must be configured");
//            }

//            fields.addAll(Arrays.asList(fieldParserOrder.split(",")));

            // parse fields
            ImmutableMap<String, String> fieldsMap = context.getSubProperties(FIELDS);
            if (MapUtils.isEmpty(fieldsMap)) {
                throw new IllegalArgumentException("field must be configured");
            }

            fields.addAll(fieldsMap.keySet());
            jsonpathsMappings.putAll(fieldsMap);

            // parse default
            ImmutableMap<String, String> defaultMap = context.getSubProperties(DEFAULTS);
            if (MapUtils.isEmpty(defaultMap)) {
                for (String field : fields) {
                    fieldsDefault.put(field, DEFAULTS_DEFAULT);
                }
            } else {
                if (defaultMap.size() != fields.size()) {
                    throw new IllegalArgumentException("size of defaults must be equal to size of fields");
                } else {
                    for (String field : fields) {
                        if (!defaultMap.containsKey(field))
                            throw new IllegalArgumentException("item of defaults must be equal to item of fields");
                        fieldsDefault.put(field, defaultMap.get(field));
                    }
                }
            }

            // parse merge
            ImmutableMap<String, String> mergeMap = context.getSubProperties(FIELDS_MERGE);
            if (MapUtils.isNotEmpty(mergeMap)) {
                if (mergeMap.containsKey(FIELDS_MERGE_PATTERN) && mergeMap.containsKey(FIELDS_MERGE_VALUE)) {
                    String patternStr = mergeMap.get(FIELDS_MERGE_PATTERN);
                    String valueStr = mergeMap.get(FIELDS_MERGE_VALUE);

                    String fieldSeparator = FIELDS_MERGE_FIELD_SEPARATOR_DEFAULT;
                    if (mergeMap.containsKey(FIELDS_MERGE_FIELD_SEPARATOR)) {
                        fieldSeparator = mergeMap.get(FIELDS_MERGE_FIELD_SEPARATOR);
                    }

                    String collectionsSeparator = FIELDS_MERGE_COLLECTIONS_SEPARATOR_DEFAULT;
                    if (mergeMap.containsKey(FIELDS_MERGE_COLLECTIONS_SEPARATOR)) {
                        collectionsSeparator = mergeMap.get(FIELDS_MERGE_COLLECTIONS_SEPARATOR);
                    }

                    String[] patternArray = patternStr.split(fieldSeparator);

                    if (ArrayUtils.isEmpty(patternArray) || patternArray.length > 1) {
                        throw new IllegalArgumentException("merge.pattern should configure as one value which split by " + fieldSeparator);
                    }

                    List<String> pattern = Arrays.asList(patternArray[0].split(collectionsSeparator));

                    if (CollectionUtils.isEmpty(pattern) || pattern.size() != fields.size()) {
                        throw new IllegalArgumentException("the field in merge.pattern must same as the field");
                    }
                    for (String field : fields) {
                        if (!pattern.contains(field))
                            throw new IllegalArgumentException("the field in merge.pattern must same as the field");
                    }

                    fields = pattern;

                    String[] valueArray = valueStr.split(fieldSeparator);
                    for (String value : valueArray) {
                        if (fieldValueLowerCase) {
                            value = value.toLowerCase();
                        }

                        String[] items = value.split(collectionsSeparator, fields.size());

                        if (items.length != fields.size()) {
                            throw new IllegalArgumentException("the length of each item in merge.value should be equals with the size of fields");
                        } else if (StringUtils.isNotEmpty(items[fields.size() - 1])) {
                            throw new IllegalArgumentException("the last value of item in merge.value should be empty");
                        }

                        StringBuilder key = new StringBuilder();
                        for (String item : items) {
                            if (StringUtils.isNotEmpty(item)) {
                                key.append(item).append(FIELDS_MERGE_KEY_SEPARATOR);
                            }
                        }

                        if (key.length() == 0) {
                            throw new IllegalArgumentException("item in merge.value must not be empty");
                        } else {
                            merges.add(key.substring(0, key.length() - 1));
                        }
                    }
                } else {
                    throw new IllegalArgumentException("merge.pattern and merge.value must be exists both");
                }
            }

            // parse mapping
//            for (String field : fields) {
//                ImmutableMap<String, String> mappingMap = context.getSubProperties(FIELDS_MAPPING + field + ".");
//                if (MapUtils.isNotEmpty(mappingMap)) {
//                    if (!headermappings.containsKey(field)){
//                        headermappings.put(field,new HashMap<String, String>());
//                    }
//
//                    headermappings.get(field).putAll(mappingMap);
//                }
//            }
        }
    }

    public static class Constants {
        public static String CHARSET = "charset";
        public static String CHARSET_DEFAULT = "UTF-8";

        public static String FIELDS = "field.";
        public static String DEFAULTS = "default.";
        public static String DEFAULTS_DEFAULT = "default";
        public static String FIELDS_MAPPING = "mapping.";
        //        public static String FIELDS_PARSE_ORDER = "parseorder";
        public static String FIELDS_MERGE = "merge.";
        public static String FIELDS_MERGE_PATTERN = "pattern";
        public static String FIELDS_MERGE_VALUE = "value";
        public static String FIELDS_MERGE_FIELD_SEPARATOR = "field.separator";
        public static String FIELDS_MERGE_FIELD_SEPARATOR_DEFAULT = ",";
        public static String FIELDS_MERGE_COLLECTIONS_SEPARATOR = "collection.separator";
        public static String FIELDS_MERGE_COLLECTIONS_SEPARATOR_DEFAULT = ":";

        public static String HEADER_OVERWRITE = "headeroverwrite";
        public static boolean HEADER_OVERWRITE_DEFAULT = true;

        public static String FIELD_VALUE_LOWERCASE = "lowercase";
        public static boolean FIELD_VALUE_LOWERCASE_DEFAULT = true;

        public static String FIELDS_MERGE_KEY_SEPARATOR = "_";
    }
}
