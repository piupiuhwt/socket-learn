package com.hwt.netty.server;

import com.hwt.netty.innerserver.NettyEncoder;
import com.hwt.netty.timesystem.TimerClientHandlerVisioPack;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class ClientServer {
    public void connect(int port,String host) throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try{
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NettyEncoder());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
//        int port = 9090;
//        new ClientServer().connect(port,"47.93.221.206");
        int port = 50000;
        new ClientServer().connect(port,"127.0.0.1");
    }
}
