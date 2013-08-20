package net.jonasbandi.ejbremote.server;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.security.RolesAllowed;

@Stateless
@Remote(HelloWorld.class)
@RolesAllowed("user")
public class HelloWorldBean implements HelloWorld {
    public String sayHello(){
        return "Hello World!";
    }
}
