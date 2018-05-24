package com.yih.chasm.io.nio;

import com.yih.chasm.io.ReaderInput;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DataReader implements ReaderInput {
    final int BUF_CAP = 1024 * 1024;

    public ByteBuffer read(String path) throws IOException {
        ByteBuffer bb = ByteBuffer.allocateDirect(BUF_CAP);

        FileInputStream fis = new FileInputStream(path);
        FileChannel channel = fis.getChannel();

        int len = channel.read(bb);
        return bb;
    }
}