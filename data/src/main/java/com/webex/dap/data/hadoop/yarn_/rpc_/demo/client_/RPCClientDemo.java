package com.webex.dap.data.hadoop.yarn_.rpc_.demo.client_;

import com.webex.dap.data.hadoop.yarn_.rpc_.demo.interface_.EchoProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by harry on 9/27/18.
 */
public class RPCClientDemo {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        EchoProtocol echoProtocol = RPC.getProxy(EchoProtocol.class, EchoProtocol.versionID, new InetSocketAddress("127.0.0.1", 8989), conf);

        int result = echoProtocol.add(1,2);
    }
}
