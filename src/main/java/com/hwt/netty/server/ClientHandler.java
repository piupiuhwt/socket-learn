package com.hwt.netty.server;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.rtsp.RtspResponseEncoder;

import java.nio.charset.StandardCharsets;


public class ClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result = handleMessage((String) msg);
        StringBuilder responseBuilder = new StringBuilder();
        RtspResponseEncoder
        responseBuilder.append("HTTP/1.1 200 OK\r\n")
                .append("content-type: text/html; Charset=utf-8\r\n")
                .append("Transfer-encoding:chunked")
//                .append("content-length:")
//                .append(result.getBytes(StandardCharsets.UTF_8).length)
                .append("\r\n")
                .append("\r\n")
                .append(result);
        ctx.writeAndFlush(responseBuilder.toString());
    }

    private String handleMessage(String message){
        System.out.println(message);
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>测试转发</title>\n" +
                "    <link rel=\"icon\" href=\"http://www.petsnet.cn/wp-content/uploads/2017/01/e76bc1db5286ebad2b8a5e3489fe1586.jpg\" sizes=\"32x32\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <button>\n" +
                "        this is a button\n" +
                "    </button>\n" +
//                "    <h2>HELLO</h2>\n" +
                "</body>\n" +
                "</html>";
    }
}
