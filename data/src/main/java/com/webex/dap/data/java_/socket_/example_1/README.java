package com.webex.dap.data.java_.socket_.example_1;

/*
    https://www.cnblogs.com/yiwangzhibujian/p/7107785.html#q1

    如何告知对方已发送完命令了。
        客户端打开一个输出流，如果不做约定，也不关闭它，那么服务器端永远不知道客户端是否发送完消息。那么服务端会一直等待下去，直到超时。

        1. 通过socket关闭
            当socket关闭是，服务器端就会收到响应的关闭信号，那么服务器端也就是知道流已经关闭了，这个时候读取操作完成了。
            缺点：
                1. 客户端socket关闭后，就不能接受服务器端发送的信息了，也不能再次发送消息了

        2. 通过socket关闭输出流的方式
            socket.shutdownOutput() 而不是 outputStream.close().

            如果调用了socket.shutdownOutput()方式，底层会告知服务器端这边已经写完了。
            缺点：
                不能再次发送信息了

        3. 通过约定符号
            如一行end，代表发送完成。
            缺点：
                额外的约定结束符，太简单的容易出现在发送的消息体重。太复杂的不好处理。

        4. 通过指定长度



 */
public class README {
}
