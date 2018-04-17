package com.yih.chasm.transport;

import com.yih.chasm.net.EndPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientPool implements Runnable {
    List<EndPoint> endPointList = new ArrayList<>();
    ExecutorService executor = Executors.newCachedThreadPool();

    public ClientPool(List<EndPoint> endPoints) {
        endPointList = endPoints;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Iterator<EndPoint> iter = endPointList.iterator();
            while (iter.hasNext()) {
                EndPoint ep = iter.next();
                Client client = new Client(ep);
                Future<Boolean> future = executor.submit(client);
                while (true) {
                    if (future.isDone()) {
                        try {
                            if (future.get()) {
                                iter.remove();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }
}