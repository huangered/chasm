package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;

public class ProposeCallback extends AbstractPaxosCallback<ProposeResponse> {
    public ProposeCallback(int count) {
        super(count);
    }

    @Override
    public void response(MessageIn<ProposeResponse> in) {
        ProposeResponse pr = in.payload;
        latch.countDown();
    }
}
