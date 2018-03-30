package com.yih.chasm.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

public class PsUtil {

    public static ByteBuf createBuf() {
        ByteBufAllocator allocator = new PooledByteBufAllocator(true);
        return allocator.buffer();
    }

    public static ByteBuf createBuf(int size) {
        ByteBufAllocator allocator = new PooledByteBufAllocator(true);
        return allocator.buffer(size);
    }

    public static void writeInt(int value, ByteBuf buf){
        buf.writeInt(value);
    }
}
