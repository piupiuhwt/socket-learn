package com.hwt.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateEvent;

public class IdleStateEventHandler extends ChannelInitializer{
    @Override
    protected void initChannel(Channel ch) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {

        }
        super.userEventTriggered(ctx, evt);
    }
}
