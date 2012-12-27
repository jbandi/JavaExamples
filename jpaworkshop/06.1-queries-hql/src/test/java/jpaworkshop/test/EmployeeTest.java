package jpaworkshop.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
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

            Phone phone = new Phone();
            phone.setPhonenumber("031 999 99 99");
            phone.setType("Work");
            phone.setEmployee(emp);

            emp.getPhones().add(phone);

            Phone phone2 = new Phone();
            phone2.setPhonenumber("031 333 33 33");
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
            emp.getProjects().add(project);
            project.getEmployees().add(emp);

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
            Project project = em.find(Project.class, projectId);
            if (project != null) {
                em.remove(project);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Test
    public void constructorWithList() throws Exception {
        Query query = em.createQuery("SELECT NEW jpaworkshop.statistics.EmployeeStats(e.name, e.salary) FROM Employee e");
        
        List<EmployeeStats> statsList = query.getResultList();

        Assert.assertNotNull(statsList);
        Assert.assertTrue(statsList.size() > 0);
        
        for (EmployeeStats stats : statsList) {
            logger.info("Name: " + stats.getName());
        }
    }

    @Test
    public void findEmployee() throws Exception {

        //****
        logger.info("Dynamic Queries with Query-Language (JPA-QL)");
        List<Department> departments1 = em.createQuery("select d from Department d where d.employees is not empty").getResultList();
        assertEquals(1, departments1.size());

        List<Department> departments2 = em.createQuery("select d from Department d join d.employees e where e.salary = 80000 and e member of d.employees").getResultList();
        assertEquals(1, departments2.size());

        Query query1 = em.createQuery("select p from Phone p where p.employee.department.name like :dep_name");
        query1.setParameter("dep_name", "P%");
        List<Phone> phones1 = query1.getResultList();
        assertEquals(2, phones1.size());

        Query query2 = em.createQuery("select distinct d from Department d join d.employees e join e.projects p where p.name like 'A%'");
        List<Department> departments3 = query2.getResultList();
        assertEquals(1, departments3.size());
        
        List<Phone> phones2 = em.createQuery("select p from Employee e join e.phones p where e.salary = 80000 order by p.phonenumber").getResultList();
        assertEquals(2, phones2.size());
        
        //****
        logger.info("Named Queries with Query-Language (JPA-QL)");
        Query query3 = em.createNamedQuery(Employee.QUERY_FIND_BY_NAME);
        query3.setParameter("name", "John Doe");
        List<Employee> employees1 = query3.getResultList();
        assertEquals(1, employees1.size());
        
        //****
        logger.info("SQL Queries");
        Query query4 = em.createNativeQuery("select * from T_EMP where T_EMP.name like ?" , Employee.class);
        query4.setParameter(1, "J%");
        List<Employee> employees2 = query4.getResultList();
        assertEquals(1, employees2.size());      
        
        // no projects
        Query query5 =   em.createQuery("select e from Employee e where e.projects is empty");
        List<Employee> employees5 = query5.getResultList();
        assertEquals(0, employees5.size());
    }
}
