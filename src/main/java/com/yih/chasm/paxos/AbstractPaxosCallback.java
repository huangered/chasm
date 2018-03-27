package com.yih.chasm.paxos;

import com.yih.chasm.net.IAsyncCallback;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractPaxosCallback<T> implements IAsyncCallback<T> {
    protected final CountDownLatch latch;

    public AbstractPaxosCallback(int count) {
        latch = new CountDownLatch(count);
    }

    public void await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
