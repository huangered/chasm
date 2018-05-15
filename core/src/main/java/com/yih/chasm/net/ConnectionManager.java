package com.yih.chasm.net;

import com.yih.chasm.transport.Client;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionManager {

    static ConcurrentMap<EndPoint, Channel> pool = new ConcurrentHashMap<>();

    public static Channel get(EndPoint endPoint) {
        if (pool.containsKey(endPoint)) {
            return pool.get(endPoint);
        } else {
            Client client = new Client(endPoint);
            if (client.connect()) {
                pool.put(endPoint, client.getChannel());
            }
            return client.getChannel();
        }
    }

    public static void remove(EndPoint endPoint) {
        if (pool.containsKey(endPoint)) {
            pool.remove(endPoint);
        }
    }
}
