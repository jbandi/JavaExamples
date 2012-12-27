package jpaworkshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.Address;
import jpaworkshop.model.Department;
import jpaworkshop.model.DesignProject;
import jpaworkshop.model.Employee;
import jpaworkshop.model.Phone;
import jpaworkshop.model.Project;
import jpaworkshop.statistics.EmployeeStats;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class EmployeeTest {

    private static Logger logger = Logger.getLogger(EmployeeTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer employeeId;
    private Integer departmentId;
    private Integer projectId;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("jpaworkshop");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
//        if (em != null) {
//            em.close();
//        }
//        if (emf != null) {
//            emf.close();
//        }
    }

    @Before
    public void setUp() {
        try {
            em.getTransaction().begin();

            Employee emp = new Employee();
            emp.setName("Simon Martinelli");
            emp.setSalary(80000);
            em.persist(emp);

            Phone phone = new Phone();
            phone.setPhonenumber("031 999 99 99");
            phone.setType("Work");
            phone.setEmployee(emp);

            emp.getPhones().add(phone);

            Phone phone2 = new Phone();
            phone2.setPhonenumber("032 333 33 33");
            phone2.setType("Home");
            phone2.setEmployee(emp);

            emp.getPhones().add(phone2);

            Department department = new Department();
            department.setName("PR");
            em.persist(department);

            emp.setDepartment(department);

            Address address = new Address();
            address.setCity("Bern");
            address.setState("BE");
            address.setStreet("Frankenstrasse 70");
            address.setZip("3018");

            emp.setAddress(address);

            DesignProject project = new DesignProject();
            project.setName("Arcos");

            em.persist(project);

            em.getTransaction().commit();

            employeeId = emp.getId();
            departmentId = department.getId();
            projectId = project.getId();

            em.clear();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @After
    public void tearDown() {

//        try {
//            em.getTransaction().begin();
//
//            Employee emp = em.find(Employee.class, employeeId);
//            if (emp != null) {
//                em.remove(emp);
//            }
//            Department department = em.find(Department.class, departmentId);
//            if (department != null) {
//                em.remove(department);
//            }
//            Project project = em.find(Project.class, projectId);
//            if (project != null) {
//                em.remove(project);
//            }
//
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            em.getTransaction().rollback();
//        }
    }

    @Ignore
    public void constructorWithList() throws Exception {
        Query query = em.createQuery("SELECT NEW jpaworkshop.statistics.EmployeeStats(e.name, e.salary) FROM Employee e");
        query.setParameter("id", this.employeeId);
        
        List<EmployeeStats> statsList = query.getResultList();

        Assert.assertNotNull(statsList);
        Assert.assertTrue(statsList.size() > 0);
        
        for (EmployeeStats stats : statsList) {
            logger.info("Name: " + stats.getName());
        }
    }

    @Test
    public void insertEmployee() throws Exception {
        logger.info("insertEmployee");

        em.getTransaction().begin();

        Employee emp = new Employee();
        // Achtung! Darf nicht mehr gesetzt werden
        // emp.setId(2);
        emp.setName("Peter Muster");
        emp.setSalary(90000);
        em.persist(emp);

        em.getTransaction().commit();

        emp = em.find(Employee.class, emp.getId());

        assertNotNull(emp);

        em.getTransaction().begin();
        em.remove(emp);
        em.getTransaction().commit();

    }

    @Test
    public void findEmployee() throws Exception {
        logger.info("updateEmployee");

        em.clear();

        Employee emp = em.find(Employee.class, employeeId);
        Set<Phone> phones = emp.getPhones();

        assertTrue(phones.size() > 0);
        assertNotNull(emp);
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
