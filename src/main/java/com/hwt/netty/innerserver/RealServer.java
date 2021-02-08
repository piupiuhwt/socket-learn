package com.hwt.netty.innerserver;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

@Data
public class RealServer {
    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private RealServerHandler realServerHandler = new RealServerHandler();

    public void bind(int port){
        ServerBootstrap bootstrap = new ServerBootstrap();
        try{
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_BACKLOG,1000)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyEncoder())
                                    .addLast(new RealServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
