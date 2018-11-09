package com.webex.dap.data.dataX_.core;

import com.webex.dap.data.dataX_.core.job.JobContainer;
import com.webex.dap.data.dataX_.core.util.ConfigParser;
import com.webex.dap.data.dataX_.core.util.Configuration;
import com.webex.dap.data.dataX_.core.util.container.LoadUtil;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Engine {
    public static void main(String[] args){
        int exitCode = 0;
        try{
            Engine.entry(args);
        }catch (Throwable e){
            System.exit(exitCode);
        }
        System.exit(exitCode);
    }

    private static void entry(String[] args) throws Throwable {
        Options options = new Options();
        options.addOption("job", true, "Job config.");
        options.addOption("jobid", true, "Job unique id.");
        options.addOption("mode", true, "Job runtime mode.");

        BasicParser parser = new BasicParser();
        CommandLine cl = parser.parse(options, args);

        String jobPath = cl.getOptionValue("job");

        //
        //

        Configuration configuration = ConfigParser.parse(jobPath);


        Engine engine = new Engine();
        engine.start(configuration);

    }

    private void start(Configuration allConf) {
        //
        //
        //

        LoadUtil.bind(allConf);

        boolean isJob = true;

        AbstractContainer container;

        if (isJob){
            container = new JobContainer(allConf);
        }else{

        }
    }
}
