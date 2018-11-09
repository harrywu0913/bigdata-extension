package com.webex.dap.data.metrics_.yammer_.core_;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by harry on 8/7/18.
 */
public class Meter {
    private static final long INTERVAL = 5; // seconds

//    private final EWMA m1Rate = EWMA.oneMinuteEWMA();

    private final AtomicLong count = new AtomicLong();
    private final long startTime;
    private final TimeUnit rateUnit;
    private final String eventType;
    private final ScheduledFuture<?> future;
    private final Clock clock;

    Meter(ScheduledExecutorService tickThread, String eventType, TimeUnit rateUnit,Clock clock){
        this.rateUnit = rateUnit;
        this.eventType = eventType;
        this.future = tickThread.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                tick();

            }
        },INTERVAL,INTERVAL,TimeUnit.SECONDS);

        this.clock = clock;
        this.startTime = this.clock.tick();
    }

    private void tick() {
//        m1Rate.tick();
    }
}
