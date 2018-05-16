package com.yih.chasm.net.codec;

import com.yih.chasm.net.ConnectionManager;
import com.yih.chasm.net.EndPoint;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.util.ApiVersion;
import com.yih.chasm.util.ChannelUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameDecoder extends ByteToMessageDecoder {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("channel active " + ctx.name());
        EndPoint ep = ChannelUtil.getEndPoint(ctx.channel());
        PaxosService.instance().registerChannel(ep, ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel inactive " + ctx.name());
        EndPoint ep = ChannelUtil.getEndPoint(ctx.channel());
        PaxosService.instance().unregisterChannel(ep);
        ConnectionManager.remove(ep);
        super.channelInactive(ctx);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> results) throws Exception {
        int len = msg.readByte();
        log.debug("{} receive len {}", ctx.name(), len);

        if (msg.readableBytes() < Frame.MinLen) {
            return;
        }

        int version = msg.readInt();
        if (version != ApiVersion.Version.id) {
            throw new Exception("wrong version");
        }
        int opcode = msg.readInt();
        long traceId = msg.readLong();
        int length = msg.readInt();
        ByteBuf payload = msg.readRetainedSlice(length);
        results.add(new Frame(version, opcode, length, payload, traceId));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        ctx.close();
    }
}