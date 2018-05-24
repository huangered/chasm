package com.yih.chasm.storage;

import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PaxosInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class StorageService {

    static StorageService instance = new StorageService();
    int currentId;
    BlockingQueue<PaxosInstance> queue = new LinkedBlockingQueue<>();

    public static StorageService instance() {
        return instance;
    }

    public void snapshot() {

    }

    public void write(Commit commit) {
        PaxosInstance paxosInstance = new PaxosInstance(currentId++, commit.getValue());
        log.info("storage offer {}", queue.offer(paxosInstance));
    }

    public void debugPrint() {
//        if (log.isDebugEnabled()) {
        for (PaxosInstance in : queue) {
            log.info("print instance {}", in);
        }
//        }
    }
}
