package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrepareCallback extends AbstractPaxosCallback<PrepareResponse> {
    public boolean promised;

    public PrepareCallback(int count) {
        super(count);
        promised = false;
    }

    @Override
    public void response(MessageIn<PrepareResponse> in) {
        log.info("handle response {}", in);
        PrepareResponse pr = in.payload;
        if (pr.isPromised()) {
            latch.countDown();
            if (latch.getCount() == 0) {
                promised = true;
            }
        }
    }
}