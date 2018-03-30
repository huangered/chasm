package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.service.PaxosService;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Data
public class MessageIn<T> {
    public final InetSocketAddress from;
    public final T payload;
    public final PaxosService.Verb verb;


    public static <M> MessageIn<M> create(InetSocketAddress from, M payload, PaxosService.Verb verb) {
        return new MessageIn<>(from, payload, verb);
    }

    public static <M> MessageIn<M> read(ByteBuf buf) {

        PaxosService.Verb v = PaxosService.Verb.values()[buf.readInt()];
        IVersonSerializer handler = PaxosService.instance().getSerializer(v);
        Object payload = handler.deserialize(buf);

        return new MessageIn(null, payload, v);
    }
}