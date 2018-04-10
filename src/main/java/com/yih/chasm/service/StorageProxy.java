package com.yih.chasm.service;

import com.yih.chasm.config.Config;
import com.yih.chasm.net.EndPoint;
import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PrepareCallback;
import com.yih.chasm.paxos.ProposeCallback;
import com.yih.chasm.transport.Client;
import com.yih.chasm.transport.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StorageProxy {

    Server server;
    List<Client> clients = new ArrayList<>();
    Config config;

    public StorageProxy(Config config) {
        this.config = config;
        server = new Server(config.getPort());

        for (EndPoint ep : config.getEndPoints()) {
            Client client = new Client(ep);
            clients.add(client);
        }
    }

    public void run() {
        Thread sThread = new Thread(server);
        sThread.start();
        for (Client client : clients) {
            Thread cThread = new Thread(client);
            cThread.start();
        }
    }

    public void beginPaxos() {
        String id = "abcd";
        int target = 5;
        int n = 10;
        Commit commit = new Commit(123, n);


        PrepareCallback summary = preparePaxos(commit, config.getEndPoints());
        if (!summary.promised) {
            log.info("prepare paxos fail");
        }

        ProposeCallback s2 = proposePaxos(commit, config.getEndPoints());
        log.info("ok~~");
    }

    private PrepareCallback preparePaxos(Commit commit, List<EndPoint> endpoints) {
        PrepareCallback prepareCallback = new PrepareCallback(endpoints.size());
        PaxosService.instance().putCallback(commit.getTraceId().toString(), prepareCallback);
        for (EndPoint endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(commit, Commit.serializer, Verb.PAXOS_PREPARE);

            PaxosService.instance().sendRR(out, endpoint);
        }
        prepareCallback.awaitWithTime();
        PaxosService.instance().removeCallback(commit.getTraceId().toString());
        return prepareCallback;
    }

    private ProposeCallback proposePaxos(Commit commit, List<EndPoint> endpoints) {
        ProposeCallback proposeCallback = new ProposeCallback(endpoints.size());
        PaxosService.instance().putCallback(commit.getTraceId().toString(), proposeCallback);
        for (EndPoint endpoint : endpoints) {
            MessageOut<Commit> out = new MessageOut<>(commit, Commit.serializer, Verb.PAXOS_PROPOSE);

            PaxosService.instance().sendRR(out, endpoint);
        }
        proposeCallback.awaitWithTime();
        PaxosService.instance().removeCallback(commit.getTraceId().toString());
        return proposeCallback;
    }
}