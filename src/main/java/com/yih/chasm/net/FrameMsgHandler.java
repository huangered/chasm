package com.yih.chasm.net;

import com.yih.chasm.paxos.Commit;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.transport.Frame;
import com.yih.chasm.transport.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Handles a server-side channel.
 */
@Slf4j
public class FrameMsgHandler extends SimpleChannelInboundHandler<Frame> { // (1)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("channel active " + ctx.name() + " " + ctx.channel().remoteAddress());
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        PaxosService.instance().registerChannel(address.toString(), ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame msg) {
        log.info("{}", msg);
//        log.info("{}", msg.getType().codec);
//        Message body = msg.getType().codec.decode(msg.getPayload(), 1);
     Commit commit =   Commit.serializer.deserialize(msg.getPayload());
        log.info("{}", commit);
        MessageIn<Commit> cm = new MessageIn<>(null, commit, PaxosService.Verb.PAXOS_PREPARE);
        Thread t = new Thread(new MessageDeliverTask(cm));
        t.start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}