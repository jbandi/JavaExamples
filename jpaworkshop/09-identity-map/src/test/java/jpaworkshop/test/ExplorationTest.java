package jpaworkshop.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;
import jpaworkshop.model.Phone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExplorationTest {

    private static Logger logger = Logger.getLogger(ExplorationTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer employeeId1;
    private Integer departmentId;

    @Before
    public void setUp() {
    	emf = Persistence.createEntityManagerFactory("jpaworkshop");
    	em = emf.createEntityManager();

    	try{
    		em.getTransaction().begin();

    		Employee emp1 = new Employee();
    		emp1.setName("John Doe");
    		emp1.setSalary(80000);
    		em.persist(emp1);
    		
    		Phone phone = new Phone();
    		phone.setPhonenumber("031 999 99 99");
    		phone.setType("Work");
    		phone.setEmployee(emp1);

    		emp1.getPhones().add(phone);

    		Phone phone2 = new Phone();
    		phone2.setPhonenumber("032 333 33 33");
    		phone2.setType("Home");
    		phone2.setEmployee(emp1);

    		emp1.getPhones().add(phone2);

    		Department department = new Department();
    		department.setName("PR");
    		em.persist(department);

    		emp1.setDepartment(department);
    		department.getEmployees().add(emp1);

    		em.getTransaction().commit();

    		employeeId1 = emp1.getId();
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
    public void explore_identity_map() throws Exception {

    	em.getTransaction().begin();
    	
        // Get the employee by navigating the object graph
        Query query1 = em.createQuery("select d from Department d where d.id = :id");
        query1.setParameter("id", departmentId);
        Department dep = (Department) query1.getSingleResult();    
        Iterator<Employee> iterator = dep.getEmployees().iterator();
        Employee john = iterator.next();
    	

    	// Get the employee by a query
        Query query2 = em.createQuery("select e from Employee e where e.id = :id");
        query2.setParameter("id", employeeId1);
        Employee emp2 = (Employee) query2.getSingleResult();

        // Get the employee by a result list
        Query query3 = em.createQuery("select e from Employee e order by e.name desc");   
        List<Employee> employees = query3.getResultList();
        Employee emp3 = employees.get(0);

        // All employees are the same instance!
        assertSame(john, emp2);
        assertSame(john, emp3);
        
        // Start a second persistence context
    	EntityManager em2 = emf.createEntityManager();
    	em2.getTransaction().begin();
    	
        Query query4 = em2.createQuery("select e from Employee e where e.id = :id");
        query4.setParameter("id", employeeId1);
        Employee emp4 = (Employee) query4.getSingleResult();   	
    	
        // Identity is only guaranteed in the same persistence context
        assertNotSame(john, emp4);
        
        em.getTransaction().commit();
        em2.getTransaction().commit();
    }
   
}
