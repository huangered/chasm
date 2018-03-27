package com.yih.chasm.net;

import com.yih.chasm.service.PaxosService;
import lombok.Data;

import java.net.InetAddress;

@Data
public class MessageIn<T> {
    public final InetAddress from;
    public final T payload;
    public final PaxosService.Verb verb;


    public static MessageIn create() {
        return new MessageIn(null, null, null);
    }

    public static MessageIn read() {
        return new MessageIn(null, null, null);
    }
}
