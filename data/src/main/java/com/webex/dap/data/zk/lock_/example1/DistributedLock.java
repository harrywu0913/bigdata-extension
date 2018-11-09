package com.webex.dap.data.zk.lock_.example1;

public interface DistributedLock {
    void lock() throws Exception;
    boolean tryLock() throws Exception;
    boolean tryLock(long millisecond) throws Exception;
    void unlock() throws Exception;
}
