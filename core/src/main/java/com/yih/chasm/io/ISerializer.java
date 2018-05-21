package com.yih.chasm.io;

public interface ISerializer<Input, Output> {
    void serialize(Input obj, Output buf);

    Input deserialize(Output buf);
}
