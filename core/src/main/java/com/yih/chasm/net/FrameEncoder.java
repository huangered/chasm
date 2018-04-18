package com.yih.chasm.net;

import com.yih.chasm.transport.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameEncoder extends MessageToMessageEncoder<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, List<Object> out) {
        ByteBufAllocator allocator = new PooledByteBufAllocator(true);
        ByteBuf buf = allocator.buffer();
        buf.writeInt(msg.getVersion());
        buf.writeInt(msg.getPhase().id);
        buf.writeInt(msg.getDirect().id);
        buf.writeLong(msg.getTraceId());
        buf.writeInt(msg.getLength());

        out.add(buf);
        out.add(msg.getPayload());
    }
}