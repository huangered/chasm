package com.yih.chasm.mbean;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class PaxosJmxServer {
    public void init() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        Paxos mbean = new Paxos();
        ObjectName name = null;
        try {
            name = new ObjectName("com.yih.chasm.mbean:name=Paxos");

        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        try {
            server.registerMBean(mbean, name);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        }

    }
}
