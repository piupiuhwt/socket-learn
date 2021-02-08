package com.hwt.nonblock.timesystem;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
//        if (args != null && args.length >= 0) {
//            try{
//                port = Integer.parseInt(args[0]);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
            new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
//        }
    }
}
