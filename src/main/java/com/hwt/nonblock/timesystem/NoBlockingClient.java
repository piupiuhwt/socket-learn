package com.hwt.nonblock.timesystem;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NoBlockingClient {
    public static void main(String[] args) throws IOException {
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);
        client.connect(new InetSocketAddress("127.0.0.1", 4488));
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        allocate.put(input.getBytes());
        allocate.flip();
        client.write(allocate);
        if (!allocate.hasRemaining()){
            System.out.println("send ok !");
        }
    }

}
