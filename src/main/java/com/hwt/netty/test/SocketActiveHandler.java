package com.hwt.netty.test;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class SocketActiveHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("socket连接被激活服务端："+ctx.channel().localAddress());
        System.out.println("socket连接被激活客户端："+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("socket连接被关闭服务端："+ctx.channel().localAddress());
        System.out.println("socket连接被关闭客户端"+ctx.channel().remoteAddress());
    }
}
