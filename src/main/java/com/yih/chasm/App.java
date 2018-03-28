package com.yih.chasm;

import com.yih.chasm.config.Config;
import com.yih.chasm.transport.Server;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        Config c = new Config();
        c.read();
        System.out.println(c.getEndPoints());
        Server server = new Server();
        server.start();
    }

    public String getGreeting() {
        return "Hello world.";
    }
}