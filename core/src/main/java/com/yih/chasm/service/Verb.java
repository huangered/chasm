package com.yih.chasm.service;

public enum Verb {
    REQUEST(0),
    RESPONSE(1);

    public int id;

    Verb(int id) {
        this.id = id;
    }
}
