package net.jonasbandi;

import javax.ejb.Local;

@Local
public interface HelloLocal {

    public String sayHello() throws Exception;
}
