package com.yih.chasm.net;

import com.yih.chasm.service.Verb;
import com.yih.chasm.transport.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> results) {

        int version = msg.readInt();
        int opcode = msg.readInt();
        int direct = msg.readInt();
        long traceId = msg.readLong();
        int length = msg.readInt();
        ByteBuf payload = msg.readBytes(length);
        results.add(new Frame(version, opcode, length, payload, Verb.values()[direct], traceId));

    }
}