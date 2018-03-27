package com.yih.chasm.net;

import lombok.Data;

import java.net.InetAddress;

@Data
public class MessageOut<T> {
    public InetAddress from;
    public T payload;

    public MessageOut(T payload) {
        this.payload = payload;
    }

    public static MessageOut create() {
        return new MessageOut(null);
    }

    public static MessageOut read() {
        return new MessageOut(null);
    }
}
