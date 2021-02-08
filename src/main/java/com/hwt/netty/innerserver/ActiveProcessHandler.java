package com.hwt.netty.innerserver;

import io.netty.channel.Channel;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ActiveProcessHandler implements Runnable{

    @Override
    public void run() {
        while(true){
            Set<Map.Entry<String, Channel>> entries = GlobalContext.channelMap.entrySet();
            Iterator<Map.Entry<String, Channel>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Channel> next = iterator.next();
                String address = next.getKey();
                Channel client = next.getValue();
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                StringBuilder responseBuilder = new StringBuilder();
                responseBuilder.append("HTTP/1.1 200 OK\r\n")
                        .append("content-type: text/html; Charset=utf-8\n")
                        .append("cache-control: private,no-cache\n")
                        .append("\n")
                        .append(input);
                client.writeAndFlush(input);
            }
        }
    }
}
