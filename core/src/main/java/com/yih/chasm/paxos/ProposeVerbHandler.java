package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProposeVerbHandler implements IVerbHandler<Commit> {
    @Override
    public synchronized void doVerb(MessageIn<Commit> in) {
        ProposeResponse pr = PaxosState.propose(in.payload);
        MessageOut<ProposeResponse> out = new MessageOut<>(pr, ProposeResponse.serializer, Phase.PAXOS_PROPOSE, in.traceId);
        PaxosService.instance().sendBack(out, in.from);
    }
}