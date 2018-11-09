package com.webex.dap.data.zk.lock_.example1;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/*
    1. 多个客户端竞争创建LOCK临时节点
    2. 其中某个客户端成功创建LOCK节点，其他客户端对LOCK节点设置watcher
    3. 持有锁的客户端删除LOCK节点或是该客户端崩溃，由ZK删除LOCK节点
    4. 其他客户端获取LOCK节点被删除的通知
 */
public class ExclusiveLock implements DistributedLock {
    private static final String LOCK_NODE_FULL_PATH = "/exclusive_lock/lock";

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public ExclusiveLock() throws IOException, InterruptedException {
        new ZooKeeper("127.0.0.1:2181",1000,new LockNodeWatcher());
        connectedSemaphore.await();
    }

    @Override
    public void lock() throws Exception {
//        if (createLockNode()){
//            return;
//        }
    }

    @Override
    public boolean tryLock() throws Exception {
        return false;
    }

    @Override
    public boolean tryLock(long millisecond) throws Exception {
        return false;
    }

    @Override
    public void unlock() throws Exception {

    }

    private class LockNodeWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {

        }
    }
}
