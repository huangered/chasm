package com.yih.chasm.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> results) {

        try {
            int i = msg.readableBytes();
            for (int j = 0; j < i; j++) {
                byte b = msg.readByte();
                System.out.println((char) b + ":" + b);

            }
            ctx.write(msg);
            ctx.flush();
        } finally {
            //ReferenceCountUtil.release(msg); // (2)
        }
    }
}
