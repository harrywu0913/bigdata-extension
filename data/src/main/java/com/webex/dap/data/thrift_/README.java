package com.webex.dap.data.thrift_;

/*
    https://blog.csdn.net/liuxilil/article/details/54924366

    一、数据类型
        基础数据类型：
            bool
            byte
            i16         =>  short
            i32         =>  int
            i64         =>  long
            double
            string
        结构体
            struct      =>  JavaBean

        容器类型：
            list
            set
            map
        异常类型：
            exception

    二、服务器端编码步骤
        1. 实现服务处理接口impl
        2. 创建TProcessor
        3. 创建TServerTransport
        4. 创建TProtocol
        5. 创建TServer
        6. 启动Server

    三、客户端端编码步骤
        1. 创建Transport
        2. 创建TProtocol
        3. 基于Transport和TProtocol创建Client
        4. 调用Client

    四、数据传输协议：
        TBinaryProtocol
        TCompactProtocol
        TJSONProtocol
        TSimpleJSONProtocol


https://www.cnblogs.com/maociyuan/p/5718341.html

    五、服务端工作模式：
        1. TSimpleServer
            单线程服务器使用了标准的阻塞式IO，只有一个工作线程，循环监听请求并完成对请求的处理。

        2. TThreadPoolServer
            多线程服务器使用了标准的阻塞式IO，采用阻塞socket方式。主线程负责阻塞式监听socket，业务处理交由线程池数量

        3. TNonblockingServer
            单线程工作，采用了NIO方式，所有的socket都被注册到selector上，一个线程通过selector循环监控所有的socket，

        4. THsHaServer

        5. TThreadedSelectorServer
 */
public class README {

}
