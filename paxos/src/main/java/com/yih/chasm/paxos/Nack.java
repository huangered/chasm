package com.yih.chasm.paxos;

import lombok.Data;

@Data
public class Nack {
    private String error;
    private String errorCode;
}
