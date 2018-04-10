package com.yih.chasm.service;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.*;
import com.yih.chasm.paxos.*;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.util.PsUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PaxosService {


    public static EnumMap<Verb, IVersonSerializer<?>> versionSerializers = new EnumMap<Verb, IVersonSerializer<?>>(Verb.class) {
        {
            put(Verb.PAXOS_PREPARE, Commit.serializer);
            put(Verb.PAXOS_PROPOSE, Commit.serializer);
            put(Verb.PAXOS_COMMIT, Commit.serializer);
            put(Verb.REQUEST_RESPONSE, Commit.serializer);
        }
    };

    public static EnumMap<Verb, IVersonSerializer<?>> callbackSerializers = new EnumMap<Verb, IVersonSerializer<?>>(Verb.class) {
        {
            put(Verb.PAXOS_PREPARE, PrepareResponse.serializer);
            put(Verb.PAXOS_PROPOSE, ProposeResponse.serializer);
        }
    };

    public static EnumMap<Verb, IVerbHandler<?>> verbHandlers = new EnumMap<Verb, IVerbHandler<?>>(Verb.class) {
        {
            put(Verb.PAXOS_COMMIT, new PrepareVerbHandler());
            put(Verb.PAXOS_PREPARE, new PrepareVerbHandler());
            put(Verb.PAXOS_PROPOSE, new ProposeVerbHandler());
            put(Verb.REQUEST_RESPONSE, new TestHander());
        }
    };
    private static PaxosService service = new PaxosService();
    private final Map<String, IAsyncCallback<?>> callbacks = new HashMap<>();

    private final Map<EndPoint, Channel> channels = new HashMap<>();

    public static PaxosService instance() {
        return service;
    }

    public void putCallback(String key, IAsyncCallback callback) {
        callbacks.put(key, callback);
    }

    public IAsyncCallback getCallback(String key) {
        return callbacks.get(key);
    }

    public void removeCallback(String key) {
        callbacks.remove(key);
    }

    public IVerbHandler getVerbHandler(Verb verb) {
        return verbHandlers.get(verb);
    }

    public IVersonSerializer getSerializer(Verb verb) {
        return versionSerializers.get(verb);
    }

    public void registerChannel(EndPoint ep, Channel channel) {
        channels.put(ep, channel);
    }

    public void unregisterChannel(EndPoint ep) {
        channels.remove(ep);
    }

    public void sendRR(MessageOut<Commit> out, EndPoint endpoint) {
        ByteBuf buf = PsUtil.createBuf();
        out.serializer.serialize(out.payload, buf);
        Channel c = channels.get(endpoint);
        c.writeAndFlush(new Frame(1, out.verb.id, buf.readableBytes(), buf, Direction.REQUEST));
    }

    public void sendBack(MessageOut out, EndPoint endpoint) {
        ByteBuf buf = PsUtil.createBuf();
        out.serializer.serialize(out.payload, buf);
        Channel c = channels.get(endpoint);
        c.writeAndFlush(new Frame(1, out.verb.id, buf.readableBytes(), buf, Direction.RESPONSE));
    }

    @Slf4j
    public static class TestHander implements IVerbHandler<Commit> {

        @Override
        public void doVerb(MessageIn<Commit> in) {
            IAsyncCallback<Commit> callback = PaxosService.instance().getCallback(in.payload.getTraceId().toString());
            if (callback != null) {
                log.info("{}", callback);
            }

        }
    }


}