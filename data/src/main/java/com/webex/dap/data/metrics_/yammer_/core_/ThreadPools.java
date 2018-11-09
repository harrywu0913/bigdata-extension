package com.webex.dap.data.metrics_.yammer_.core_;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by harry on 8/7/18.
 */
public class ThreadPools {
    private final ConcurrentMap<String, ScheduledExecutorService> threadPools = new ConcurrentHashMap<String, ScheduledExecutorService>(100);

    private static class NamedThreadFactory implements ThreadFactory{
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NamedThreadFactory(String name) {
            final SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "metrics-" + name + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new Thread(group,r,namePrefix + threadNumber.getAndIncrement(),0);
            t.setDaemon(true);

            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public ScheduledExecutorService newScheduledThreadPool(int poolSize, String name) {
        final ScheduledExecutorService existing = threadPools.get(name);

        if (isValidExecutor(existing)){
            return existing;
        }else{
            synchronized (this){
                final ScheduledExecutorService service = Executors.newScheduledThreadPool(poolSize,new NamedThreadFactory(name));
                threadPools.put(name,service);

                return service;
            }
        }
    }

    private boolean isValidExecutor(ScheduledExecutorService executor) {
        return executor != null && !executor.isShutdown() && !executor.isTerminated();
    }
}
