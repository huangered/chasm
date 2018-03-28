package com.yih.chasm.net;

import com.yih.chasm.transport.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler2 extends SimpleChannelInboundHandler<Frame> { // (1)

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ctx.write(msg);
//        ctx.flush();
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame msg) {
       System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}