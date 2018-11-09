package com.webex.dap.data.hadoop.yarn_.rpc_.demo.server_;

import com.webex.dap.data.hadoop.yarn_.rpc_.demo.interface_.EchoProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

/**
 * Created by harry on 9/27/18.
 */
public class RPCServerDemo {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        Server server = new RPC.Builder(conf)
                .setProtocol(EchoProtocol.class)
                .setInstance(new EchoProtocolImpl())
                .setBindAddress("127.0.0.1")
                .setPort(8989).setNumHandlers(5).build();

        server.start();
    }
}
