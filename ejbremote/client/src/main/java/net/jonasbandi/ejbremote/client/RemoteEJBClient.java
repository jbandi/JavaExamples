package net.jonasbandi.ejbremote.client;

import net.jonasbandi.ejbremote.server.HelloWorld;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import javax.naming.*;
import java.util.Hashtable;
import java.util.Properties;


public class RemoteEJBClient {

    public static void main(String[] args) throws Exception {
        invokeStatelessBean();
    }

    private static void invokeStatelessBean() throws NamingException {

        /////////////////////////////
        // The "standard" JNDI lookup
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote://localhost:4447");
        jndiProperties.put(Context.SECURITY_PRINCIPAL, "ejbuser");
        jndiProperties.put(Context.SECURITY_CREDENTIALS, "ejbuser123!");
        jndiProperties.put("jboss.naming.client.ejb.context", true);
        final Context context = new InitialContext(jndiProperties);

        traverseJndiNode("/", context);
        final HelloWorld helloWorld = (HelloWorld) context.lookup("ejbremote-ear/ejbremote-ejb/HelloWorldBean!"+ HelloWorld.class.getName());
        System.out.println(helloWorld.sayHello());
        context.close();

        /////////////////////////////////////////////
        // Using the proprietary JBoss EJB Client API
        final Properties ejbProperties = new Properties();
        ejbProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        ejbProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        ejbProperties.put("remote.connections", "1");
        ejbProperties.put("remote.connection.1.host", "localhost");
        ejbProperties.put("remote.connection.1.port", "4447");
        ejbProperties.put("remote.connection.1.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        ejbProperties.put("remote.connection.1.username", "ejbuser");
        ejbProperties.put("remote.connection.1.password", "ejbuser123!");
        ejbProperties.put("org.jboss.ejb.client.scoped.context", "true");

        final EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(ejbProperties);
        final ConfigBasedEJBClientContextSelector selector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
        EJBClientContext.setSelector(selector);

        final Context ejbContext = new InitialContext(ejbProperties);
        final HelloWorld ejbHelloWorld = (HelloWorld) ejbContext.lookup("ejb:ejbremote-ear/ejbremote-ejb/HelloWorldBean!"+ HelloWorld.class.getName());
        System.out.println(ejbHelloWorld.sayHello());
    }

    private static void traverseJndiNode(String nodeName, Context context)  {
        try {
            NamingEnumeration<NameClassPair> list = context.list(nodeName);
            while (list.hasMore()){
                String childName = nodeName + "/" + list.next().getName();
                System.out.println(childName);
                traverseJndiNode(childName, context);
            }
        } catch (NamingException ex) {
            // We reached a leaf
        }
    }
}
