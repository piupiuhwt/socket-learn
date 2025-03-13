package com.hwt.nonblock.timesystem;

import sun.misc.Unsafe;
import sun.reflect.Reflection;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class OnlyNoBlocking {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel acceptor = ServerSocketChannel.open();
        acceptor.bind(new InetSocketAddress(4488));
        acceptor.configureBlocking(false);
        while(true){
            SocketChannel channel = acceptor.accept();
            if (channel == null) {
                System.out.println("channel is null");
                Unsafe unsafe = getUnsafe();
                unsafe.park(false,1000_000_000L);
                continue;
            }
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            channel.configureBlocking(false);
            int read = channel.read(allocate);
            if (read > 0) {
                allocate.flip();
                byte[] bytes = new byte[allocate.remaining()];
                allocate.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.out.println("body:"+body);
                break;
            }
            System.out.println("read is null==========");
        }
    }

    private static Unsafe getUnsafe(){
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(Unsafe.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
