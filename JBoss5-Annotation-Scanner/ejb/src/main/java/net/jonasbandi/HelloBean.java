package net.jonasbandi;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class HelloBean implements HelloLocal {

    @Override
    public String sayHello() throws Exception {

        System.out.println("Start scanning classpath ...");
        EarScanner earScanner = new EarScanner();
        List<String> annotatedClasses = earScanner.getClassesAnnotatedWith(Stateless.class);
        for(String className : annotatedClasses){
            System.out.println("Found class with annotation:" + className);
        }

        return "Hello";
    }
}
