package com.yih.chasm.util;

import com.yih.chasm.net.EndPoint;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ChannelUtil {

    public static EndPoint getEndPoint(Channel channel) {
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return new EndPoint(address.getHostName(), address.getPort());

    }

    public static void debug(ByteBuf buf){
        log.info("debug readable bytes {}" , buf.readableBytes());
        log.info("debug read index     {}", buf.readerIndex());
        log.info("debug write index    {}", buf.writerIndex());
        log.info("debug capacity       {}", buf.capacity());

    }

    public static void debugPrint(ByteBuf buf){
        int len = buf.readableBytes();
        byte[] bytes = new byte[len];

        buf.readBytes(bytes, 0, len);


        log.info("debug print: {}", bytes);

        String a ="";
        for (byte by:bytes) {
            if (by > '0' && by <'z') {
                a += (char)by;
            }
        }
        log.info("debug print try: {}", a);
    }
}
