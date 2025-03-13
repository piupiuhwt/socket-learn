package com.hwt.aio.timesystem;

import java.nio.channels.AsynchronousServerSocketChannel;

public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeServerHandler timeserver = new AsyncTimeServerHandler(port);
        new Thread(timeserver,"AIO-TimeServerHandler-001").start();
    }
}
