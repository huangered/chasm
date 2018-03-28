package com.yih.chasm.net;

import com.yih.chasm.transport.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> results) {


        int version = msg.readInt();
        int length = msg.readInt();
        ByteBuf payload = msg.readBytes(length);
        results.add(new Frame(version, length, payload));

    }
}