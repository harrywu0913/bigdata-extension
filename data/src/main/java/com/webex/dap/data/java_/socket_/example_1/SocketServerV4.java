package com.webex.dap.data.java_.socket_.example_1;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerV4 {
    public static void main(String[] args) throws IOException {
        int port = 51234;

        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes;
        while(true){
            int first = inputStream.read();

            if (first == -1){
                break;
            }

            int second = inputStream.read();

            int length = (first << 8) + second;
            bytes = new byte[length];

            inputStream.read(bytes);

            System.out.println("Get Message from client: " + new String(bytes,"UTF-8"));
        }

        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}
