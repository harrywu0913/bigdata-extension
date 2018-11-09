package com.webex.dap.data.hadoop.yarn_.rpc_.demo.interface_;

import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

/**
 * Created by harry on 9/27/18.
 */
public interface EchoProtocol extends VersionedProtocol {

    final long versionID = 1L;

    String echo(String value) throws IOException;

    int add(int v1, int v2) throws IOException;
}
