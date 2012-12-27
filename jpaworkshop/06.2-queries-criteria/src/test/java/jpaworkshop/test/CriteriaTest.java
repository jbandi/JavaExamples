package jpaworkshop.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import jpaworkshop.model.Address;
import jpaworkshop.model.Department;
import jpaworkshop.model.DesignProject;

import jpaworkshop.model.Employee;
import jpaworkshop.model.Phone;
import jpaworkshop.model.Project;
import org.junit.After;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CriteriaTest {

    private static Logger logger = java.util.logging.Logger.getLogger(CriteriaTest.class.getName());
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Integer employeeId;
    private Integer employeeId2;
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

            Employee emp2 = new Employee();
            emp2.setName("Jane Doe");
            emp2.setSalary(80000);
            em.persist(emp2);

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
            employeeId2 = emp2.getId();
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
            Employee emp2 = em.find(Employee.class, employeeId2);
            if (emp2 != null) {
                em.remove(emp2);
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
    public void simpleCrieriaQuery() {
        logger.info("\n>>>>>>>>>> simpleCrieriaQuery <<<<<<<<<<\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> c = cq.from(Employee.class);
        cq.select(c);

        TypedQuery<Employee> tq = em.createQuery(cq);
        List<Employee> results = tq.getResultList();

        for (Employee employee : results) {
            logger.info(employee.toString());
        }
    }

    @Test
    @Ignore
    public void metamodelQuery() {
        logger.info("\n>>>>>>>>>> metamodelQuery <<<<<<<<<<\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> c = cq.from(Employee.class);
        cq.where(cb.equal(c.get("id"), 1));
        cq.select(c);

        TypedQuery<Employee> tq = em.createQuery(cq);
        List<Employee> results = tq.getResultList();

        for (Employee employee : results) {
            logger.info(employee.toString());
        }
    }

    @Test
    public void employeePlz3() {
        logger.info("\n>>>>>>>>>> employeePlz3 <<<<<<<<<<\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> emp = cq.from(Employee.class);
        cq.where(cb.like(emp.get("address").get("zip").as(String.class), "3%"));
        cq.select(emp);

        TypedQuery<Employee> tq = em.createQuery(cq);
        List<Employee> results = tq.getResultList();

        for (Employee employee : results) {
            logger.info(employee.toString());
            logger.info("PLZ: " + employee.getAddress().getZip());
        }
    }

    @Test
    public void avgSalary() {
        logger.info("\n>>>>>>>>>> avgSalary <<<<<<<<<<\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
        Root<Employee> emp = c.from(Employee.class);
        c.multiselect(emp.get("department").get("name").as(String.class),
                cb.avg(emp.get("salary").as(Long.class))).groupBy(
                emp.get("department").get("name").as(String.class));

        Query q = em.createQuery(c);

        for (Object obj : q.getResultList()) {
            Object[] objs = (Object[]) obj;
            logger.info("Emp: " + objs[0]);
            logger.info("Salary: " + objs[1]);
        }
    }

    @Test
    public void noProject() {
        logger.info("\n>>>>>>>>>> noProject <<<<<<<<<<\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> emp = cq.from(Employee.class);
        cq.where(cb.isEmpty(emp.<List<Project>>get("projects")));
        cq.select(emp);

        TypedQuery<Employee> tq = em.createQuery(cq);
        List<Employee> results = tq.getResultList();

        for (Employee employee : results) {
            logger.info(employee.toString());
            logger.info("# of projects: " + employee.getProjects().size());
        }
    }

    @Test
    public void join() {
        logger.info("\n>>>>>>>>>> join <<<<<<<<<<\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> emp = cq.from(Employee.class);
        Join<Employee, Project> projects = emp.join("projects");
        cq.where(cb.like(projects.get("name").as(String.class), "A%"));
        cq.select(emp);

        TypedQuery<Employee> tq = em.createQuery(cq);
        List<Employee> results = tq.getResultList();

        for (Employee employee : results) {
            logger.info(employee.toString());
            for (Project project : employee.getProjects()) {
                logger.info(project.getName());
            }
        }
    }
}
