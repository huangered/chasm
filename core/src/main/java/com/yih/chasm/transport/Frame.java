package com.yih.chasm.transport;

import com.yih.chasm.service.Phase;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Frame {
    public static final int MinLen = 4 + 4 + 8 + 4;

    private int version;
    private Phase phase;
    private long traceId;
    private int length;
    private ByteBuf payload;


    public Frame(int version, int verbCode, int length, ByteBuf payload, long traceId) {
        this.version = version;
        this.length = length;
        this.payload = payload;
        this.phase = Phase.values()[verbCode];
        this.traceId = traceId;
    }
}