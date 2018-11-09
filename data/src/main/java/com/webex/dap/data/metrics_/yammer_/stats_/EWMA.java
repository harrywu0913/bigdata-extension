package com.webex.dap.data.metrics_.yammer_.stats_;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.exp;

/**
 * Created by harry on 8/8/18.
 */
public class EWMA {
    private static final int INTERVAL = 5;
    private static final double SECONDS_PER_MINUTE = 60.0;
    private static final int ONE_MINUTE = 1;
    private static final double M1_ALPHA = 1 - exp(-INTERVAL / SECONDS_PER_MINUTE / ONE_MINUTE);

    private volatile boolean initialized = false;
    private volatile double rate = 0.0;

    private final AtomicLong uncounted = new AtomicLong();
    private final double alpha, interval;

    public EWMA(double alpha, long interval, TimeUnit intervalUnit) {
        this.interval = intervalUnit.toNanos(interval);
        this.alpha = alpha;
    }

    public static EWMA oneMinuteEWMA(){
        return new EWMA(M1_ALPHA, INTERVAL, TimeUnit.SECONDS);
    }

    public void tick(){
        final long count = uncounted.getAndSet(0);
        final double instantRate = count / interval;
        if (initialized){
            rate += (alpha * (instantRate - rate));
        }else{
            rate = instantRate;
            initialized = true;
        }
    }
}
