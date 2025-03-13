package com.hwt.netty.server;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.nio.file.NoSuchFileException;
import java.util.Objects;


public class ClientHandler extends ChannelHandlerAdapter {

    static final String messageSeparator = "\r\n";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] responseBytes = handleMessage((String) msg);
        ctx.writeAndFlush(responseBytes);
    }

    private byte[] handleMessage(String message) throws NoSuchFileException {
        if (message == null|| message.trim().length() == 0) {
            throw new RuntimeException("null message");
        }
        System.out.println(message);
        URLEntity urlEntity = urlPasser(message);
        byte[] bodyBytes = getResponseBody(urlEntity);
        String responseHeader = getResponseHeader(urlEntity,bodyBytes.length);
        return bytesMarge(Objects.requireNonNull(responseHeader).getBytes(), bodyBytes);
    }

    private byte[] getResponseBody(URLEntity urlEntity) throws NoSuchFileException {
        int type = urlEntity.getType();
        if (type == 0) {
            return STANDARD_RESULT.getBytes();
        }
        return FileUtil.getResourceFileBytes(urlEntity.getUrl());
    }

    private String getResponseHeader(URLEntity urlEntity,int length) {
        int type = urlEntity.getType();
        String contentType = type == 0?"text/html":urlEntity.getMimeType();
        StringBuilder responseHeader = new StringBuilder();
        responseHeader.append("HTTP/1.1 200 OK")
                .append(messageSeparator)
                .append("Transfer-Encoding: chunked")
                .append(messageSeparator)
                .append("Content-Type: ")
                .append(contentType)
                .append(messageSeparator)
                .append("Content-Length: ")
                .append(length)
                .append(messageSeparator)
                .append(messageSeparator);
        return responseHeader.toString();
    }

    /**
     * 根据轻轨头获取url
     * @param message 请求信息
     * @return url实体
     */
    private URLEntity urlPasser(String message){
        URLEntity urlEntity = new URLEntity();
        String[] messageLines = message.split(messageSeparator);
        String[] headerValues = messageLines[0].split(" ");
        String url = headerValues[1];
        if (url.contains(".")) {
            String[] urlParts = url.split(".");
            String suffix = urlParts[urlParts.length - 1];
            String mimeType = MIMEConstant.mimeCacheMap.get(suffix);
            if (mimeType != null) {
                urlEntity.setType(1);
                urlEntity.setMimeType(mimeType);
            }
        }
        urlEntity.setUrl(url);
        return urlEntity;
    }

    /**
     * 将两个byte数组合并成一个
     * @param pre 前方数组
     * @param suffix 后方数组
     * @return 合并数组
     */
    private byte[] bytesMarge(byte[] pre,byte[] suffix){
        int preLength = pre.length;
        int suffixLength = suffix.length;
        byte[] result = new byte[suffixLength + preLength];
        System.arraycopy(pre, 0, result, 0, preLength);
        System.arraycopy(suffix, 0, result, preLength, suffixLength);
        return result;
    }

    public static final String STANDARD_RESULT =
            "<!DOCTYPE html>\n" +
            "<html lang=\"zh\">\n" +
            "    <head>\n" +
            "        <title>rate</title>\n" +
            "        <link href=\"car.css\" type=\"text/css\" rel=\"stylesheet\"/>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <div id=\"container\">\n" +
            "            <div id=\"car\">\n" +
            "                <img src=\"img/1).jpg\"/>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div id=\"cache\" style=\"display: none\">\n" +
            "\n" +
            "        </div>\n" +
            "        <script>\n" +
            "            let path = \"img/\",\n" +
            "                id = \"car\",\n" +
            "                num_of_images = 36,\n" +
            "                type_of_images = \".jpg\";\n" +
            "            let i = 1;\n" +
            "            for(let k = 1; k<num_of_images;k++){\n" +
            "                document.getElementById(\"cache\")\n" +
            "                    .innerHTML+=`<img src='${path}${k})${type_of_images}'/>`;\n" +
            "            }\n" +
            "            let img = document.querySelector(\"#car img\");\n" +
            "            window.addEventListener('wheel',function (e) {\n" +
            "                console.log(e);\n" +
            "                // e.preventDefault();\n" +
            "                e.wheelDelta / 120 > 0 ? next() : prev();\n" +
            "            });\n" +
            "\n" +
            "            function prev() {\n" +
            "                if(i === 1){\n" +
            "                    i=36;\n" +
            "                    img.src = `${path}${i})${type_of_images}`;\n" +
            "                }\n" +
            "                if(i>=1){\n" +
            "                    img.src = `${path}${--i})${type_of_images}`;\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            function next() {\n" +
            "                if(i === num_of_images){\n" +
            "                    i=1;\n" +
            "                    img.src = `${path}${i})${type_of_images}`;\n" +
            "                }\n" +
            "                if(i>=1){\n" +
            "                    img.src = `${path}${++i})${type_of_images}`;\n" +
            "                }\n" +
            "            }\n" +
            "        </script>\n" +
            "    </body>\n" +
            "</html>";
}
