package com.yih.chasm.service;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.EndPoint;
import com.yih.chasm.net.IAsyncCallback;
import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageOut;
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

    public static EnumMap<Phase, IVersonSerializer<? super Commit>> versionSerializers = new EnumMap<Phase, IVersonSerializer<? super Commit>>(Phase.class) {
        {
            put(Phase.PAXOS_PREPARE, Commit.serializer);
            put(Phase.PAXOS_PROPOSE, Commit.serializer);
        }
    };

    public static EnumMap<Phase, IVersonSerializer<?>> callbackSerializers = new EnumMap<Phase, IVersonSerializer<?>>(Phase.class) {
        {
            put(Phase.PAXOS_PREPARE, PrepareResponse.serializer);
            put(Phase.PAXOS_PROPOSE, ProposeResponse.serializer);
        }
    };

    public static EnumMap<Phase, IVerbHandler<?>> verbHandlers = new EnumMap<Phase, IVerbHandler<?>>(Phase.class) {
        {
            put(Phase.PAXOS_PREPARE, new PrepareVerbHandler());
            put(Phase.PAXOS_PROPOSE, new ProposeVerbHandler());
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

    public IVerbHandler getVerbHandler(Phase phase) {
        return verbHandlers.get(phase);
    }

    public IVersonSerializer getSerializer(Phase phase) {
        return versionSerializers.get(phase);
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
        c.writeAndFlush(new Frame(1, out.phase.id, buf.readableBytes(), buf, Verb.REQUEST, out.getTracingId()));
    }

    public void sendBack(MessageOut out, EndPoint endpoint) {
        ByteBuf buf = PsUtil.createBuf();
        out.serializer.serialize(out.payload, buf);
        Channel c = channels.get(endpoint);
        c.writeAndFlush(new Frame(1, out.phase.id, buf.readableBytes(), buf, Verb.RESPONSE, out.getTracingId()));
    }
}