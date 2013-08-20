JBoss EJB Remoting Example
======================

This example demonstrates how to call EJBs from a remote client.
The example goes a bit further than the [ejb-remote](https://github.com/jboss-jdf/jboss-as-quickstart/tree/master/ejb-remote) quickstart provided by JBoss:

* The EJB is deployed in a EAR
* The EJB has a @RolesAllowed annotation
* The client demonstrates the two different remoting strategies:
  -  Looking up the bean by "standard" JNDI
  -  Using the JBoss proprietary EJB client API
* Passing security credentials in the client
* Registering a client interceptor

The example was bootstraped with the jboss-javaee6-webapp-ear-blank-archetype Maven Archetype (619).

Running the Example
-------------------

Add the application user by running the script:

		For Linux:   JBOSS_HOME/bin/add-user.sh
		For Windows: JBOSS_HOME\bin\add-user.bat
		
Add the following user:

		Username: ejbuser
		Password: ejbuser123!
		Roles:    user
		
Build and deploy the server component (EAR):

        mvn clean package jboss-as:deploy		

Run the client:

		cd client
		mvn clean compile exec:exec