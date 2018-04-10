package com.yih.chasm.net;

import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Verb;

public class MessageDeliverTask implements Runnable {
    private final MessageIn<?> in;

    public MessageDeliverTask(MessageIn<?> in) {
        this.in = in;
    }

    @Override
    public void run() {
        Verb verb = in.verb;
        IVerbHandler handler = PaxosService.instance().getVerbHandler(verb);
        handler.doVerb(in);
    }
}