package com.yih.chasm.transport;

import io.netty.buffer.ByteBuf;

public interface Codec<M> {

    M decode(ByteBuf body, int version);

    void encode(M t, ByteBuf dest, int version);

}