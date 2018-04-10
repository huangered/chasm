package com.yih.chasm.transport;

import com.yih.chasm.net.EndPoint;
import com.yih.chasm.net.FrameDecoder;
import com.yih.chasm.net.FrameEncoder;
import com.yih.chasm.net.FrameMsgHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;

public class Client implements Runnable {
    protected Bootstrap bootstrap;
    protected Channel channel;

    private EndPoint ep;

    public Client(EndPoint ep) {
        this.ep = ep;
    }

    public void run() {
        connect();
    }

    public void connect() {
        establishConnection();
    }

    public void establishConnection() {
        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(io.netty.channel.socket.nio.NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true);

        // Configure the pipeline factory.
        bootstrap.handler(new Initializer());

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ep.getIp(), ep.getPort()));

        // Wait until the connection attempt succeeds or fails.
        channel = future.awaitUninterruptibly().channel();
        if (!future.isSuccess()) {
            bootstrap.group().shutdownGracefully();
//            throw new IOException("Connection Error", future.cause());
        }
    }

    private class Initializer extends ChannelInitializer<Channel> {
        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast("client-frame-decoder", new FrameDecoder());
            pipeline.addLast("client-frame-encoder", new FrameEncoder());

            pipeline.addLast("client-handler", new FrameMsgHandler());
        }
    }
}