package jpaworkshop.test;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpaworkshop.model.Department;
import jpaworkshop.model.Employee;
import jpaworkshop.model.Phone;

import org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExplorationTest {

    private static Logger logger = Logger.getLogger(ExplorationTest.class.toString());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer employeeId1;
    private Integer employeeId2;
    private Integer departmentId;

    @Before
    public void setUp() {
    	emf = Persistence.createEntityManagerFactory("jpaworkshop");
    	em = emf.createEntityManager();

    	try{
    		em.getTransaction().begin();

    		Department department = new Department();
    		department.setName("PR");
    		em.persist(department);
    		
    		Employee emp1 = new Employee();
    		emp1.setName("John Doe");
    		emp1.setSalary(80000);
    		em.persist(emp1);

    		Employee emp2 = new Employee();
    		emp2.setName("Jane Dull");
    		emp2.setSalary(90000);
    		em.persist(emp2);
    		
    		Phone phone11 = new Phone();
    		phone11.setPhonenumber("031 999 99 99");
    		phone11.setType("Work");
    		phone11.setEmployee(emp1);

    		Phone phone12 = new Phone();
    		phone12.setPhonenumber("032 333 33 33");
    		phone12.setType("Home");
    		phone12.setEmployee(emp1);

    		Phone phone21 = new Phone();
    		phone21.setPhonenumber("040 999 99 99");
    		phone21.setType("Work");
    		phone21.setEmployee(emp2);
    		
    		Phone phone22 = new Phone();
    		phone22.setPhonenumber("040 333 33 33");
    		phone22.setType("Home");
    		phone22.setEmployee(emp2);

    		emp1.setDepartment(department);
    		department.getEmployees().add(emp1);
    		
    		emp2.setDepartment(department);
    		department.getEmployees().add(emp2);
    		
    		emp1.getPhones().add(phone11);
    		emp1.getPhones().add(phone12);

    		emp2.getPhones().add(phone21);
    		emp2.getPhones().add(phone22);

    		em.getTransaction().commit();

    		departmentId = department.getId();

    		em.clear();
    		emf.getCache().evictAll();

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
    public void explore_eager_loading() throws Exception {

    	em.getTransaction().begin();

        Query query = em.createQuery("select d from Department d where d.id = :id");
//        Query query = em.createQuery("select distinct d from Department d left join fetch d.employees where d.id = :id");
        query.setParameter("id", departmentId);
        Department dep = (Department) query.getSingleResult(); 
        
		logger.info("Starting to iterate employees...");
        for (Employee emp : dep.getEmployees()) {
        	logger.info("Employee: " + emp.getName());
        	for (Phone phone : emp.getPhones()){
        		logger.info("\tPhone: " + phone.getPhonenumber());
        	}
        }
        
        em.getTransaction().commit();
    }
   
}
