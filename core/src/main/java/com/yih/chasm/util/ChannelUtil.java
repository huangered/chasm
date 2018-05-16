package com.yih.chasm.util;

import com.yih.chasm.net.EndPoint;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Slf4j
public class ChannelUtil {

    public static EndPoint getEndPoint(Channel channel) {
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return new EndPoint(address.getHostName(), address.getPort());

    }
}
