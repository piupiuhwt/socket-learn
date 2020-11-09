package com.hwt.blocking;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Data
public class SocketService {

    private List<Socket> sockets;

    private ServerSocket service;

    private SocketService(ServerSocket service){
        this.sockets = new ArrayList<>();
        this.service = service;
    }

    public static SocketService getAService(int port){
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("server 启动监听端口为"+port);
            return new SocketService(server);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getClients(){
        new Thread(() -> {
            try {
                while(true){
                    Socket client = this.service.accept();
                    boolean add = this.sockets.add(client);
                    if (add) {
                        System.out.println(client.getInetAddress().toString() + client.getPort() + "添加成功");
                        //添加接口 对客户端收集处理
                        this.doRead(client);
                        continue;
                    }
                    System.out.println(client.getInetAddress().toString() + client.getPort() + "添加失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void doRead(Socket client) {
        byte[] buffer = new byte[1024*8];
        new Thread(() -> {
            try {
                String ipAddress = client.getInetAddress().toString();
                int port = client.getPort();
                InputStream inputStream = client.getInputStream();
                int len = 0;
                while (true) {
                    int read = inputStream.read(buffer);
                    String s = new String(buffer, 0, read);
                    System.out.println(ipAddress+" "+port+" : "+s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SocketService socketService = SocketService.getAService(8001);
        if (socketService!=null) {
            socketService.getClients();
        }
    }
}
