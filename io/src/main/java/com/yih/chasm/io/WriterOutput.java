package com.yih.chasm.io;

import java.io.IOException;

public interface WriterOutput {
    void write(byte[] bytes) throws IOException;

    void close();
}
