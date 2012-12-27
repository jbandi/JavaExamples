package jpaworkshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity(name = "Employee")
public class Employee extends BaseEntity {

    private String name;
    private long salary;

    @ManyToMany(mappedBy = "employees")
    private List<Project> projects = new ArrayList<Project>();

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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
