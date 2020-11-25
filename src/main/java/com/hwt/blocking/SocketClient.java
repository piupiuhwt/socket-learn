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
