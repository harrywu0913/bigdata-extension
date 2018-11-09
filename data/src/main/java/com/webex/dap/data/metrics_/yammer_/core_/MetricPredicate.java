package com.webex.dap.data.metrics_.yammer_.core_;

/**
 * Created by harry on 8/7/18.
 */
public interface MetricPredicate {
    MetricPredicate ALL = new MetricPredicate(){
        @Override
        public boolean matches(MetricName name, Metric metric) {
            return true;
        }
    };

    boolean matches(MetricName name, Metric metric);
}
