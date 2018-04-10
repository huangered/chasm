package com.yih.chasm.storage;

import com.yih.chasm.paxos.Commit;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class MetaService {

    private static Map<Integer, Value> valueMap = new HashMap<>();

    private static MetaService service = new MetaService();

    public static MetaService instance() {
        return service;
    }

    public void set(String key, String metaRow) {

    }

    public MetaRow get(Integer key) {
        return new MetaRow();
    }

    private void persist(String key, String metaRow) {

    }

    public Value  getValue(Integer key) {
        if (!valueMap.containsKey(key)) {
            valueMap.put(key, new Value());
        }
        return valueMap.get(key);
    }

    @Data
    public static class Value{
        private Commit acceptNumber = new Commit(0,0);
        private Commit acceptValue = new Commit(0,0);
    }
}

