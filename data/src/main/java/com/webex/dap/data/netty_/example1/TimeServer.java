package com.webex.dap.data.netty_.example1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by harry on 7/13/18.
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 12345;

        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.printf("select: %s \n", new Date());
            selector.select(1000);
            System.out.printf("select: %s \n", new Date());

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();

                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();

                        System.out.printf("Accept ==> {local-addr:%s, remote-addr:%s} \n",
                                socketChannel.socket().getLocalSocketAddress(),
                                socketChannel.socket().getRemoteSocketAddress());

                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        System.out.printf("Readable ==> {local-addr:%s, remote-addr:%s} \n",
                                channel.socket().getLocalSocketAddress(),
                                channel.socket().getRemoteSocketAddress());

                        readMsg(key, channel);
                    }
                }

                it.remove();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readMsg(SelectionKey key, SocketChannel channel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int len = 0;

        while ((len = channel.read(buf)) > 0) {
            buf.flip();
            byte[] msg = new byte[1024];
            buf.get(msg, 0, len);
            System.out.println(new String(msg, 0, len));
        }

        // 当客户端断开连接时，len=-1,需要关闭socketchanel，释放资源
        if (len < 0) {
            System.out.printf("Cancel ==> {key: %s,local-addr:%s, remote-addr:%s} \n",
                    key,
                    channel.socket().getLocalSocketAddress(),
                    channel.socket().getRemoteSocketAddress());
            key.cancel();
            channel.close();
        } else {
            ByteBuffer outBuffer = ByteBuffer.wrap("received".getBytes());
            channel.write(outBuffer);
        }

    }
}
