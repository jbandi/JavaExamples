package jpaworkshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;

@Entity
@Inheritance
public abstract class Project {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;

	@ManyToMany
	private List<Employee> employees = new ArrayList<Employee>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	// Convenience Methods

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void addEmployee(Employee employee) {
		employee.getProjects().add(this);
		this.employees.add(employee);
	}

	public void removeEmployee(Employee employee) {
		employee.getProjects().remove(this);
		this.employees.remove(employee);
	}
}
