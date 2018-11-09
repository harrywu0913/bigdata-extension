package com.webex.dap.data.dataX_.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class RetryUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RetryUtil.class);

    private static final long MAX_SLEEP_MILLISECOND = 256 * 1000;

    public static <T> T executeWithRetry(Callable<T> callable, int retryTimes, long sleepTimeInMilliSeconds, boolean exponential) throws Exception {
        Retry retry = new Retry();

        return retry.doRetry(callable, retryTimes, sleepTimeInMilliSeconds, exponential, null);
    }

    private static class Retry {

        public <T> T doRetry(Callable<T> callable, int retryTimes, long sleepTimeInMilliSecond, boolean exponential, List<Class<?>> retryExceptionClasss) throws Exception {
            if (null == callable) {
                throw new IllegalArgumentException("");
            }

            if (retryTimes < 1) {
                throw new IllegalArgumentException("");
            }

            Exception saveException = null;
            for (int i = 0; i < retryTimes; i++) {
                try {
                    return call(callable);
                } catch (Exception e) {
                    saveException = e;

                    if (i == 0) {
                        LOG.error(String.format("Exception when calling callable, 异常Msg:%s", saveException.getMessage()), saveException);
                    }

                    if (null != retryExceptionClasss && !retryExceptionClasss.isEmpty()) {
                        boolean needRetry = false;
                        for (Class<?> eachExceptionClass : retryExceptionClasss) {
                            if (eachExceptionClass == e.getClass()) {
                                needRetry = true;
                                break;
                            }
                        }

                        if (!needRetry) {
                            throw saveException;
                        }
                    }

                    if (i + 1 < retryTimes && sleepTimeInMilliSecond > 0) {
                        long startTime = System.currentTimeMillis();

                        long timeToSleep;

                        if (exponential) {
                            timeToSleep = sleepTimeInMilliSecond * (long) Math.pow(2, i);
                            if (timeToSleep >= MAX_SLEEP_MILLISECOND) {
                                timeToSleep = MAX_SLEEP_MILLISECOND;
                            }
                        } else {
                            timeToSleep = sleepTimeInMilliSecond;
                            if (timeToSleep >= MAX_SLEEP_MILLISECOND) {
                                timeToSleep = MAX_SLEEP_MILLISECOND;
                            }
                        }

                        try {
                            Thread.sleep(timeToSleep);
                        } catch (InterruptedException ignored) {

                        }

                        long realTimeSleep = System.currentTimeMillis() - startTime;
                    }
                }
            }

            throw saveException;

        }

        private <T> T call(Callable<T> callable) throws Exception {
            return callable.call();
        }
    }
}
