package com.webex.dap.tools_.distcp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.tools.DistCp;
import org.apache.hadoop.tools.DistCpConstants;
import org.apache.hadoop.util.ShutdownHookManager;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class DapDistCp extends DistCp {
    private static final String DISTCP_DEFAULT_XML = "distcp-default.xml";
    static final Log LOG = LogFactory.getLog(DapDistCp.class);

    @Override
    protected Path createInputFileListing(Job job) throws IOException {
        job.setMapperClass(DapCopyMapper.class);
        return super.createInputFileListing(job);
    }

    public static void main(String argv[]) {
        int exitCode;
        try {
            DapDistCp distCp = new DapDistCp();
            Cleanup CLEANUP = new Cleanup(distCp);

            ShutdownHookManager.get().addShutdownHook(CLEANUP,
                    SHUTDOWN_HOOK_PRIORITY);
            exitCode = ToolRunner.run(getDefaultConf(), distCp, argv);
        } catch (Exception e) {
            LOG.error("Couldn't complete DistCp operation: ", e);
            exitCode = DistCpConstants.UNKNOWN_ERROR;
        }
        System.exit(exitCode);
    }

    private static Configuration getDefaultConf() {
        Configuration config = new Configuration();
        config.addResource(DISTCP_DEFAULT_XML);
        return config;
    }

    private static class Cleanup implements Runnable {
        private final DapDistCp distCp;

        public Cleanup(DapDistCp distCp) {
            this.distCp = distCp;
        }

        @Override
        public void run() {
            try {
                Path metaFolderPath = new Path(distCp.getConf().get(DistCpConstants.CONF_LABEL_META_FOLDER));
                FileSystem jobFS = metaFolderPath.getFileSystem(distCp.getConf());
                if (jobFS.exists(metaFolderPath)) {
                    jobFS.delete(metaFolderPath, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
