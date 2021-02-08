package com.hwt.netty.test;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

public class NettyEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        out.add(Unpooled.wrappedBuffer(msg.getBytes()));
    }

}