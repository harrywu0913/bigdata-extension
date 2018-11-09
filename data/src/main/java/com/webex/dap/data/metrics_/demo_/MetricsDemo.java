package com.webex.dap.data.metrics_.demo_;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.reporting.ConsoleReporter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by harry on 8/7/18.
 * <p>
 * http://ju.outofmemory.cn/entry/105544
 */
public class MetricsDemo {
    private List<String> stringList = new LinkedList<String>();
    Gauge<Integer> gauge = Metrics.newGauge(MetricsDemo.class, "list-size-gauge", new Gauge<Integer>() {
        @Override
        public Integer value() {
            return stringList.size();
        }
    });

    private static final Counter pendingJobs = Metrics.newCounter(MetricsDemo.class, "pending-jobs");

    private static final Meter requests = Metrics.newMeter(MetricsDemo.class,"requests","requests",TimeUnit.SECONDS);

    public void inputElement(String input) {
        stringList.add(input);
    }

    public static void main(String[] args) throws InterruptedException {
        ConsoleReporter.enable(1, TimeUnit.SECONDS);
        MetricsDemo learnGauge = new MetricsDemo();
        for (int i = 0; i < 10; i++) {
            learnGauge.inputElement(String.valueOf(i));

            pendingJobs.inc();

            requests.mark();

            Thread.sleep(1000);
        }
    }

}
