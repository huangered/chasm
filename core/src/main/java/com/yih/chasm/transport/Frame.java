package com.yih.chasm.transport;

import com.yih.chasm.service.Phase;
import com.yih.chasm.service.Verb;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Frame {
    private int version;
    private Verb direct;
    private Phase phase;
    private long traceId;
    private int length;
    private ByteBuf payload;


    public Frame(int version, int verbCode, int length, ByteBuf payload, Verb direct, long traceId) {
        this.version = version;
        this.length = length;
        this.payload = payload;
        this.phase = Phase.values()[verbCode];
        this.direct = direct;
        this.traceId = traceId;
    }
}