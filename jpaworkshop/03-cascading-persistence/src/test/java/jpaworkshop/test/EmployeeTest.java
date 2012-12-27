package jpaworkshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpaworkshop.model.Address;
import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;
import jpaworkshop.model.Phone;

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
    public void explore_cascading_persistence() throws Exception {

        em.getTransaction().begin();

        // Construct object-graph
        Department dep = new Department();
        dep.setName("PR");
        em.persist(dep);

        Employee emp = new Employee();
        emp.setName("Simon Martinelli");
        emp.setSalary(80000);

        dep.getEmployees().add(emp);
        emp.setDepartment(dep);
        em.persist(emp); // Persisting Employee is necessary even when it is added to the collection of Department

        Phone phone = new Phone();
        phone.setPhonenumber("031 999 99 99");
        phone.setType("Work");
        phone.setEmployee(emp);

        emp.getPhones().add(phone); // Persisting Phone is not necessary when it is added to the collection of Employee

        Phone phone2 = new Phone();
        phone2.setPhonenumber("032 333 33 33");
        phone2.setType("Home");
        phone2.setEmployee(emp);

        emp.getPhones().add(phone2); // Persisting Phone is not necessary when it is added to the collection of Employee

        Address address = new Address();
        address.setCity("Bern");
        address.setState("BE");
        address.setStreet("Frankenstrasse 70");
        address.setZip("3018");

        emp.setAddress(address); // Persisting Address is not necessary when it is referenced by Employee

        em.getTransaction().commit();
        em.clear();
        em.getTransaction().begin();

        // Check object-graph
        dep = em.find(Department.class, dep.getId());
        assertNotNull(dep);
        assertEquals(1, dep.getEmployees().size());
        emp = dep.getEmployees().iterator().next();
        assertEquals(2, emp.getPhones().size());
        assertNotNull(emp.getAddress());

        em.getTransaction().commit();
        em.clear();
        em.getTransaction().begin();

        address = em.find(Address.class, emp.getAddress().getId());
        assertNotNull(address);

        // Delete object-graph
        dep = em.find(Department.class, dep.getId());
        assertNotNull(dep);

        em.remove(dep);
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            // exception is thrown by DB, because there is a FK-Constraint Employee->Department
            logger.info("Caught a fk-constraint exception!");
        }
        em.clear();
        em.getTransaction().begin();

        emp = em.find(Employee.class, emp.getId());
        assertNotNull(emp);
        em.remove(emp);
        em.getTransaction().commit();
        em.getTransaction().begin();

        address = em.find(Address.class, emp.getAddress().getId());
        assertNull(address); // address was deleted even though no explicit remove() was called!
    }
}
