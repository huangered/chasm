package com.yih.chasm.storage;

import com.yih.chasm.paxos.PaxosInstance;
import com.yih.chasm.paxos.SuggestionID;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class PaxosInstanceService {

    private static Map<Long, PaxosInstance> paxosInst = new TreeMap<>();

    private static PaxosInstanceService instance = new PaxosInstanceService(0);

    private long curInstanceId;

    public PaxosInstanceService(long initIid) {
        this.curInstanceId = initIid;
    }

    public static PaxosInstanceService instance() {
        return instance;
    }

    public synchronized PaxosInstance createInstance() {
        log.info("Create instance");
        curInstanceId++;
        PaxosInstance instance = new PaxosInstance(curInstanceId);
        instance.setPromised(new SuggestionID(-1L, ""));
        paxosInst.put(curInstanceId, instance);
        return instance;
    }

    public PaxosInstance currentInstance() {
        PaxosInstance ii = paxosInst.get(curInstanceId);
        if (ii == null) {
            createInstance();
        }
        return paxosInst.get(curInstanceId);
    }

    public void write(PaxosInstance instance) {
        WriterThread.instance().add(new StorageData(instance.getId(), instance.getValue()));
    }

    public long curId() {
        return curInstanceId;
    }

    public void print() {
        log.info("=====");
        for (Map.Entry<Long, PaxosInstance> entry : paxosInst.entrySet()) {
            log.info("iid {} value {} accept {}", entry.getKey(), entry.getValue().getValue(), entry.getValue().getAccepted());
        }
    }
}