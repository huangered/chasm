package com.yih.chasm.net;

import com.yih.chasm.transport.Client;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionManager {

    static ConcurrentMap<EndPoint, Channel> map = new ConcurrentHashMap<>();

    public static Channel get(EndPoint endPoint) {
        if (map.containsKey(endPoint)) {
            return map.get(endPoint);
        } else {
            Client client = new Client(endPoint);
            if (client.connect()) {
                map.put(endPoint, client.getChannel());
            }
            return client.getChannel();
        }
    }
}
