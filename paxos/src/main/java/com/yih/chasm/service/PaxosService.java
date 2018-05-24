package com.yih.chasm.service;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.*;
import com.yih.chasm.paxos.*;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.util.ApiVersion;
import com.yih.chasm.util.BufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PaxosService {

    private static EnumMap<PaxosPhase, IVersonSerializer<?>> versionSerializers = new EnumMap<PaxosPhase, IVersonSerializer<?>>(PaxosPhase.class) {
        {
            put(PaxosPhase.PAXOS_PREPARE, Commit.serializer);
            put(PaxosPhase.PAXOS_PROPOSE, Commit.serializer);
            put(PaxosPhase.PAXOS_LEARN, Commit.serializer);
        }
    };

    private static EnumMap<PaxosPhase, IVersonSerializer<?>> callbackSerializers = new EnumMap<PaxosPhase, IVersonSerializer<?>>(PaxosPhase.class) {
        {
            put(PaxosPhase.PAXOS_PREPARE, PrepareResponse.serializer);
            put(PaxosPhase.PAXOS_PROPOSE, ProposeResponse.serializer);
        }
    };

    private static EnumMap<PaxosPhase, IVerbHandler<?>> verbHandlers = new EnumMap<PaxosPhase, IVerbHandler<?>>(PaxosPhase.class) {
        {
            put(PaxosPhase.PAXOS_PREPARE, new PrepareVerbHandler());
            put(PaxosPhase.PAXOS_PROPOSE, new ProposeVerbHandler());
            put(PaxosPhase.PAXOS_LEARN, new LearnVerbHandler());
        }
    };

    private static PaxosService service = new PaxosService();

    private final Map<String, IAsyncCallback<?>> callbacks = new HashMap<>();

    private final Map<SocketAddress, OutboundTcpConnection> channels = new HashMap<>();

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

    public IVerbHandler getVerbHandler(PaxosPhase phase) {
        return verbHandlers.get(phase);
    }

    public IVersonSerializer getVersionSerializer(PaxosPhase phase) {
        return versionSerializers.get(phase);
    }

    public IVersonSerializer getCallbackSerializer(PaxosPhase phase) {
        return callbackSerializers.get(phase);
    }

    public void registerChannel(SocketAddress ep, OutboundTcpConnection channel) {
        channels.put(ep, channel);
    }

    public void unregisterChannel(SocketAddress ep) {
        channels.remove(ep);
    }

    public void sendRR(MessageOut<Commit> out, SocketAddress endpoint) {
        ByteBuf buf = BufUtil.createBuf();
        out.serializer.serialize(out.payload, buf);
        Channel c = ConnectionManager.get(endpoint);
        if (c.isActive()) {
            c.writeAndFlush(new Frame(ApiVersion.Version.id, out.phase.id, buf.readableBytes(), buf, out.getTracingId()));
        } else {
            log.error("Channel {} error", endpoint);
        }
    }

    public void sendBack(MessageOut out, SocketAddress endpoint) {
        ByteBuf buf = BufUtil.createBuf();
        out.serializer.serialize(out.payload, buf);
        log.debug("send back {} {} {}", buf.readerIndex(), buf.writerIndex(), buf.readableBytes());

        OutboundTcpConnection c = channels.get(endpoint);
        c.write(new Frame(ApiVersion.Version.id, out.phase.id, buf.readableBytes(), buf, out.getTracingId()));

    }
}