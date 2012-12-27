package jpaworkshop.model;

import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Employee {
	
	private static Logger logger = Logger.getLogger(Employee.class.toString());

    @Id
    @GeneratedValue
    protected Integer id;
    
    @Version
    protected Integer version;
	
    private String name;
    private long salary;


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

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append(" id: ");
        sb.append(this.id);
        sb.append(" name: ");
        sb.append(this.name);
        sb.append(" salary: ");
        sb.append(this.salary);
        return sb.toString();
    }
}
