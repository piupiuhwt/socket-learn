package com.hwt.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hwt.netty.server.innerserver.GlobalContext.channelMap;
import static com.hwt.netty.server.innerserver.GlobalContext.messageQueue;

public class ForwardServerHandler extends ChannelHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ForwardServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString();
        channelMap.put(address, channel);
        System.out.println("全局上下文放入channelMap："+address+":"+channel);
        log.info("全局上下文放入channelMap："+address+":"+channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf body = (ByteBuf) msg;
        ByteBuf byteBuf = Unpooled.copiedBuffer(body);
        super.channelRead(ctx,msg);
        messageQueue.add(byteBuf);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String address = ctx.channel().remoteAddress().toString();
        System.out.println("channel斷開："+ address);
        channelMap.remove(address);
        System.out.println("channelMap移除：" + address);
    }
}
