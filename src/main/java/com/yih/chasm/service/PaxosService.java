package com.yih.chasm.service;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.IAsyncCallback;
import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;

import java.net.InetAddress;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PaxosService {


    public enum Verb {
        REQUEST_RESPONSE,
        PAXOS_PREPARE,
        PAXOS_PROPOSE,
        PAXOS_COMMIT,
    }

    public static EnumMap<Verb, IVersonSerializer<?>> versonSerializers = new EnumMap<Verb, IVersonSerializer<?>>(Verb.class) {
        {
            put(Verb.PAXOS_PREPARE, Commit.serializer);
            put(Verb.PAXOS_PROPOSE, Commit.serializer);
            put(Verb.PAXOS_COMMIT, Commit.serializer);
        }
    };

    public static EnumMap<Verb, IVerbHandler<?>> verbHandlers = new EnumMap<Verb, IVerbHandler<?>>(Verb.class);

    private static PaxosService service = new PaxosService();
    public final Map<String, IAsyncCallback<?>> callbacks = new HashMap<>();

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

    public void sendPrepare(MessageOut<Commit> out, InetAddress endpoint) {

    }

    public void sendPropose(MessageOut<Commit> out, InetAddress endpoint) {

    }
}