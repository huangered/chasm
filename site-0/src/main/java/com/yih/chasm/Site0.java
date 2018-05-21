package com.yih.chasm;

import com.yih.chasm.config.Config;
import com.yih.chasm.service.StorageProxy;
import com.yih.chasm.storage.StorageService;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class Site0 {
    public static void main(String[] args) throws InterruptedException {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

            }
        });

        Config c = new Config();
        c.read();
        System.out.println(c.getEndPoints());
        System.out.println(c.getPort());


        StorageProxy sp = new StorageProxy(c);
        sp.run();
        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            sp.beginPaxos(i, "abcdefghijklmnoperestuvwxya" +i, "adfklsajfkskf" + i);
            Thread.sleep(1000);
        }
        StorageService.instance().debugPrint();

    }
}