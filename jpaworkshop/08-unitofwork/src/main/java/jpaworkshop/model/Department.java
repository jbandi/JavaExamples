package jpaworkshop.model;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class Department {
	
	private static Logger logger = Logger.getLogger(Department.class.toString());

    @Id
    @GeneratedValue
    protected Integer id;
    
    @Version
    protected Integer version;
	
    private String name;

    @OneToMany
    @JoinColumn(name="department_id")
    private Set<Employee> employees = new HashSet<Employee>();
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
