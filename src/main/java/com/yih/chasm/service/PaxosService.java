package com.yih.chasm.service;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.IAsyncCallback;
import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PrepareVerbHandler;
import com.yih.chasm.paxos.ProposeVerbHandler;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.transport.Message;
import com.yih.chasm.util.PsUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
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
        }
    };
    public static EnumMap<Verb, IVerbHandler<?>> verbHandlers = new EnumMap<Verb, IVerbHandler<?>>(Verb.class) {
        {
            put(Verb.PAXOS_PREPARE, new PrepareVerbHandler());
            put(Verb.PAXOS_PROPOSE, new ProposeVerbHandler());
        }
    };
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

    public IVersonSerializer getSerializer(Verb verb){
        return versionSerializers.get(verb);
    }

    public void registerChannel(String address, Channel channel) {
        channels.put(address.toString(), channel);
    }

    public void sendPrepare(MessageOut<Commit> out, InetSocketAddress endpoint) {
        log.info("{}", out);
        ByteBuf buf = PsUtil.createBuf();
        Commit.serializer.serialize(out.payload, buf);
        Channel c = channels.get(endpoint.toString());
        c.writeAndFlush(new Frame(1, Message.Type.REP.opcode, buf.readableBytes(), buf));
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