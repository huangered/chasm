package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Verb;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProposeVerbHandler implements IVerbHandler<Commit> {
    @Override
    public void doVerb(MessageIn<Commit> in) {
        log.info("{}", in);
        MessageOut<ProposeResponse> out = new MessageOut<>(new ProposeResponse(in.payload.getTraceId()), ProposeResponse.serializer, Verb.PAXOS_PROPOSE);
        PaxosService.instance().sendBack(out, in.from);
    }
}