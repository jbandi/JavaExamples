package net.jonasbandi.ejbremote.client;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;

public class ClientInterceptor implements EJBClientInterceptor {
    @Override
    public void handleInvocation(EJBClientInvocationContext context) throws Exception {
        System.out.println("############################ Client Interceptor: handleInvocation");
        context.sendRequest();
    }

    @Override
    public Object handleInvocationResult(EJBClientInvocationContext context) throws Exception {
        System.out.println("############################ Client Interceptor: handleInvocationResult");
        return context.getResult();
    }
}
