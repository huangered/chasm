package com.yih.chasm.service;

public enum Verb {
    REQUEST_RESPONSE(0),
    PAXOS_PREPARE(1),
    PAXOS_PROPOSE(2),
    PAXOS_COMMIT(3);

    public static final Verb[] verbIdx;

    static {
        verbIdx = new Verb[4];
        for (Verb verb : Verb.values()) {
            verbIdx[verb.id] = verb;
        }
    }

    public int id;

    Verb(int id) {
        this.id = id;
    }
    
}