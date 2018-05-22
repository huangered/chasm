package com.yih.chasm.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager implements IService<Runnable> {
    ExecutorService service = Executors.newCachedThreadPool();

    static ThreadManager instance = new ThreadManager();

    public static ThreadManager instance() {
        return instance;
    }

    public void submit(Runnable runnable) {
        service.submit(runnable);
    }
}
