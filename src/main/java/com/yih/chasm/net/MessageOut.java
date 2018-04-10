package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Verb;
import lombok.Data;

import java.net.InetAddress;

@Data
public class MessageOut<T> {
    public InetAddress from;
    public T payload;

    public IVersonSerializer<T> serializer;

    public Verb verb;

    public MessageOut(T payload, IVersonSerializer<T> serializer, Verb verb) {

        this.payload = payload;
        this.serializer = serializer;
        this.verb = verb;
    }

    public static <M> MessageOut<M> create(Verb verb, M payload) {
        return new MessageOut(payload, null, verb);
    }
}
