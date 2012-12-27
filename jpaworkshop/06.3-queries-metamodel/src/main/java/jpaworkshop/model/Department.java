package jpaworkshop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Department {

	@Id
	@GeneratedValue
	private Integer id;
	private String name2;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<Employee> employees = new HashSet<Employee>();

	public String getName() {
		return name2;
	}

	public void setName(String name) {
		this.name2 = name;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	// Convenience Methods

	public void addEmployee(Employee employee) {
		employee.setDepartment(this);
		this.employees.add(employee);
	}

	public void removeEmployee(Employee employee) {
		employee.setDepartment(null);
		this.employees.remove(employee);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
