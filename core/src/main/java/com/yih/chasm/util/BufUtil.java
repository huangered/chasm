package com.yih.chasm.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BufUtil {

    public static ByteBuf createBuf() {

        return Unpooled.buffer();
    }

    public static ByteBuf createBuf(int size) {
        return Unpooled.buffer(size);
    }

    public static void writeInt(int value, ByteBuf buf) {
        buf.writeInt(value);
    }
}
