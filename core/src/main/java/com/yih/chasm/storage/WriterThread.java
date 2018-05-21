package com.yih.chasm.storage;

import com.yih.chasm.util.BufUtil;
import io.netty.buffer.ByteBuf;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WriterThread implements Runnable, Closeable {

    final String NAME = "storage";

    private ConcurrentLinkedDeque<StorageData> queue = new ConcurrentLinkedDeque<>();

    static WriterThread instance = new WriterThread();
    FileOutputStream reader;

    public static WriterThread instance() {
        return instance;
    }

    private WriterThread() {
        try {
            reader = new FileOutputStream(NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void add(StorageData instance) {
        queue.push(instance);
    }

    @Override
    public void run() {
        while (true) {
            StorageData commit = queue.pollFirst();
            if (commit != null) {
                ByteBuf buf = BufUtil.createBuf();
                StorageData.serializer.serialize(commit, buf);
                byte[] bytes = buf.array();
                write(bytes);
                buf.release();
            }
        }
    }

    private void write(byte[] bytes) {
        try {
            reader.write(bytes);
            reader.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }
    }
}