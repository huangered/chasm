package com.yih.chasm.service;

import com.google.common.base.Strings;
import com.yih.chasm.config.Config;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PrepareCallback;
import com.yih.chasm.paxos.ProposeCallback;
import com.yih.chasm.paxos.SuggestionID;
import com.yih.chasm.transport.Server;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.List;

@Slf4j
public class StorageProxy {

    static int i = 0;
    Server server;
    Config config;

    public StorageProxy(Config config) {
        this.config = config;
        server = new Server(config.getPort());
    }

    public void run() {
        Thread sThread = new Thread(server);
        sThread.start();

//        Thread cThread = new Thread(clients);
//        cThread.start();

    }

    public void beginPaxos(long rnd, String str, String value) {

        Commit commit = Commit.newPrepare(new SuggestionID(rnd, str));

        PrepareCallback prepareCallback = preparePaxos(commit, config.getEndPoints());
        if (!prepareCallback.isPromised()) {
            log.info("paxos prepare fail");
            return;
        }

        if (Strings.isNullOrEmpty(prepareCallback.getResponse().getValue())) {
            prepareCallback.getResponse().setValue(value);
        }
        commit = Commit.newPropose(prepareCallback.getResponse().getRnd(), prepareCallback.getResponse().getValue());
        ProposeCallback proposeCallback = proposePaxos(commit, config.getEndPoints());
        if (proposeCallback.isSuccessful()) {
            log.info("paxos propose success");
        }
        learnPaxos(commit, config.getEndPoints());
    }

    private PrepareCallback preparePaxos(Commit toPrepare, List<SocketAddress> endpoints) {
        int traceId = genId();
        PrepareCallback prepareCallback = new PrepareCallback(requireNum(endpoints), toPrepare);
        PaxosService.instance().putCallback(Integer.toString(traceId), prepareCallback);
        for (SocketAddress endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(toPrepare, Commit.serializer, PaxosPhase.PAXOS_PREPARE, traceId);

            PaxosService.instance().sendRR(out, endpoint);
        }
        prepareCallback.awaitWithTime(config.getTtl());
        PaxosService.instance().removeCallback(Integer.toString(traceId));
        return prepareCallback;
    }

    private ProposeCallback proposePaxos(Commit toPropose, List<SocketAddress> endpoints) {
        int traceId = genId();
        ProposeCallback proposeCallback = new ProposeCallback(requireNum(endpoints));
        PaxosService.instance().putCallback(Integer.toString(traceId), proposeCallback);
        for (SocketAddress endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(toPropose, Commit.serializer, PaxosPhase.PAXOS_PROPOSE, traceId);

            PaxosService.instance().sendRR(out, endpoint);
        }
        proposeCallback.awaitWithTime(config.getTtl());
        PaxosService.instance().removeCallback(Integer.toString(traceId));
        return proposeCallback;
    }

    private void learnPaxos(Commit toLearn, List<SocketAddress> endpoints) {
        for (SocketAddress endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(toLearn, Commit.serializer, PaxosPhase.PAXOS_LEARN, 0L);

            PaxosService.instance().sendRR(out, endpoint);
        }
    }

    private int requireNum(List endpoints) {
        return endpoints.size();
    }

    private int genId() {
        return i++;
    }
}