package com.yih.chasm.net;

import com.yih.chasm.transport.Frame;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class OutboundTcpConnection implements Runnable {
    private String name;
    private Channel channel;
    private BlockingQueue<Frame> queue = new LinkedBlockingQueue<>();
    private volatile boolean enable;

    public OutboundTcpConnection(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
        this.enable = true;
    }

    public boolean write(Frame frame) {
        return queue.offer(frame);
    }

    public void close() {
        enable = false;
        channel.close();
    }

    @Override
    public void run() {
        while (enable) {
            if (log.isDebugEnabled()) {
                log.debug("name: {} size: {}", name, queue.size());
            }
            Frame head = queue.poll();
            if (head != null) {
                if (channel == null || !channel.isActive()) {
                    log.error("name: {} channel: {} error", name, channel);
                } else {
                    channel.writeAndFlush(head);
                }
            }
        }
    }
}