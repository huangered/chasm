package com.yih.chasm.net;

import com.yih.chasm.service.PaxosPhase;
import lombok.Data;

import java.net.SocketAddress;

@Data
public class MessageIn<T> {
    public final SocketAddress from;
    public final T payload;
    public final PaxosPhase phase;
    public final long traceId;

    public static <M> MessageIn<M> create(SocketAddress from, M payload, PaxosPhase phase, long traceId) {
        return new MessageIn<>(from, payload, phase, traceId);
    }
}