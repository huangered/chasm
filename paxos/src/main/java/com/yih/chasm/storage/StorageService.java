package com.yih.chasm.storage;

import com.yih.chasm.paxos.Commit;
import com.yih.chasm.paxos.PaxosInstance;
import com.yih.chasm.sql.ContentService;
import com.yih.chasm.sql.SimpleSql;
import com.yih.chasm.sql.SqlHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class StorageService implements Runnable {

    static StorageService instance = new StorageService();
    int currentId;
    ConcurrentLinkedQueue<PaxosInstance> queue = new ConcurrentLinkedQueue<>();

    public static StorageService instance() {
        return instance;
    }

    private SqlHandler handler = new SimpleSql();

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

    @Override
    public void run() {
        while (true) {
            PaxosInstance instance = queue.poll();
            if (instance != null) {
                handler.handle(instance.getValue());
            }
        }
    }
}