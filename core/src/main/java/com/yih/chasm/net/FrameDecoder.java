package com.yih.chasm.net;

import com.yih.chasm.transport.Frame;
import com.yih.chasm.util.PsUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> results) {
        try {
            int version = msg.readInt();
            int opcode = msg.readInt();
            long traceId = msg.readLong();
            int length = msg.readInt();
            ByteBuf payload = PsUtil.createBuf(length);
            msg.readBytes(payload, length);
            results.add(new Frame(version, opcode, length, payload, traceId));
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
//            log.info("ddd {}", msg.refCnt());
//            log.info("ddd2 {}", msg.release());
        }
    }
}