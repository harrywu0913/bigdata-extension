package com.webex.dap.data.thrift_.java_.example1;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UserInfoServiceClient {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8090;
    public static final int SERVER_PORT1 = 8091;
    public static final int SERVER_PORT2 = 8092;
    public static final int SERVER_PORT3 = 8093;
    public static final int TIMEOUT = 30000;

    public void startClient(int userid,int port) {
        TTransport transport = null;
        try {
            transport = new TSocket(SERVER_IP, port, TIMEOUT);
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
//             TProtocol protocol = new TCompactProtocol(transport);
//             TProtocol protocol = new TJSONProtocol(transport);

            UserInfoService.Client client = new UserInfoService.Client(protocol);
            transport.open();

            String result = client.getUserNameById(userid);

            System.out.println("Thrify client result =: " + result);

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            //处理服务端返回值为null问题
            if (e instanceof TApplicationException
                    && ((TApplicationException) e).getType() ==
                    TApplicationException.MISSING_RESULT) {
                System.out.println("The result of lg_userinfo_getUserNameById function is NULL");
            }
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    public void startClientAsync(int userid,int port) {
        TNonblockingTransport transport = null;
        try {
            TAsyncClientManager clientManager = new TAsyncClientManager();
            transport = new TNonblockingSocket(SERVER_IP,port, TIMEOUT);
            TProtocolFactory tprotocol = new TBinaryProtocol.Factory();

            UserInfoService.AsyncClient asyncClient = new UserInfoService.AsyncClient(tprotocol, clientManager, transport);

            System.out.println("Client start .....");
            CountDownLatch latch = new CountDownLatch(1);
            AsynCallback callBack = new AsynCallback(latch);
            System.out.println("call method sayHello start ...");
            asyncClient.getUserNameById(userid,callBack);
            System.out.println("call method sayHello .... end");
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("startClient end.");
    }

    public class AsynCallback implements AsyncMethodCallback<String> {
        private CountDownLatch latch;
        public AsynCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onComplete(String response) {
            System.out.println("onComplete");
            try {
                Thread.sleep(1000L * 1);
                System.out.println("AsynCall result =:" + response);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }

        @Override
        public void onError(Exception exception) {
            System.out.println("onError :" + exception.getMessage());
            latch.countDown();
        }

    }

    public static void main(String[] args) {
        UserInfoServiceClient client = new UserInfoServiceClient();
        client.startClient(1,SERVER_PORT);
        client.startClient(2,SERVER_PORT1);

        client.startClientAsync(1,SERVER_PORT2);
//        client.startClientAsync(2,SERVER_PORT3);
//        client.startClientAsync(3,SERVER_PORT2);
    }
}
