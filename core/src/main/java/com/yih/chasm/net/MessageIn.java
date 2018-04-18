package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Phase;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class MessageIn<T> {
    public final EndPoint from;
    public final T payload;
    public final Phase phase;
    public final long traceId;

    public static <M> MessageIn<M> create(EndPoint from, M payload, Phase phase, long traceId) {
        return new MessageIn<>(from, payload, phase, traceId);
    }

    public static <M> MessageIn<M> read(ByteBuf buf) {

        Phase v = Phase.values()[buf.readInt()];
        IVersonSerializer handler = PaxosService.instance().getVersionSerializer(v);
        Object payload = handler.deserialize(buf);

        return new MessageIn(null, payload, v, 123);
    }
}