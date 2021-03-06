package com.yih.chasm;

import com.yih.chasm.config.Config;
import com.yih.chasm.service.StorageProxy;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        Config c = new Config();
        c.read();
        System.out.println(c.getEndPoints());
        System.out.println(c.getPort());


        StorageProxy sp = new StorageProxy(c);
        sp.run();
//        Thread.sleep(5000);
//        sp.beginPaxos(1, "x");

    }
}
