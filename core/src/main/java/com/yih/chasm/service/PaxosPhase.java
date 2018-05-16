package com.yih.chasm.service;

public enum PaxosPhase {

    PAXOS_PREPARE(0),
    PAXOS_PROPOSE(1);

    public int id;

    PaxosPhase(int id) {
        this.id = id;
    }

}