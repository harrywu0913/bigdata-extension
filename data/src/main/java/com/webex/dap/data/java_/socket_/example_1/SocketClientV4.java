package com.webex.dap.data.java_.socket_.example_1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClientV4 {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 51234;


        Socket socket = new Socket(host,port);
        OutputStream outputStream = socket.getOutputStream();

        String message = "msg1: 你好，world!";
        byte[] sendMsg = message.getBytes("UTF-8");

        outputStream.write(sendMsg.length >> 8);
        outputStream.write(sendMsg.length);
        outputStream.write(sendMsg);
        outputStream.flush();

        message = "msg2: Hello World!";
        sendMsg = message.getBytes("UTF-8");

        outputStream.write(sendMsg.length >> 8);
        outputStream.write(sendMsg.length);
        outputStream.write(sendMsg);
        outputStream.flush();

        message = "msg3: Hello World!";
        sendMsg = message.getBytes("UTF-8");

        outputStream.write(sendMsg.length >> 8);
        outputStream.write(sendMsg.length);
        outputStream.write(sendMsg);
        outputStream.flush();

        outputStream.close();

        socket.close();
    }
}
