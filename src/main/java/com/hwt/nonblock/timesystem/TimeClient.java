package com.hwt.nonblock.timesystem;

public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";
//        if (args != null && args.length > 0) {
//            try{
//                port = Integer.parseInt(args[0]);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            TimeClientHandler timeClient = new TimeClientHandler(host,port);
            new Thread(timeClient,"TimeClient-001").start();
//        }
    }
}
