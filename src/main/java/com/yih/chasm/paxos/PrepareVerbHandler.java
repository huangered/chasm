package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.service.PaxosService;
import com.yih.chasm.service.Verb;
import com.yih.chasm.storage.MetaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrepareVerbHandler implements IVerbHandler<Commit> {
    @Override
    public void doVerb(MessageIn<Commit> in) {
        log.info("{}", in);

        MetaService.Value v = MetaService.instance().getValue(in.payload.getTraceId());
        log.info("{}", v);
        Commit re;
        if (in.payload.getProposalNumber() > v.getAcceptNumber().getProposalNumber()) {
            v.getAcceptNumber().setProposalNumber(in.payload.getProposalNumber());
            re = new Commit(in.payload.getTraceId(), in.payload.getProposalNumber());
        } else {
            re = new Commit(in.payload.getTraceId(), v.getAcceptNumber().getProposalNumber());
        }

        MessageOut<PrepareResponse> out = new MessageOut<>(new PrepareResponse(re.getTraceId(), true, re), PrepareResponse.serializer, Verb.PAXOS_PREPARE);
        PaxosService.instance().sendBack(out, in.from);
    }
}