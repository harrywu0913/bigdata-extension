package com.webex.dap.data.metrics_.yammer_.core_;


import com.yammer.metrics.core.MetricsRegistryListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by harry on 8/7/18.
 */
public class MetricsRegistry {
    private static final int EXPECTED_METRIC_COUNT = 1024;
    private final Clock clock;
    private final ConcurrentMap<MetricName, Metric> metrics;
    private final ThreadPools threadPools;
    private final List<MetricsRegistryListener> listeners;

    public MetricsRegistry() {
        this(Clock.defaultClock());
    }

    public MetricsRegistry(Clock clock) {
        this.clock = clock;
        this.metrics = new ConcurrentHashMap<MetricName, Metric>(EXPECTED_METRIC_COUNT);
        this.threadPools = new ThreadPools();
        this.listeners = new CopyOnWriteArrayList<MetricsRegistryListener>();
    }

    public ScheduledExecutorService newScheduledThreadPool(int poolSize, String name) {
        return threadPools.newScheduledThreadPool(poolSize, name);
    }

    public SortedMap<String, SortedMap<MetricName, Metric>> groupedMetrics(MetricPredicate predicate) {
        final SortedMap<String, SortedMap<MetricName, Metric>> groups = new TreeMap<String, SortedMap<MetricName, Metric>>();

        for (Map.Entry<MetricName, Metric> entry : metrics.entrySet()) {
            final String qualifiedTypeName = entry.getKey().getGroup() + "." + entry.getKey().getType();

            if (predicate.matches(entry.getKey(),entry.getValue())){
                final String scopedName;
                if (entry.getKey().hasScope()){
                    scopedName = qualifiedTypeName + "." + entry.getKey().getScope();
                }else{
                    scopedName = qualifiedTypeName;
                }

                SortedMap<MetricName,Metric> group = groups.get(scopedName);
                if (group == null){
                    group = new TreeMap<MetricName,Metric>();
                    groups.put(scopedName,group);
                }

                group.put(entry.getKey(),entry.getValue());
            }
        }

        return Collections.unmodifiableSortedMap(groups);
    }
}
