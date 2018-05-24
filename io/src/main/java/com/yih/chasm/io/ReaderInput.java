package com.yih.chasm.io;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ReaderInput {
    ByteBuffer read(String path) throws IOException;
}
