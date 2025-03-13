package com.hwt.netty.server.innerserver;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalContext {
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    public static ArrayBlockingQueue<ByteBuf> messageQueue = new ArrayBlockingQueue<>(10000);
}
