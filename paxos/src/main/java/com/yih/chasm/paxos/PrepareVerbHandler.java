package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.service.PaxosPhase;
import com.yih.chasm.service.PaxosService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrepareVerbHandler implements IVerbHandler<Commit> {
    @Override
    public synchronized void doVerb(MessageIn<Commit> in) {
        PrepareResponse pr = PaxosState.prepare(in.payload);
        log.debug("receive pr {}", pr);
        MessageOut<PrepareResponse> out = new MessageOut<>(pr, PrepareResponse.serializer, PaxosPhase.PAXOS_PREPARE, in.traceId);
        PaxosService.instance().sendBack(out, in.from);
    }
}