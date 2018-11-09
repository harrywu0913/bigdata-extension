package org.apache.hadoop.fs.shell;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class DapFsShell extends FsShell {
    protected static FsShell newShellInstance() {
        return new DapFsShell();
    }

    @Override
    protected void init() throws IOException {
        super.init();
        this.commandFactory.registerCommands(DapPutComamnd.class);
    }

    public static void main(String argv[]) throws Exception {
        FsShell shell = newShellInstance();
        Configuration conf = new Configuration();
        conf.setQuietMode(false);
        shell.setConf(conf);
        int res;
        try {
            res = ToolRunner.run(shell, argv);
        } finally {
            shell.close();
        }
        System.exit(res);
    }
}
