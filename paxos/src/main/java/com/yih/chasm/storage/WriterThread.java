package com.yih.chasm.storage;

import com.yih.chasm.io.WriterOutput;
import com.yih.chasm.io.nio.DataWriter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WriterThread implements Runnable, Closeable {

    private static WriterThread instance = new WriterThread();
    private final String NAME = "storage";
    private ConcurrentLinkedQueue<StorageData> queue = new ConcurrentLinkedQueue<>();
    private WriterOutput output;

    private WriterThread() {
        output = new DataWriter();
    }

    public static WriterThread instance() {
        return instance;
    }

    public void add(StorageData instance) {
        queue.offer(instance);
    }

    @Override
    public void run() {
        while (true) {
            StorageData commit = queue.poll();
            if (commit != null) {

                byte[] dst = StorageData.serializer.serialize(commit);
                try {
                    output.write(dst);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void close() {
        output.close();
    }
}
