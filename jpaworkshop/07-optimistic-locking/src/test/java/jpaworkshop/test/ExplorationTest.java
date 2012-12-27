package jpaworkshop.test;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;

import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExplorationTest {

    private static Logger logger = Logger.getLogger(ExplorationTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer employeeId;
    private Integer departmentId;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("jpaworkshop");
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Employee emp = new Employee();
            emp.setName("John Doe");
            emp.setSalary(80000);
            em.persist(emp);

            Department department = new Department();
            department.setName("PR");
            em.persist(department);

            em.getTransaction().commit();

            employeeId = emp.getId();
            departmentId = department.getId();

            em.clear();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @After
    public void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void check_optimistic_locking() throws Exception {

    	// First persistence context
        EntityManager em1 = emf.createEntityManager();
        em1.getTransaction().begin();

        // Load employee in first persistence context
        Employee emp1 = em1.find(Employee.class, employeeId);
        emp1.setName(emp1.getName() + "_updated1"); // Update employee in first persistence context
        
        // Second persistence context
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();

        // Load employee in second persistence context
        Employee emp2 = em2.find(Employee.class, employeeId);
        
        Assert.assertNotSame(emp1, emp2); // The same employee but loaded in two different persistence contexts
        
        emp2.setName(emp2.getName() + "_updated2"); // Update employee in second persistence context

        // Commit changes in first persistence context
        em1.flush();
        em1.getTransaction().commit();
        
        // Commit changes in second persistence context
        try {
        	em2.flush();
        	em2.getTransaction().commit();
        }
        catch (Exception e) {
        	logger.info("Optimistic locking prevented saving of updated entity! Exception: " + e);
        }
    }
    
    @Test
    public void check_optimistic_locking_for_collections() throws Exception {
    
    	// First persistence context
        EntityManager em1 = emf.createEntityManager();
        em1.getTransaction().begin();

        // Load department in first persistence context
        Department dep1 = em1.find(Department.class, departmentId);
        Employee emp1 = em1.find(Employee.class, employeeId);
        
        // Add the employee to the department in first persistence context 
        dep1.getEmployees().add(emp1); 
        // Question: Which entity is now changed? Department and/or Employee?
        
        // Second persistence context
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();

        // Load department in second persistence context
        Department dep2 = em2.find(Department.class, departmentId);
        Employee emp2 = em2.find(Employee.class, employeeId);
        
        Assert.assertNotSame(dep1, dep2); // The same department but loaded in two different persistence contexts
        Assert.assertNotSame(emp1, emp2); // The same employee but loaded in two different persistence contexts

        dep2.setName(dep2.getName() + "_updated2"); // Update department in second persistence context
        emp2.setName(emp2.getName() + "_updated2"); // Update employee in second persistence context

        // Commit changes in first persistence context
        em1.flush();
        em1.getTransaction().commit();
        
        // Commit changes in second persistence context
        try {
        	em2.flush();
        	Assert.fail("Flushing should fail, since the persistence context contains outdated entities!");
        }
        catch (Exception e) {
        	// Check why the exception is thrown! Because of the employee or the department?
        	logger.info("Optimistic locking prevented saving of updated entity! Exception: " + e);
        }
    }

    @Test(expected=OptimisticLockException.class)
    public void check_optimistic_locking_and_detaching() throws Exception {
    
    	//Get two instances of the same entity through two persistence-contexts
        EntityManager em1 = emf.createEntityManager();
        em1.getTransaction().begin();
        Employee emp1 = em1.find(Employee.class, employeeId);
                
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Employee emp2 = em2.find(Employee.class, employeeId);

        // Update the entity in the DB
        emp1.setName(emp1.getName() + "_update1");
        em1.getTransaction().commit();
        em1.close();
        
        em2.close();
        
        // Update the name on the detached entity
        emp2.setName(emp2.getName() + "_update2");
        
        // Merge the detached entity, there is already a newer version in the DB
        EntityManager em3 = emf.createEntityManager();
        em3.getTransaction().begin();
        
        em3.merge(emp2);
        Assert.fail("It should not be possible to merge an outdated entity!");
    }
}
