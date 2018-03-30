package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrepareVerbHandler implements IVerbHandler<Commit> {
    @Override
    public void doVerb(MessageIn<Commit> in) {
        log.info("{}", in);
    }
}
