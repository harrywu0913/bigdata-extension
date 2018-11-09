package com.webex.dap.fs_;

import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.shell.DapFsShell;

/**
 * Created by harry on 9/7/18.
 */
public class DapFsShellDemo {
    public static void main(String[] args) throws Exception {
        DapFsShell.main(new String[]{"-Doutput.compress=true","-Doutput.compress.codec=com.hadoop.compression.lzo.LzoCodec","-dapput", "-f","/Users/harry/Documents/project/cmse/dap-extension-self/hadoop-extension/pom.xml","/tmp/pom.xml.lzo_deflate"});
    }
}
