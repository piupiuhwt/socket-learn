package com.hwt.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import static com.hwt.netty.decoder.EchoConstant.DELIMITER;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {
    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String body = (String) msg;
        System.out.println("This is " + ++counter + "times receive client : [" + body + "]");
//        下方为delimiter方式
//        body += DELIMITER;
//        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
//        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
