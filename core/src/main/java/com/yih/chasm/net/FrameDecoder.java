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
        ChannelUtil.debug(msg);
        int len = msg.readByte();
        log.info("receive len {}", len);
        if (msg.readableBytes() < Frame.MinLen) {
            ChannelUtil.debugPrint(msg);
            return;
        }

        int version = msg.readInt();
        if (version != ApiVersion.Version.id) {
            throw new Exception("wrong version");
        }
        int opcode = msg.readInt();
        long traceId = msg.readLong();
        int length = msg.readInt();
//        ByteBuf payload = PsUtil.createBuf(length);
        if (length> msg.readableBytes()){
            log.info("wrong {} len {} > {}", ctx.name(), length, msg.readableBytes());
            ChannelUtil.debugPrint(msg);
            throw new Exception("wrong body");
        }
        ByteBuf payload = msg.readRetainedSlice(length);
        results.add(new Frame(version, opcode, length, payload, traceId));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}