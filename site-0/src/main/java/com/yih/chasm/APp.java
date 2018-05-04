package com.yih.chasm;

import com.yih.chasm.config.Config;
import com.yih.chasm.paxos.PaxosState;
import com.yih.chasm.service.StorageProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@EnableAutoConfiguration
public class APp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(APp.class, args);
    }

    StorageProxy sp;

    @RequestMapping("/init")
    @ResponseBody
    String init() throws InterruptedException {
        Config c = new Config();
        c.read();
        System.out.println(c.getEndPoints());
        System.out.println(c.getPort());


        sp = new StorageProxy(c);
        sp.run();

        return "Hello World!";
    }

    @RequestMapping("/test/{rnd}")
    @ResponseBody
    String home(@RequestParam String data, @PathVariable Long rnd) {
        sp.beginPaxos(rnd,"dd", data);
        return "Hello World!";
    }
}
