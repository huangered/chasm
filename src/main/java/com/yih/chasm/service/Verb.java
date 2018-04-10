package com.yih.chasm.service;

public enum Verb {
    REQUEST_RESPONSE(0),
    PAXOS_PREPARE(1),
    PAXOS_PROPOSE(2),
    PAXOS_COMMIT(3);

    public int id;

    Verb(int id) {
        this.id = id;
    }

}