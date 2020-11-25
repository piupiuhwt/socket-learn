package com.hwt.nonblock;

import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import lombok.Data;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Data
public class SocketChannelService {


    public static void main(String[] args) {
        try {
            ServerSocketChannel acceptorSvr = ServerSocketChannel.open();
            acceptorSvr.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"),8088));
            acceptorSvr.configureBlocking(false);

            Selector selector = Selector.open();

            SelectionKey key = acceptorSvr.register(selector, SelectionKey.OP_ACCEPT);

            int num = selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();



            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                //todo io event
            }


            SocketChannel channel = acceptorSvr.accept();

            channel.configureBlocking(false);
            channel.socket().setReuseAddress(true);

            SelectionKey register = channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 8);
            int read = channel.read(byteBuffer);

            Object message = null;
            while (byteBuffer.hasRemaining()) {
                byteBuffer.mark();
//                message = decode(byteBuffer);
                message = byteBuffer.array();
                if (message == null) {
                    byteBuffer.reset();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
