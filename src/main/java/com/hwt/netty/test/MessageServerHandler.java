package com.hwt.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.net.SocketAddress;

public class MessageServerHandler extends ChannelHandlerAdapter implements ChannelHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf body = (ByteBuf) msg;
        byte[] bodyBytes = new byte[body.readableBytes()];
        body.readBytes(bodyBytes);
        System.out.println(new String(bodyBytes));
        String contentString = "<html><head><title>test</title></head><body><h1>测试页</h1></body></html>";
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
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println(socketAddress);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
