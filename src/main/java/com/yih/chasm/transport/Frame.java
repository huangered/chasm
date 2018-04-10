package com.yih.chasm.transport;

import com.yih.chasm.service.Direction;
import com.yih.chasm.service.Verb;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Frame {
    private int version;
    private int length;
    private ByteBuf payload;
    private Verb verb;

    private Direction direct;

    public Frame(int version, int verbCode, int length, ByteBuf payload, Direction direct) {
        this.version = version;
        this.length = length;
        this.payload = payload;
        this.verb = Verb.values()[verbCode];
        this.direct = direct;
    }
}