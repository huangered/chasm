package com.yih.chasm.storage;

import com.yih.chasm.paxos.Commit;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class MetaService {

    public static Map<Long, Value> valueMap = new HashMap<>();

    private static MetaService service = new MetaService();

    public static MetaService instance() {
        return service;
    }

    public boolean hasInstance(Long rnd) {
        return valueMap.containsKey(rnd);
    }

    public void createInstance(Long rnd) {
        Value v = new Value();
        valueMap.put(rnd, v);
    }

    public Value getByInstance(Long rnd) {
        if (!hasInstance(rnd)) {
            createInstance(rnd);
        }
        return valueMap.get(rnd);
    }

    @Data
    public static class Value {
       private String value = "";
    }
}

