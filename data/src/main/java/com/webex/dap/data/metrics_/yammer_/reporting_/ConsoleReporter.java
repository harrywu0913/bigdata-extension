package com.webex.dap.data.metrics_.yammer_.reporting_;

import com.webex.dap.data.metrics_.yammer_.Metrics;
import com.webex.dap.data.metrics_.yammer_.core_.*;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by harry on 8/7/18.
 */
public class ConsoleReporter extends AbstractPollingReporter implements Runnable, MetricProcessor<PrintStream> {
    private static final int CONSOLE_WIDTH = 80;

    public static void enable(long period, TimeUnit unit) {
        enable(Metrics.defaultRegistry(), period, unit);
    }

    private static void enable(MetricsRegistry metricsRegistry, long period, TimeUnit unit) {
        final ConsoleReporter reporter = new ConsoleReporter(metricsRegistry, System.out, MetricPredicate.ALL);

        reporter.start(period, unit);
    }

    private final PrintStream out;
    private final MetricPredicate predicate;
    private final Clock clock;
    private final TimeZone timeZone;
    private final Locale locale;

    public ConsoleReporter(PrintStream out) {
        this(Metrics.defaultRegistry(), out, MetricPredicate.ALL);
    }

    public ConsoleReporter(MetricsRegistry metricsRegistry, PrintStream out, MetricPredicate predicate) {
        this(metricsRegistry, out, predicate, Clock.defaultClock(), TimeZone.getDefault());
    }

    public ConsoleReporter(MetricsRegistry metricsRegistry,
                           PrintStream out,
                           MetricPredicate predicate,
                           Clock clock,
                           TimeZone timeZone) {
        this(metricsRegistry, out, predicate, clock, timeZone, Locale.getDefault());
    }

    public ConsoleReporter(MetricsRegistry metricsRegistry,
                           PrintStream out,
                           MetricPredicate predicate,
                           Clock clock,
                           TimeZone timeZone, Locale locale) {
        super(metricsRegistry, "console-reporter");
        this.out = out;
        this.predicate = predicate;
        this.clock = clock;
        this.timeZone = timeZone;
        this.locale = locale;
    }

    @Override
    public void run() {
        try {
            final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
            format.setTimeZone(timeZone);

            final String dateTime = format.format(new Date(clock.time()));

            out.print(dateTime);
            out.print(' ');
            for (int i = 0; i < (CONSOLE_WIDTH - dateTime.length() - 1); i++) {
                out.print('=');
            }
            out.println();
            for (Map.Entry<String, SortedMap<MetricName, Metric>> entry : getMetricsRegistry().groupedMetrics(predicate).entrySet()) {
                out.print(entry.getKey());
                out.println(':');

                for (Map.Entry<MetricName, Metric> subEntry : entry.getValue().entrySet()) {
                    out.print("  ");
                    out.print(subEntry.getKey().getName());
                    out.println(':');
                    subEntry.getValue().processWith(this, subEntry.getKey(), out);
                    out.println();
                }
                out.println();
            }

            out.println();
            out.flush();
        } catch (Exception e) {

        }
    }

    @Override
    public void processGauge(MetricName name, Gauge<?> gauge, PrintStream stream) throws Exception {
        stream.printf(locale, "    value = %s\n", gauge.value());
    }

    @Override
    public void processCounter(MetricName name, Counter counter, PrintStream stream) throws Exception {
        stream.printf(locale, "    count = %d\n", counter.count());
    }
}
