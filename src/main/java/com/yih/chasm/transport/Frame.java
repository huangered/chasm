package com.yih.chasm.transport;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Frame {
    private int version;
    private int length;
    private ByteBuf payload;

    public Frame(int version, int length, ByteBuf payload) {
        this.version = version;
        this.length = length;
        this.payload = payload;
    }
}
