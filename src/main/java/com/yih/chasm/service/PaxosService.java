package com.yih.chasm.service;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.IAsyncCallback;
import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;
import com.yih.chasm.transport.Frame;
import io.netty.channel.Channel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PaxosService {


    public static EnumMap<Verb, IVersonSerializer<?>> versonSerializers = new EnumMap<Verb, IVersonSerializer<?>>(Verb.class) {
        {
            put(Verb.PAXOS_PREPARE, Commit.serializer);
            put(Verb.PAXOS_PROPOSE, Commit.serializer);
            put(Verb.PAXOS_COMMIT, Commit.serializer);
        }
    };
    public static EnumMap<Verb, IVerbHandler<?>> verbHandlers = new EnumMap<Verb, IVerbHandler<?>>(Verb.class);
    public final Map<String, IAsyncCallback<?>> callbacks = new HashMap<>();

    private final Map<String, Channel> channels =new HashMap<>();

    private static PaxosService service = new PaxosService();

    public static PaxosService instance() {
        return service;
    }

    public void put(String key, IAsyncCallback callback) {
        callbacks.put(key, callback);
    }

    public IAsyncCallback getCallback(String key) {
        return callbacks.get(key);
    }

    public IVerbHandler getVerbHandler(Verb verb) {
        return verbHandlers.get(verb);
    }

    public void registerChannel(String address, Channel channel) {
        channels.put(address.toString(), channel);
    }

    public void sendPrepare(MessageOut<Commit> out, InetSocketAddress endpoint) {
        Channel c = channels.get(endpoint.toString());
        c.writeAndFlush(new Frame(1, 0, 0, null));
    }

    public void sendPropose(MessageOut<Commit> out, InetSocketAddress endpoint) {

    }

    public enum Verb {
        REQUEST_RESPONSE,
        PAXOS_PREPARE,
        PAXOS_PROPOSE,
        PAXOS_COMMIT,
    }
}