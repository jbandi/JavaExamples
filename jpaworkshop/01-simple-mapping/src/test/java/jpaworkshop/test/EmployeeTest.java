package jpaworkshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeTest {

    private static Logger logger = Logger.getLogger(EmployeeTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer employeeId;
    private Integer departmentId;

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

    @Before
    public void setUp() {
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
        try {
            em.getTransaction().begin();

            Employee emp = em.find(Employee.class, employeeId);
            if (emp != null) {
                em.remove(emp);
            }
            Department department = em.find(Department.class, departmentId);
            if (department != null) {
                em.remove(department);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Test
    public void insertEmployee() throws Exception {
        logger.info("insertEmployee");

        em.getTransaction().begin();

        Employee emp = new Employee();

        emp.setName("Peter Muster");
        emp.setSalary(90000);

        logger.info("Id before persist: " + emp.getId());
        em.persist(emp);
        logger.info("Id after persist: " + emp.getId());

        em.getTransaction().commit();
        logger.info("Id after commit: " + emp.getId());

        emp = em.find(Employee.class, emp.getId());

        assertNotNull(emp);

        em.getTransaction().begin();
        em.remove(emp);
        em.getTransaction().commit();
    }

    @Test
    public void findEmployee() throws Exception {
        logger.info("findEmployee");

        em.clear();

//        Employee emp = em.find(Employee.class, employeeId);
        Query query = em.createQuery("select emp from Employee emp where emp.id = :id");
        query.setParameter("id", employeeId);
        Employee emp = (Employee) query.getSingleResult();
        
		assertNotNull(emp);
    }
    
    @Test
    public void findDepartment() throws Exception {
    	logger.info("findDepartment");
    	
    	em.clear();
    	
//    	Department dep = em.find(Department.class, departmentId);
        Query query = em.createQuery("select dep from Department dep where dep.id = :id");
        query.setParameter("id", departmentId);
        Department dep = (Department) query.getSingleResult();
    	
    	assertNotNull(dep);
    }

    @Test
    public void updateEmployee() throws Exception {
        logger.info("updateEmployee");

        em.getTransaction().begin();

        Employee emp = em.find(Employee.class, employeeId);
        assertNotNull(emp);

        emp.setSalary(emp.getSalary() + 10000);

        em.getTransaction().commit();

        emp = em.find(Employee.class, employeeId);

        assertEquals(90000, emp.getSalary());
    }

    @Test
    public void deleteEmployee() throws Exception {
        try {
            logger.info("deleteEmployee");

            em.getTransaction().begin();

            Employee emp = new Employee();
            emp.setName("Peter Muster");
            emp.setSalary(90000);
            em.persist(emp);

            em.getTransaction().commit();

            em.clear();
            em.getTransaction().begin();

            emp = em.find(Employee.class, emp.getId());
            em.remove(emp);

            em.getTransaction().commit();

            emp = em.find(Employee.class, emp.getId());

            assertNull(emp);
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
}
