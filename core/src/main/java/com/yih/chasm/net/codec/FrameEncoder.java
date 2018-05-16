package com.yih.chasm.net.codec;

import com.yih.chasm.transport.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrameEncoder extends MessageToByteEncoder<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf buf) {
        int total = Frame.MinLen + msg.getLength() + 1;

        ByteBuf bf = Unpooled.buffer(total);
        try {
            Frame.serializer.serialize(msg, bf);
            buf.writeBytes(bf);
        } finally {
            msg.getPayload().release();
            bf.release();
        }
    }
}