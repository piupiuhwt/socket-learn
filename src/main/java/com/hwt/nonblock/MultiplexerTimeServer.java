package com.hwt.nonblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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

                }
            }
        }
    }
}
