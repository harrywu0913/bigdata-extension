package com.webex.dap.data.dataX_.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ConfigParser {
    public static Configuration parse(final String jobPath) {
        Configuration configuration = ConfigParser.parseJobConfig(jobPath);
        //
        //

        String readerPluginName = configuration.getString("job.content[0].reader.name");
        String writerPluginName = configuration.getString("job.content[0].writer.name");
        String preHandlerName = configuration.getString("job.preHandler.pluginName");
        String postHandlerName = configuration.getString("job.postHandler.pluginName");

        Set<String> pluginList = new HashSet<String>();
        pluginList.add(readerPluginName);
        pluginList.add(writerPluginName);

        if(StringUtils.isNotEmpty(preHandlerName)) {
            pluginList.add(preHandlerName);
        }
        if(StringUtils.isNotEmpty(postHandlerName)) {
            pluginList.add(postHandlerName);
        }

        return null;
    }

    public static Configuration parseJobConfig(final String path) {
        return null;
    }
}
