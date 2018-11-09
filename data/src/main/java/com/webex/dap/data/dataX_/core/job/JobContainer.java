package com.webex.dap.data.dataX_.core.job;

import com.webex.dap.data.dataX_.core.AbstractContainer;
import com.webex.dap.data.dataX_.core.util.Configuration;

public class JobContainer extends AbstractContainer {
    private long startTimeStamp;

    private long endTimeStamp;

    private long startTransferTimeStamp;

    private long endTransferTimeStamp;

    private int totalStage = 1;

    public JobContainer(Configuration allConf) {
        super();
    }

    @Override
    public void start() {
        boolean hasException = false;
        boolean isDryRun = false;

        try {
            this.startTimeStamp = System.currentTimeMillis();


            if (isDryRun) {
                this.preCheck();
            } else {

                //
                this.preHandle();

                this.init();

                this.prepare();

                this.totalStage = this.split();

                this.schedule();

                this.post();

                this.postHandle();

                this.invokeHooks();
            }

        } catch (Throwable e) {

        }
    }

    private void invokeHooks() {

    }

    private void postHandle() {

    }

    private void post() {

    }

    private void schedule() {

    }

    private int split() {
        return -1;
    }

    private void prepare() {
        
    }

    private void init() {
        
    }

    private void preHandle() {

    }

    private void preCheck() {
    }
}
