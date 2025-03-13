package com.hwt.netty.server.innerserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import static com.hwt.netty.server.innerserver.GlobalContext.messageQueue;


public class RealServerHandler extends ChannelHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(RealServerHandler.class);

    private volatile byte[] completeData = null;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ConcurrentHashMap<String, Channel> channelMap = GlobalContext.channelMap;
        if (channelMap.size()>0) {
            Collection<Channel> values = channelMap.values();
            Iterator<Channel> iterator = values.iterator();
            if (iterator.hasNext() && completeData!=null && completeData.length!=0) {
                Channel clientServer = iterator.next();
                clientServer.writeAndFlush(Unpooled.copiedBuffer(completeData));
                completeData = null;
                ByteBuf take = messageQueue.take();
                byte[] bodyBytes = new byte[take.readableBytes()];
                take.readBytes(bodyBytes);
                ctx.writeAndFlush(new String(bodyBytes));
                return;
            }
        }
        String contentString = "<html><head><title>test</title></head><body><h1>還未有服務注冊</h1></body></html>";
        int length = contentString.getBytes("UTF-8").length;
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 200 OK\r\n")
                .append("content-type: text/html; Charset=utf-8\n")
                .append("cache-control: private,no-cache\n")
                .append("content-length: " + length + "\n")
                .append("\n")
                .append(contentString);
        ctx.writeAndFlush(responseBuilder.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf body = (ByteBuf) msg;
        byte[] readBytes = new byte[body.readableBytes()];
        body.readBytes(readBytes);
        if (completeData == null) {
            completeData = readBytes;
            return;
        }
        byte[] newData = new byte[completeData.length+readBytes.length];
        System.arraycopy(completeData, 0, newData, 0, completeData.length);
        System.arraycopy(readBytes,0,newData,completeData.length,readBytes.length);
        completeData = newData;
    }
}
