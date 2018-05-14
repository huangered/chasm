package com.yih.chasm.net;

import com.yih.chasm.transport.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrameEncoder extends MessageToByteEncoder<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf buf) {
        try {
            buf.writeInt(msg.getVersion());
            buf.writeInt(msg.getPhase().id);
            buf.writeLong(msg.getTraceId());
            buf.writeInt(msg.getLength());

            buf.writeBytes(msg.getPayload());
        }finally {
            msg.getPayload().release();
        }
    }
}