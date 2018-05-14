package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
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
public class ClientHandler extends SimpleChannelInboundHandler<Frame> { // (1)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("channel active " + ctx.name() + " " + ctx.channel().remoteAddress());
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        PaxosService.instance().registerChannel(new EndPoint(address.getHostName(), address.getPort()), ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        PaxosService.instance().unregisterChannel(new EndPoint(address.getHostName(), address.getPort()));
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame msg) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

        IVersonSerializer<?> serializer = PaxosService.instance().getCallbackSerializer(msg.getPhase());
        Object payload = serializer.deserialize(msg.getPayload());

        IAsyncCallback callback = PaxosService.instance().getCallback(Long.toString(msg.getTraceId()));
        MessageIn mi = new MessageIn(new EndPoint(address.getHostName(), address.getPort()), payload, msg.getPhase(), msg.getTraceId());
        callback.response(mi);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}