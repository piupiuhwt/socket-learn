package com.hwt.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class SocketService2 {
    public static volatile boolean flag = true;

    public static void main(String[] args) throws IOException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
        ServerSocket serverSocket = new ServerSocket(4445,100,localHost);
        System.out.println("start up and accept");
        Socket accept = serverSocket.accept();
        doRead(accept);
        int read = System.in.read();
        flag = false;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accept.close();
        serverSocket.close();

    }

    public static void doRead(Socket client) {
        byte[] buffer = new byte[1024*8];
        Thread t = new Thread(() -> {
            try {
                String ipAddress = client.getInetAddress().toString();
                int port = client.getPort();
                InputStream inputStream = client.getInputStream();
                int len = 0;
                while (flag) {
                    int read = inputStream.read(buffer);
                    String s = new String(buffer, 0, read);
                    System.out.println(ipAddress+" "+port+" : "+s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}
