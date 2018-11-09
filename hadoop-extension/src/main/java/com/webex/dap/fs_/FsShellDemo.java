package com.webex.dap.fs_;

import org.apache.hadoop.fs.FsShell;

/**
 * Created by harry on 9/7/18.
 */
public class FsShellDemo {
    public static void main(String[] args) throws Exception {
        FsShell.main(new String[]{"-ls", "/"});
    }
}
