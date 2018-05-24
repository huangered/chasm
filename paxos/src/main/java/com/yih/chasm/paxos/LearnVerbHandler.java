package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LearnVerbHandler implements IVerbHandler<Commit> {
    @Override
    public synchronized void doVerb(MessageIn<Commit> in) {
        log.debug("learn pr {}", in.payload);
        PaxosState.learn(in.payload);

    }
}