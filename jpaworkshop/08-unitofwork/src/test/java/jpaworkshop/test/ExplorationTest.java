package jpaworkshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExplorationTest {

    private static Logger logger = Logger.getLogger(ExplorationTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer departmentId1;
    private Integer departmentId2;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("jpaworkshop");
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Department department1 = new Department();
            department1.setName("PR");
            em.persist(department1);

            Department department2 = new Department();
            department2.setName("Taxes");
            em.persist(department2);

            em.getTransaction().commit();

            departmentId1 = department1.getId();
            departmentId2 = department2.getId();

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
    public void check_unit_of_work() throws Exception {
    	
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        logger.info("Unit of Work started!");

        Employee emp = new Employee();
        emp.setName("John Doe");
        emp.setSalary(80000);
        logger.info("Employee created!");
        em.persist(emp);
        logger.info("Employee persisted! Employee has id: " + emp.getId());
        
        Department dep1 = em.find(Department.class, departmentId1);
        dep1.setName(dep1.getName() + "_updated");
        
        // *** Issue a query. How does this change the unit of work? Inspect the timing of the DB-statements.
//        Query query = em.createQuery("select dep from Department dep where dep.id = :id");
//        query.setParameter("id", departmentId2);
//        Department dep2 = (Department) query.getSingleResult();
 
        // *** What is the difference in the DB-statements for the following line when the query above is executed or not?
//        emp.setName(emp.getName() + "_updated");
        
        // *** Start a new unit of work, does this effect the first unit of work?
//        EntityManager em2 = emf.createEntityManager();
//        em2.getTransaction().begin(); 
//        Query query2 = em2.createQuery("select dep from Department dep");
//        List<Department> departments = query2.getResultList();
//        em2.getTransaction().commit();  
        
        logger.info("Unit of Work ending!");       
        em.flush();
        em.getTransaction().commit();      
        
    }
}
