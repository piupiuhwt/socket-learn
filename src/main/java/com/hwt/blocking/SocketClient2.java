package com.hwt.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * socket 客户端2
 */
public class SocketClient2 {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("127.0.0.1", 8001);
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String s = scanner.nextLine();
                    try {
                        outputStream.write(s.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                try {
                    int read = inputStream.read(buffer);
                    String s = new String(buffer, 0, read);
                    System.out.println("服务端反回："+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
