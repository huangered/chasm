package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.service.Phase;
import lombok.Data;

import java.net.InetAddress;

@Data
public class MessageOut<T> {
    public InetAddress from;
    public T payload;

    public IVersonSerializer<T> serializer;

    public Phase phase;

    private long tracingId;

    public MessageOut(T payload, IVersonSerializer<T> serializer, Phase phase, long tracingId) {

        this.payload = payload;
        this.serializer = serializer;
        this.phase = phase;
        this.tracingId = tracingId;
    }

    public static <M> MessageOut<M> create(Phase phase, M payload, long tracingId) {
        return new MessageOut(payload, null, phase, tracingId);
    }
}
