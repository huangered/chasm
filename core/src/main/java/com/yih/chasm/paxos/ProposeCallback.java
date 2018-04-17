package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;
import lombok.Data;

@Data
public class ProposeCallback extends AbstractPaxosCallback<ProposeResponse> {
    private boolean promised;

    public ProposeCallback(int count) {
        super(count);
    }

    @Override
    public void response(MessageIn<ProposeResponse> in) {
        ProposeResponse pr = in.payload;
        if (pr.isPromised()) {
            latch.countDown();
            if (latch.getCount() ==0) {
                promised =true;
            }
        }
    }
}
