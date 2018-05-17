package com.yih.chasm.storage;

import com.yih.chasm.paxos.PaxosInstance;
import com.yih.chasm.util.BufUtil;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.ConcurrentLinkedDeque;

public class WriterThread implements Runnable {

    private ConcurrentLinkedDeque<PaxosInstance> queue = new ConcurrentLinkedDeque<>();

    public void add(PaxosInstance instance) {
        queue.push(instance);
    }

    @Override
    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                PaxosInstance instance = queue.pop();
                MetaService.instance().write(instance);
            }

        }
    }

    public static void main(String[] argc) {
        ConcurrentLinkedDeque<Integer> queue = new ConcurrentLinkedDeque<>();
        queue.push(1);
        queue.push(2);
        System.out.println(queue.pop());
        System.out.println(queue.pop());
    }
}