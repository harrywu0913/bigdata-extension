package com.webex.dap.tool;

import com.cloudera.sqoop.SqoopOptions;

/**
 * Created by harry on 8/13/18.
 */
public class DemoTool extends org.apache.sqoop.tool.BaseSqoopTool {
    public DemoTool() {
        super("demo");
    }

    @Override
    public int run(SqoopOptions options) {
        return 0;
    }
}
