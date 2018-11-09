package com.webex.dap.data.netty_.example1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by harry on 7/13/18.
 */
public class TimeClient {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);


        socketChannel.connect(new InetSocketAddress("127.0.0.1", 12345));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select(1000);

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();

                if (key.isValid()) {

                    if (key.isConnectable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        if (sc.finishConnect()) {
                            sc.configureBlocking(false);
                            sc.write(ByteBuffer.wrap("hello".getBytes()));

                            sc.register(selector, SelectionKey.OP_READ);
                        } else {
                            System.exit(1);
                        }
                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        int len = 0;


                        while ((len = sc.read(buf)) > 0) {
                            buf.flip();
                            byte[] msg = new byte[1024];
                            buf.get(msg, 0, len);
                            System.out.println(new String(msg, 0, len));
                        }

                        if (len < 0) {
                            key.cancel();
                            sc.close();
                        }else{
                            sc.write(ByteBuffer.wrap("hello".getBytes()));
                        }
                    }
                }

                it.remove();
            }
        }
    }
}
