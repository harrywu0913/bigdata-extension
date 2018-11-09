package com.webex.dap.data.thrift_.java_.example1;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;

public class UserInfoServiceServer {
    public static final int SERVER_PORT = 8090;
    public static final int SERVER_PORT1 = 8091;
    public static final int SERVER_PORT2 = 8092;
    public static final int SERVER_PORT3 = 8093;

    public void startSimleServer() {
        try {
            System.out.println("UserInfoServiceDemo TSimpleServer start ....");
            TProcessor tprocessor = new UserInfoService.Processor<UserInfoService.Iface>(new UserInfoServiceImpl());

            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);

            TServer.Args tArgs = new TServer.Args(serverTransport);

            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());


            TServer server = new TSimpleServer(tArgs);
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    public void startTThreadPoolServer() {
        try {
            System.out.println("UserInfoServiceDemo TThreadPoolServer start ....");
            TProcessor tprocessor = new UserInfoService.Processor<UserInfoService.Iface>(new UserInfoServiceImpl());

            TServerSocket serverTransport = new TServerSocket(SERVER_PORT1);

            TThreadPoolServer.Args ttpsArgs = new TThreadPoolServer.Args(serverTransport);

            ttpsArgs.processor(tprocessor);
            ttpsArgs.protocolFactory(new TBinaryProtocol.Factory());


            // 线程池服务模型，使用标准的阻塞式IO，预先创建一组线程处理请求。
            TServer server = new TThreadPoolServer(ttpsArgs);
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();

        }
    }

    public void startTNonblockingServer() {
        try {
            System.out.println("UserInfoServiceDemo TNonblockingServer start ....");
            TProcessor tprocessor = new UserInfoService.Processor<UserInfoService.Iface>(new UserInfoServiceImpl());
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(SERVER_PORT2);
            TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(tnbSocketTransport);
            tnbArgs.processor(tprocessor);

            // 使用非阻塞式IO，服务端和客户端需要指定TFramedTransport数据传输的方式
            tnbArgs.transportFactory(new TFramedTransport.Factory());
            tnbArgs.protocolFactory(new TBinaryProtocol.Factory());

            TServer server = new TNonblockingServer(tnbArgs);
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final UserInfoServiceServer server = new UserInfoServiceServer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                server.startSimleServer();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                server.startTThreadPoolServer();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                server.startTNonblockingServer();
            }
        }).start();




        //server.startTNonblockingServer();

        //server.startTHsHaServer();
    }
}
