package com.yih.chasm.transport;

import com.yih.chasm.net.EndPoint;
import com.yih.chasm.net.FrameDecoder;
import com.yih.chasm.net.FrameEncoder;
import com.yih.chasm.net.FrameMsgHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

@Slf4j
public class Client implements Callable<Boolean> {
    protected Bootstrap bootstrap;
    protected Channel channel;

    private EndPoint ep;

    public Client(EndPoint ep) {
        this.ep = ep;
    }


    public boolean connect() {
        return establishConnection();
    }

    public boolean establishConnection() {
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
            log.info("create endpoint channel fail, retry");
            bootstrap.group().shutdownGracefully();
            return false;
//            throw new IOException("Connection Error", future.cause());
        }
        return true;
    }

    @Override
    public Boolean call() throws Exception {
        return connect();
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