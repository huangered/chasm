package com.yih.chasm.service;

public enum Direction {
    REQUEST(0),
    RESPONSE(1);

    public int id;

    Direction(int id) {
        this.id = id;
    }
}
