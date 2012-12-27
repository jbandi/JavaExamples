package jpaworkshop.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpaworkshop.model.Address;
import jpaworkshop.model.Employee;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmployeeTest {

    private static Logger logger = Logger.getLogger(EmployeeTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("jpaworkshop");
        em = emf.createEntityManager();
    }

    @After
    public void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    } 

    @Test
    public void explore_embedded_object() throws Exception {

    	em.getTransaction().begin();

    	// Construct object-graph
    	Employee emp = new Employee();
    	emp.setName("Simon Martinelli");
    	emp.setSalary(80000);
    	
    	em.persist(emp);

    	Address address = new Address();
    	address.setCity("Bern");
    	address.setState("BE");
    	address.setStreet("Frankenstrasse 70");
    	address.setZip("3018");

    	emp.setAddress(address); 

//    	Address billingAddress = new Address();
//    	billingAddress.setCity("Zuerich");
//    	billingAddress.setState("ZH");
//    	billingAddress.setStreet("technoparkstrasse 1");
//    	billingAddress.setZip("8050");
//    	
//    	emp.setBillingAddress(billingAddress); 

    	em.getTransaction().commit();
    	em.clear();
    	em.getTransaction().begin();

    	// Check object-graph 
        emp = em.find(Employee.class, emp.getId());
        assertNotNull(emp);
        assertNotNull(emp.getAddress());
        
        em.getTransaction().commit();
    	em.clear();
    	em.getTransaction().begin();
    	
    	// Remove address
        emp = em.find(Employee.class, emp.getId());
        assertNotNull(emp);
        emp.setAddress(null);
        em.getTransaction().commit();
    	em.getTransaction().begin();
    	
    	// Check removed address
        emp = em.find(Employee.class, emp.getId());    	
        assertNotNull(emp);
        assertNull(emp.getAddress());
        
        em.getTransaction().commit();
    }
   
}
