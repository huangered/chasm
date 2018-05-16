package com.yih.chasm.net;

import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.PaxosPhase;

public class MessageDeliverTask implements Runnable {
    private final MessageIn<?> in;

    public MessageDeliverTask(MessageIn<?> in) {
        this.in = in;
    }

    @Override
    public void run() {
        PaxosPhase phase = in.phase;
        IVerbHandler handler = PaxosService.instance().getVerbHandler(phase);
        handler.doVerb(in);
    }
}