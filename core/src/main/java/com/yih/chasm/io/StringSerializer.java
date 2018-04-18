package com.yih.chasm.io;

import io.netty.buffer.ByteBuf;

public class StringSerializer implements IVersonSerializer<String> {

    @Override
    public void serialize(String obj, ByteBuf buf) {
        buf.writeInt(obj.length());
        buf.writeBytes(obj.getBytes());
    }

    @Override
    public String deserialize(ByteBuf buf) {
        int size = buf.readInt();
        byte[] buf2 = new byte[size];
        buf.readBytes(buf2, 0, size);
        return new String(buf2);
    }
}