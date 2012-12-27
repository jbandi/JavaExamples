package jpaworkshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmployeeTest {

    private static Logger logger = Logger.getLogger(EmployeeTest.class.toString());
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
    public void tearDown() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
    
    @Test
    public void add_and_remove_employee_to_department() throws Exception {

        em.getTransaction().begin();

        // Add the Employee to the Department
        Department dep = em.find(Department.class, departmentId);
        Employee emp = em.find(Employee.class, employeeId);
        assertNotNull(dep);
        assertNotNull(emp);

        Employee emp2 = new Employee();
        emp2.setName("2");
        em.persist(emp2); // How to make this statement obsolete? Tip: cascading persistence
        
        dep.getEmployees().add(emp);
        dep.getEmployees().add(emp2);

        em.getTransaction().commit();
        em.clear();
        em.getTransaction().begin();
        
        // Check that the Employee was added
        Query query = em.createQuery("select dep from Department dep where dep.id = :id");
        query.setParameter("id", departmentId);
        dep = (Department) query.getSingleResult();
        assertEquals(2, dep.getEmployees().size());
        
        // Remove the Employee from the Department
        emp = em.find(Employee.class, employeeId);
        dep.getEmployees().remove(emp);
        
        em.getTransaction().commit();
        em.clear();
        em.getTransaction().begin();
        
        // Check that the Employee was removed
        query = em.createQuery("select dep from Department dep where dep.id = :id");
        query.setParameter("id", departmentId);
        dep = (Department) query.getSingleResult();
        assertEquals(1, dep.getEmployees().size());
      
        em.getTransaction().commit();
    }

}
