package jpaworkshop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Department {

	@Id @GeneratedValue
	private Integer id = 0;
	
    private String name;
	
    @OneToMany //(cascade = CascadeType.ALL)
//    @JoinColumn(name="department_id")
//    @OrderBy("name DESC")
//    @OrderColumn
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
