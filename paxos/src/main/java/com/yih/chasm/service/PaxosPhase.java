package com.yih.chasm.service;

import java.util.HashMap;
import java.util.Map;

public enum PaxosPhase {

    PAXOS_PREPARE(0),
    PAXOS_PROPOSE(1),
    PAXOS_LEARN(2);
    public int id;

    private static Map<Integer, PaxosPhase> map = new HashMap<Integer, PaxosPhase>() {
        {
            put(0, PAXOS_PREPARE);
            put(1, PAXOS_PROPOSE);
            put(2, PAXOS_LEARN);
        }
    };

    PaxosPhase(int id) {
        this.id = id;
    }

    public static PaxosPhase get(Integer id){
        return map.get(id);
    }

}