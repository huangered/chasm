package com.yih.chasm.io;

import io.netty.buffer.ByteBuf;

public interface IVersonSerializer<T> {
    void serialize(T obj, ByteBuf buf);

    T deserialize(ByteBuf buf);
}
