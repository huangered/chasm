package com.yih.chasm.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yih.chasm.config.Config;
import com.yih.chasm.net.EndPoint;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PrepareCallback;
import com.yih.chasm.paxos.ProposeCallback;
import com.yih.chasm.paxos.SuggestionID;
import com.yih.chasm.storage.MetaService;
import com.yih.chasm.transport.ClientPool;
import com.yih.chasm.transport.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class StorageProxy {

    static int i = 0;
    Server server;
    ClientPool clients;
    Config config;

    public StorageProxy(Config config) {
        this.config = config;
        server = new Server(config.getPort());

        clients = new ClientPool(Lists.newArrayList(config.getEndPoints()));
    }

    public void run() {
        Thread sThread = new Thread(server);
        sThread.start();

        Thread cThread = new Thread(clients);
        cThread.start();

    }

    public void beginPaxos(long rnd, String str, String value) {

        Commit commit = Commit.newPrepare(new SuggestionID(rnd, str));

        PrepareCallback summary = preparePaxos(commit, config.getEndPoints());
        if (!summary.isPromised()) {
            log.info("prepare paxos fail");
            return;
        }

        if (Strings.isNullOrEmpty(summary.getResponse().getValue())) {
            summary.getResponse().setValue(value);
        }
        commit = Commit.newPropose(summary.getResponse().getRnd(), summary.getResponse().getValue());
        ProposeCallback s2 = proposePaxos(commit, config.getEndPoints());
        log.info("propose {} data {}", s2.isSuccessful(), MetaService.valueMap);
    }

    private PrepareCallback preparePaxos(Commit toPrepare, List<EndPoint> endpoints) {
        int traceId = genId();
        PrepareCallback prepareCallback = new PrepareCallback(requireNum(endpoints), toPrepare);
        PaxosService.instance().putCallback(Integer.toString(traceId), prepareCallback);
        for (EndPoint endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(toPrepare, Commit.serializer, Phase.PAXOS_PREPARE, traceId);

            PaxosService.instance().sendRR(out, endpoint);
        }
        prepareCallback.await();
        PaxosService.instance().removeCallback(Integer.toString(traceId));
        return prepareCallback;
    }

    private ProposeCallback proposePaxos(Commit toPropose, List<EndPoint> endpoints) {
        int traceId = genId();
        ProposeCallback proposeCallback = new ProposeCallback(requireNum(endpoints));
        PaxosService.instance().putCallback(Integer.toString(traceId), proposeCallback);
        for (EndPoint endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(toPropose, Commit.serializer, Phase.PAXOS_PROPOSE, traceId);

            PaxosService.instance().sendRR(out, endpoint);
        }
        proposeCallback.await();
        PaxosService.instance().removeCallback(Integer.toString(traceId));
        return proposeCallback;
    }

    private int requireNum(List endpoints) {
        return endpoints.size() / 2 + 1;
    }

    private int genId() {
        return i++;
    }
}