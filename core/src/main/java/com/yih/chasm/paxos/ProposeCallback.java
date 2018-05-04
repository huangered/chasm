package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ProposeCallback extends AbstractPaxosCallback<ProposeResponse> {

    private AtomicInteger accepts = new AtomicInteger(0);

    public ProposeCallback(int count) {
        super(count);
    }

    @Override
    public synchronized void response(MessageIn<ProposeResponse> in) {
        ProposeResponse pr = in.payload;
        if (pr.isPromised()) {
            accepts.incrementAndGet();
        }
        latch.countDown();

        if (isSuccessful()) {
            while (latch.getCount() > 0) {
                latch.countDown();
            }
        }
    }

    public boolean isSuccessful() {
        return accepts.get() >= latch.getCount();
    }
}
