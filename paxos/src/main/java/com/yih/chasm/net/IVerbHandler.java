package com.yih.chasm.net;

public interface IVerbHandler<T> {
    void doVerb(MessageIn<T> in);
}
