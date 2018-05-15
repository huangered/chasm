package com.yih.chasm.net;

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
        ByteBuf bf = Unpooled.buffer();
        try {
            int total = Frame.MinLen + msg.getLength();

            log.info("encode {} len {} total {}", msg.getPhase(), msg.getLength(), total);

            bf.writeByte(total);
            bf.writeInt(msg.getVersion());
            bf.writeInt(msg.getPhase().id);
            bf.writeLong(msg.getTraceId());
            bf.writeInt(msg.getLength());
            bf.writeBytes(msg.getPayload());

            buf.writeBytes(bf);

        } finally {
            msg.getPayload().release();
            bf.release();
        }
    }
}