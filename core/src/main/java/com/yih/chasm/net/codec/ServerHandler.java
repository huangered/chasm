package com.yih.chasm.net.codec;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.MessageDeliverTask;
import com.yih.chasm.net.MessageIn;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.transport.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Handles a server-side channel.
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Frame> { // (1)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("channel active " + ctx.name() + " " + ctx.channel().remoteAddress());
        PaxosService.instance().registerChannel(ctx.channel().remoteAddress(), ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        PaxosService.instance().unregisterChannel(ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame msg) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

        IVersonSerializer<?> serializer = PaxosService.instance().getVersionSerializer(msg.getPhase());

        Object data = serializer.deserialize(msg.getPayload());
        log.debug("Receive data {}", data);
        MessageIn cm = new MessageIn<>(address, data, msg.getPhase(), msg.getTraceId());
        Thread t = new Thread(new MessageDeliverTask(cm));
        t.start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        ctx.close();
    }
}