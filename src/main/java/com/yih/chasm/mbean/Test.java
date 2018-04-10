package com.yih.chasm.mbean;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Test {
    public void test(){
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        Game mbean = new Game();
        ObjectName name = null;
        try {
            name = new ObjectName("com.yih.chasm.mbean:name=Game");
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
