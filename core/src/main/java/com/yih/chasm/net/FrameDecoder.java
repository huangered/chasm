package com.yih.chasm.net;

import com.yih.chasm.service.PaxosService;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.util.ApiVersion;
import com.yih.chasm.util.ChannelUtil;
import com.yih.chasm.util.PsUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameDecoder extends ByteToMessageDecoder {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("channel active " + ctx.name() + " " + ctx.channel().remoteAddress());
        EndPoint ep = ChannelUtil.getEndPoint(ctx.channel());
        PaxosService.instance().registerChannel(ep, ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EndPoint ep = ChannelUtil.getEndPoint(ctx.channel());
        PaxosService.instance().unregisterChannel(ep);
        super.channelInactive(ctx);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> results) {
        try {
            if (msg.readableBytes() < Frame.MinLen) {
                return;
            }
            int version = msg.readInt();
            if (version != ApiVersion.Version.id) {
                return;
            }
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