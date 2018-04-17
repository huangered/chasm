package com.yih.chasm.service;

public enum Phase {

    PAXOS_PREPARE(0),
    PAXOS_PROPOSE(1);

    public int id;

    Phase(int id) {
        this.id = id;
    }

}