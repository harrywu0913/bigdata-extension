package com.webex.dap.tools_.distcp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.tools.CopyListingFileStatus;
import org.apache.hadoop.tools.DistCpConstants;
import org.apache.hadoop.tools.DistCpOptionSwitch;
import org.apache.hadoop.tools.mapred.CopyMapper;

import java.io.IOException;

public class DapCopyMapper extends CopyMapper {
    private boolean overWrite = false;
    private FileSystem targetFS;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);

        Configuration conf = context.getConfiguration();
        this.overWrite = conf.getBoolean(DistCpOptionSwitch.OVERWRITE.getConfigLabel(), false);

        if (!overWrite) {
            Path targetFinalPath = new Path(conf.get(DistCpConstants.CONF_LABEL_TARGET_FINAL_PATH));
            targetFS = targetFinalPath.getFileSystem(conf);

            if (targetFS.exists(targetFinalPath) && targetFS.isFile(targetFinalPath)) {
                this.overWrite = true; // When target is an existing file, overwrite it.
            }
        }
    }

    @Override
    public void map(Text relPath, CopyListingFileStatus sourceFileStatus, Context context) throws IOException, InterruptedException {
        Path sourcePath = sourceFileStatus.getPath();

        String relPathStr = relPath.toString();
        if (relPathStr.startsWith("/")){
            relPathStr = relPathStr.substring(1);
        }

        Path acutalRelPath = new Path(Path.getPathWithoutSchemeAndAuthority(sourcePath).getParent(),relPathStr);
        super.map(new Text(acutalRelPath.toString()), sourceFileStatus, context);
    }
}
