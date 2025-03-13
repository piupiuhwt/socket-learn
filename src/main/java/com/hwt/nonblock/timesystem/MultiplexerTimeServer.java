package com.hwt.nonblock.timesystem;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable{

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;

                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        key.cancel();
                        if (key.channel()!=null) {
                            key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        //判断是否事有效key
        if (key.isValid()) {
            //判断是否事可接收的（服务端）
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            //判断是否可读
            if (key.isReadable()) {
                SocketChannel sc  = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes>0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("the timeserver receive order : " + body);
//                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? LocalDateTime.now().toString() : "BAD ORDER";

                    String contentString = "<html><head><title>test</title></head><body><h1>测试页</h1></body></html>";
                    int length = contentString.getBytes("UTF-8").length;
                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("HTTP/1.1 200 OK\r\n")
                            .append("content-type: text/html; Charset=utf-8\n")
                            .append("cache-control: private,no-cache\n")
                            .append("content-length: " + length + "\n")
                            .append("\n")
                            .append(contentString);
                    doWrite(sc, responseBuilder.toString());
                }else if (readBytes<0){
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel channel,String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            System.out.println(channel+" 写入 "+response);
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
