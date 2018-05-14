package com.yih.chasm.util;

import com.yih.chasm.net.EndPoint;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public class ChannelUtil {

    public static EndPoint getEndPoint(Channel channel) {
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return new EndPoint(address.getHostName(), address.getPort());

    }
}
