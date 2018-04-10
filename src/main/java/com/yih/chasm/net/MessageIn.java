package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Verb;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class MessageIn<T> {
    public final EndPoint from;
    public final T payload;
    public final Verb verb;


    public static <M> MessageIn<M> create(EndPoint from, M payload, Verb verb) {
        return new MessageIn<>(from, payload, verb);
    }

    public static <M> MessageIn<M> read(ByteBuf buf) {

        Verb v = Verb.values()[buf.readInt()];
        IVersonSerializer handler = PaxosService.instance().getSerializer(v);
        Object payload = handler.deserialize(buf);

        return new MessageIn(null, payload, v);
    }
}