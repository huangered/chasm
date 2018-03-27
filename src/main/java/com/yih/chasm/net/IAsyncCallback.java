package com.yih.chasm.net;

public interface IAsyncCallback<T> {
    void response(MessageIn<T> in);
}