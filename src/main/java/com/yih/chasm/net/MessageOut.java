package com.yih.chasm.net;

import com.yih.chasm.service.PaxosService;
import lombok.Data;

import java.net.InetAddress;

@Data
public class MessageOut<T> {
    public InetAddress from;
    public T payload;

    public MessageOut(T payload) {
        this.payload = payload;
    }

    public static <M> MessageOut<M> create(PaxosService.Verb verb, M payload) {
        return new MessageOut(null);
    }
}
