package com.hwt.blocking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * socket 客户端1
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("127.0.0.1", 8001);

            client.setKeepAlive(true);
            client.setSoTimeout(10000);
            new Thread(() -> {
                while (true){
                    try {
                        Thread.sleep(1000);
                        client.sendUrgentData(0xFF);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            OutputStream outputStream = client.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            while(true){
                String s = scanner.nextLine();
                outputStream.write(s.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
