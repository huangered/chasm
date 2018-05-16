package com.yih.chasm.net.codec;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.net.EndPoint;
import com.yih.chasm.net.IAsyncCallback;
import com.yih.chasm.net.MessageIn;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.util.ChannelUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles a server-side channel.
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Frame> { // (1)


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame msg) {
        EndPoint ep = ChannelUtil.getEndPoint(ctx.channel());

        IVersonSerializer<?> serializer = PaxosService.instance().getCallbackSerializer(msg.getPhase());
        Object payload = serializer.deserialize(msg.getPayload());

        IAsyncCallback callback = PaxosService.instance().getCallback(Long.toString(msg.getTraceId()));
        MessageIn mi = MessageIn.create(ep, payload, msg.getPhase(), msg.getTraceId());
        if (callback!=null) {
            callback.response(mi);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}