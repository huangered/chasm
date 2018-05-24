package com.yih.chasm.util;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugUtil {

    public static void debug(ByteBuf buf) {
        log.debug("debug readable bytes {}", buf.readableBytes());
        log.debug("debug read index     {}", buf.readerIndex());
        log.debug("debug write index    {}", buf.writerIndex());
        log.debug("debug capacity       {}", buf.capacity());

    }

    public static void debugPrint(ByteBuf buf) {
        int len = buf.readableBytes();
        byte[] bytes = new byte[len];

        buf.readBytes(bytes, 0, len);


        log.debug("debug print: {}", bytes);

        String a = "";
        for (byte by : bytes) {
            if (by > '0' && by < 'z') {
                a += (char) by;
            }
        }
        log.debug("debug print try: {}", a);
    }
}
