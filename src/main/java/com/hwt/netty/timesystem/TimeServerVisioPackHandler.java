package com.hwt.netty.timesystem;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimeServerVisioPackHandler extends ChannelHandlerAdapter {
    private int counter;

    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("server receive order : " + body + ";the counter is : " + ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "Bad Order";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        ctx.close();
    }
}
