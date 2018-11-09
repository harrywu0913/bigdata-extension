package com.webex.dap.data.zk.election.example1;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by harry on 7/25/18.
 */
public class WorkerServer {
    private volatile boolean running = false; //记录服务器运行状态

    private static final String MASTER_PATH = "/master"; //master节点对应在zookeeper中的节点路径

    private ZkClient zkClient; //开源客户端-zk客户端

    private RunningData serverData; //集群中当前服务器节点的基本信息

    private RunningData masterData; //集群中master节点的基本信息

    private IZkDataListener dataListener; //监听zookeeper中master节点的删除事件

    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
    private int delayTime = 5;

    public WorkerServer(RunningData rd) {
        this.serverData = rd;
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

                /*
                    生产环境下，可能由于插拔网线等会导致的网络抖动。而在生成环境下可能server注册很多，每次切换主机，都有大量数据的同步，
                    所以，在这种网络抖动的情况下，我们最好还是选择master还是原有的集机器，可以避免master切换后，导致的数据同步

                 */
                if (masterData != null && masterData.getName().equals(serverData.getName())) {
                    takeMaster();
                } else {
                    //延时5s后再选主
                    delayExector.schedule(new Runnable() {
                        @Override
                        public void run() {
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }
        };
    }

    public void start() throws Exception {
        if (running) {
            throw new Exception("server has startup ...");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);

        takeMaster();
    }

    public void stop() throws Exception {
        if (!running) {
            throw new Exception("server has stoped");
        }

        running = false;

        delayExector.shutdown();

        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);

        releaseMaster();
    }

    private void releaseMaster() {
        if (checkMaster()){
            zkClient.delete(MASTER_PATH);
        }
    }

    private boolean checkMaster() {
        try {
            RunningData eventData = zkClient.readData(MASTER_PATH);
            masterData = eventData;

            if (masterData.getName().equals(serverData.getName())){
                return true;
            }

            return false;
        }catch (ZkNoNodeException e){
            return false;
        }catch (ZkInterruptedException e){
            return checkMaster();
        }catch (ZkException e){
            return false;
        }
    }

    private void takeMaster() {
        if (!running) {
            return;
        }

        try {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);

            masterData = serverData;

            System.out.println(serverData.getName() + " is master");
        } catch (ZkNodeExistsException e) {
            RunningData runningData = zkClient.readData(MASTER_PATH, false);
            if (runningData == null) {
                takeMaster();
            } else {
                masterData = runningData;
            }
        } catch (Exception e) {

        }
    }
}
