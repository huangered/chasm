package com.yih.chasm.mbean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Paxos implements PaxosMBean {

    private int count;

    @Override
    public void playFootball(String clubName) {

    }

    @Override
    public int getCount() {
        System.out.println("Return playerName " + this.count);
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}