package com.yih.chasm.net;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.paxos.PrepareResponse;
import com.yih.chasm.paxos.ProposeResponse;
import com.yih.chasm.service.Direction;
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
public class FrameMsgHandler extends SimpleChannelInboundHandler<Frame> { // (1)
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
        log.info("{}", msg);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

        if (msg.getDirect() == Direction.RESPONSE) { // back
            IVersonSerializer<?> serializer = PaxosService.callbackSerializers.get(msg.getVerb());
            Object payload = serializer.deserialize(msg.getPayload());

            if (payload instanceof PrepareResponse) {
                PrepareResponse pr = (PrepareResponse) payload;
                IAsyncCallback callback = PaxosService.instance().getCallback(Integer.toString(pr.getTraceId()));
                MessageIn mi = new MessageIn(new EndPoint(address.getHostName(), address.getPort()), payload, msg.getVerb());
                callback.response(mi);
            } else if (payload instanceof ProposeResponse) {
                ProposeResponse pr = (ProposeResponse)payload;
                IAsyncCallback callback = PaxosService.instance().getCallback(Integer.toString(pr.getTraceId()));
                MessageIn mi = new MessageIn(new EndPoint(address.getHostName(), address.getPort()), payload, msg.getVerb());
                callback.response(mi);
            }
        } else if (msg.getDirect() == Direction.REQUEST) { // 1
            IVersonSerializer<?> serializer = PaxosService.versionSerializers.get(msg.getVerb());

            Object data = serializer.deserialize(msg.getPayload());
            log.info("{}", data);
            MessageIn cm = new MessageIn<>(new EndPoint(address.getHostName(), address.getPort()), data, msg.getVerb());
            Thread t = new Thread(new MessageDeliverTask(cm));
            t.start();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}