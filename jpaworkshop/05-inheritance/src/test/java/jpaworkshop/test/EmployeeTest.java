package jpaworkshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.DesignProject;
import jpaworkshop.model.Employee;
import jpaworkshop.model.Project;
import jpaworkshop.model.QualityProject;

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
    public void tearDown() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }    
    
    @Test
    public void explore_inheritance() throws Exception {

        em.getTransaction().begin();

        Employee emp = new Employee();
        emp.setName("John Doe");
        emp.setSalary(80000);
        em.persist(emp);;

        Project project = new DesignProject();
        project.setName("Design");
        em.persist(project);

        emp.getProjects().add(project);
        project.getEmployees().add(emp);

        project = new QualityProject();
        project.setName("Quality");
        em.persist(project);
        
        emp.getProjects().add(project);
        project.getEmployees().add(emp);
        
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        // Polymorphic query
        Query query = em.createQuery("select proj from Project proj");
        List<Project> projects = query.getResultList();
        assertEquals(2, projects.size());
        
        query = em.createQuery("select emp from Employee emp where emp.id = :id");
        query.setParameter("id", emp.getId());
        emp = (Employee) query.getSingleResult();
        assertNotNull(emp);
        assertEquals(2, emp.getProjects().size());
        
        Object[] projectArray = emp.getProjects().toArray();
        logger.info("Project 1: " + projectArray[0].getClass());
        logger.info("Project 2: " + projectArray[1].getClass());

        em.getTransaction().commit();
    }
}
