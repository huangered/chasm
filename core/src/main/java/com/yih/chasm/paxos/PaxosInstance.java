package com.yih.chasm.paxos;

import com.yih.chasm.paxos.SuggestionID;
import lombok.Data;

@Data
public class PaxosInstance {
    private long id;
    private String value;

    private SuggestionID promised;
    private SuggestionID accepted;

    public PaxosInstance(long id) {
        this.id = id;
    }

    public PaxosInstance(long id, String value) {
        this.id = id;
        this.value = value;
    }
}