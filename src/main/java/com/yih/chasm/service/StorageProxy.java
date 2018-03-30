package com.yih.chasm.service;

import com.yih.chasm.net.MessageOut;
import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PrepareCallback;
import com.yih.chasm.paxos.ProposeCallback;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

public class StorageProxy {

    public void beginPaxos(List<InetSocketAddress> endpoints) {
        String id = "abcd";
        int target = 5;

        Commit commit = new Commit(123);


        PrepareCallback summary = preparePaxos(commit, endpoints);
        if (!summary.promised) {

        }

        proposePaxos(commit, endpoints);
    }

    private PrepareCallback preparePaxos(Commit commit, List<InetSocketAddress> endpoints) {
        PrepareCallback prepareCallback = new PrepareCallback(5);
        PaxosService.instance().put("aaa", prepareCallback);
        MessageOut<Commit> out = new MessageOut<>(commit);
        for (InetSocketAddress endpoint : endpoints) {
            PaxosService.instance().sendPrepare(out, endpoint);
        }
        prepareCallback.await();
        return prepareCallback;
    }

    private void proposePaxos(Commit commit, List<InetSocketAddress> endpoints) {
        ProposeCallback proposeCallback = new ProposeCallback(5);
        PaxosService.instance().put("aaa", proposeCallback);
        MessageOut<Commit> out = new MessageOut<>(commit);
        for (InetSocketAddress endpoint : endpoints) {
            PaxosService.instance().sendPropose(out, endpoint);
        }
        proposeCallback.await();
    }
}