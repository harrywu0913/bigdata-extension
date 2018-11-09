package com.webex.dap.data.hadoop.yarn_.rpc_.demo.server_;

import com.webex.dap.data.hadoop.yarn_.rpc_.demo.interface_.EchoProtocol;
import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

/**
 * Created by harry on 9/27/18.
 */
public class EchoProtocolImpl implements EchoProtocol {
    @Override
    public String echo(String value) throws IOException {
        return value;
    }

    @Override
    public int add(int v1, int v2) throws IOException {
        return v1 + v2;
    }

    @Override
    public long getProtocolVersion(String protocol, long clientVersion) throws IOException {
        return EchoProtocol.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String protocol, long clientVersion, int clientMethodsHash) throws IOException {
        return new ProtocolSignature(EchoProtocol.versionID, null);
    }
}
