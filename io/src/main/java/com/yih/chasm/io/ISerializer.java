package com.yih.chasm.io;

public interface ISerializer<T> {
    byte[] serialize(T obj);
    T deserialize(byte[] data);
}
