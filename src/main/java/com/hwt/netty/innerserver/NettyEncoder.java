package com.hwt.netty.innerserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class NettyEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(msg.getBytes()));
    }

}