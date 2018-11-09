package com.webex.dap.tool;

import com.cloudera.sqoop.tool.ToolDesc;
import com.cloudera.sqoop.tool.ToolPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 8/21/18.
 */
public class DapToolPlugin extends ToolPlugin {
    @Override
    public List<ToolDesc> getTools() {
        List<ToolDesc> tools = new ArrayList<ToolDesc>();

        ToolDesc demoTool = new ToolDesc("demo",DemoTool.class,"just a demo");
        tools.add(demoTool);


        return tools;
    }
}
