package net.jonasbandi.ejbremote.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.security.auth.login.LoginContext;
import java.lang.management.ManagementFactory;
import org.jboss.security.client.SecurityClient;
import org.jboss.security.client.SecurityClientFactory;

@Singleton
@Startup
@RunAs("user")
public class EE6ExampleSingleton implements EE6ExampleMXBean {
    private String status = "not started";
    private ObjectName objectName;
    private MBeanServer platformMBeanServer;

    @EJB
    HelloWorld helloWorld;

    @PostConstruct
    public void start()
    {
        status = "starting";
        try {
            objectName = new ObjectName("net.jonasbandi.ejbremote.server:type=" + this.getClass().getName());
            platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            platformMBeanServer.registerMBean(this, objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to register " + objectName + " into JMX:" + e);
        }
        status = "started & registered injmx";
    }

    @PreDestroy
    public void stop()
    {
        status = "stopping";
        try {
            platformMBeanServer.unregisterMBean(objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to unregister " + objectName + " from JMX:" + e);
        }
        status = "stopped & unregistered from jmx";
    }

    // This is a method which the mbean is exposing
    public String hello(String name) throws Exception {

        // Programmatically establish a security context, since the called EJB is protected by @RolesAllowed - @RunAs("user") does not work for MBeans, see: https://issues.jboss.org/browse/WFLY-981
        SecurityClient client = SecurityClientFactory.getSecurityClient();
        client.setSimple("ejbuser", "ejbuser123!");
        client.login();
        return "Hello " + name + ". " + helloWorld.sayHello();
    }

    // This is a read only attribute the mbean is exposing.
    // To make it writeable add a public void setStatus(String) to the interface and mbean implementation
    public String getStatus()
    {
        return status;
    }
}
