package jpaworkshop;

import static org.junit.Assert.assertNotNull;


import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpaworkshop.model.Employee;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BeanValidationJPAIntegrationTest {

    private static Logger logger = Logger.getLogger(BeanValidationJPAIntegrationTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("jpaworkshop");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void insertEmployee() throws Exception {
        logger.info("insertEmployee");

        em.getTransaction().begin();

        Employee emp = new Employee();
        em.persist(emp);

        em.getTransaction().commit();

        emp = em.find(Employee.class, emp.getId());

        assertNotNull(emp);

        em.getTransaction().begin();
        em.remove(emp);
        em.getTransaction().commit();
    }
}
