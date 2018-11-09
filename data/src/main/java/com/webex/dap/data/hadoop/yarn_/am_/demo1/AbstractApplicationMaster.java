package com.webex.dap.data.hadoop.yarn_.am_.demo1;

import com.beust.jcommander.Parameter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.NMClient;
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by harry on 9/27/18.
 */
public abstract class AbstractApplicationMaster {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractApplicationMaster.class.getName());

    private Configuration conf;

    private AtomicInteger completedContainerCount;
    private AtomicInteger allocatedContainerCount;
    private AtomicInteger failedContainerCount;
    private AtomicInteger requestedContainerCount;

    private boolean done;

    protected Map<ContainerId, String> containerIdCommandMap;
    protected List<String> failedCommandList;

    private AMRMClientAsync<AMRMClient.ContainerRequest> resourceManager;
    private NMClient nodeManager;

    private String appMasterHostname = "";     // TODO: What should this really be?
    private int appMasterRpcPort = 0;          // TODO: What should this really be?
    private String appMasterTrackingUrl = "";  // TODO: What should this really be?

    @Parameter(names = {"-" + Constants.OPT_CONTAINER_MEM, "--" + Constants.OPT_CONTAINER_MEM})
    private int containerMem;

    @Parameter(names = {"-" + Constants.OPT_CONTAINER_COUNT, "--" + Constants.OPT_CONTAINER_COUNT})
    protected int totalContainerCount;

    @Parameter(names = {"-" + Constants.OPT_COMMAND, "--" + Constants.OPT_COMMAND})
    private String command;

    public AbstractApplicationMaster() {
        conf = new YarnConfiguration();
        completedContainerCount = new AtomicInteger();
        allocatedContainerCount = new AtomicInteger();
        failedContainerCount = new AtomicInteger();
        requestedContainerCount = new AtomicInteger();

        containerIdCommandMap = new HashMap<ContainerId, String>();
        failedCommandList = new ArrayList<String>();
    }


    public void init(String[] args) {
        done = false;
    }

    public boolean run() throws Exception {
        AMRMClientAsync.CallbackHandler rmListener = new RMCallbackHandler();

        resourceManager = AMRMClientAsync.createAMRMClientAsync(1000, rmListener);
        resourceManager.init(conf);
        resourceManager.start();

        nodeManager = NMClient.createNMClient();
        nodeManager.init(conf);
        nodeManager.start();

        // Register with RM

        resourceManager.registerApplicationMaster(appMasterHostname, appMasterRpcPort, appMasterTrackingUrl);

        // Ask RM to give use a bunch of containers

        for (int i = 0; i < totalContainerCount; i++) {
            AMRMClient.ContainerRequest containerRequest = setupContainerReqForRM();
            resourceManager.addContainerRequest(containerRequest);
        }

        requestedContainerCount.addAndGet(totalContainerCount);

        while (!done) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {

            }
        }

        resourceManager.unregisterApplicationMaster(FinalApplicationStatus.SUCCEEDED, "", "");

        return true;
    }

    private AMRMClient.ContainerRequest setupContainerReqForRM() {
        // Priority for worker containers - priorities are intra-application
        Priority priority = Records.newRecord(Priority.class);
        priority.setPriority(0);
        // Resource requirements for worker containers
        Resource capability = Records.newRecord(Resource.class);
        capability.setMemory(containerMem);
        //capability.setVirtualCores(1);
        AMRMClient.ContainerRequest containerReq = new AMRMClient.ContainerRequest(
                capability,
                null /* hosts String[] */,
                null /* racks String [] */,
                priority);

        return containerReq;
    }

    private class RMCallbackHandler implements AMRMClientAsync.CallbackHandler {

        @Override
        public void onContainersCompleted(List<ContainerStatus> statuses) {
            for (ContainerStatus status : statuses) {
                assert (status.getState() == ContainerState.COMPLETE);

                int exitStatus = status.getExitStatus();

                if (exitStatus != ContainerExitStatus.SUCCESS) {
                    if (exitStatus != ContainerExitStatus.ABORTED) {
                        failedContainerCount.incrementAndGet();
                    }

                    allocatedContainerCount.decrementAndGet();
                    requestedContainerCount.decrementAndGet();

                    recordFailedCommand(status.getContainerId());
                } else {
                    completedContainerCount.incrementAndGet();
                }

                int askAgainCount = totalContainerCount - requestedContainerCount.get();
                requestedContainerCount.addAndGet(askAgainCount);

                if (askAgainCount > 0) {
                    // need to reallicate failed conainters

                    for (int i = 0; i < askAgainCount; i++) {
                        AMRMClient.ContainerRequest req = setupContainerReqForRM();
                        resourceManager.addContainerRequest(req);
                    }
                }

                if (completedContainerCount.get() == totalContainerCount) {
                    done = true;
                }
            }
        }

        @Override
        public void onContainersAllocated(List<Container> containers) {
            int containerCnt = containers.size();
            List<String> cmdList = new ArrayList<String>();

            if (failedCommandList.isEmpty()) {
                int startFrom = allocatedContainerCount.getAndAdd(containerCnt);
                cmdList = buildCommandList(startFrom, containerCnt, command);
            } else {

            }

            for (int i = 0; i < containerCnt; i++) {
                Container c = containers.get(i);
                String cmdStr = cmdList.remove(0);

                StringBuilder sb = new StringBuilder();
                ContainerLaunchContext ctx = Records.newRecord(ContainerLaunchContext.class);
                containerIdCommandMap.put(c.getId(), cmdStr);

                ctx.setCommands(Collections.singletonList(sb.append(cmdStr).append(" 1> ").append(ApplicationConstants.LOG_DIR_EXPANSION_VAR).append("/stdout").append(" 2> ").append(ApplicationConstants.LOG_DIR_EXPANSION_VAR).append("/stderr").toString()));

                try{
                    nodeManager.startContainer(c,ctx);
                } catch (YarnException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Called when the RM wants the AM to shutdown for being out of sync etc. The AM should not unregister with the RM unless the AM wants to be the last attempt;
        @Override
        public void onShutdownRequest() {
            done = true;
        }

        @Override
        public void onNodesUpdated(List<NodeReport> updatedNodes) {

        }

        @Override
        public float getProgress() {
            return 0;
        }

        @Override
        public void onError(Throwable e) {
            done = true;
            resourceManager.stop();
        }
    }

    private void recordFailedCommand(ContainerId containerId) {

    }

    protected abstract List<String> buildCommandList(int startFrom, int containerCnt, String command);
}
