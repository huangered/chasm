package com.yih.chasm.io.nio;

import com.yih.chasm.io.WriterOutput;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DataWriter implements WriterOutput {

    int BUF_CAP = 100;
    String path;

    public void write(byte[] bytes) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);

        FileOutputStream fis = new FileOutputStream(path);
        FileChannel channel = fis.getChannel();
        channel.write(bb);

    }
}