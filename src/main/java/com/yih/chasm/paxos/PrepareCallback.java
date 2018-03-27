package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;

public class PrepareCallback extends AbstractPaxosCallback<PrepareResponse> {
    public boolean promised;

    public PrepareCallback(int count) {
        super(count);
    }

    @Override
    public void response(MessageIn<PrepareResponse> in) {
        PrepareResponse pr = in.payload;
        latch.countDown();
    }
}
