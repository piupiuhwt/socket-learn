package com.hwt.netty.server.innerserver;

import com.hwt.netty.server.ForwordServer;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        int port2 = Integer.parseInt(args[1]);
        RealServer realServer = new RealServer();
        ForwordServer forwordServer = new ForwordServer();
        new Thread(() -> {
            forwordServer.bind(port2);
        }).start();
        realServer.bind(port);
    }
}
