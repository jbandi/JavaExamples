package net.jonasbandi;

import javax.ejb.EJB;

public class HelloController {

    @EJB
    private HelloLocal helloService;
    private String name;
    private String greeting;

    public HelloController() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return greeting;
    }

    public String sendAction() throws Exception {
        greeting = helloService.sayHello();
        return "success";
    }
}