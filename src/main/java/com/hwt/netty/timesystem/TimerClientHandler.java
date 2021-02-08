package com.hwt.netty.timesystem;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class TimerClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger = Logger.getLogger(TimerClientHandler.class.getName());

    private final ByteBuf firstMessage;

    public TimerClientHandler(){
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body);
        //重复处理：
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        byte[] bytes = input.getBytes();
        ByteBuf buffer = Unpooled.buffer(bytes.length);
        buffer.writeBytes(bytes);
        ctx.writeAndFlush(buffer);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}
