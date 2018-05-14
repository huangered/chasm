package com.yih.chasm.net;

import com.yih.chasm.service.Phase;
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
}