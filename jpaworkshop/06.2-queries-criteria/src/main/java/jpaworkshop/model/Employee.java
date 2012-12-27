package jpaworkshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name")
@Entity
public class Employee {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private long salary;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Address address;

	@ManyToOne
	private Employee boss;

	@OneToMany(mappedBy = "boss")
	private List<Employee> directs = new ArrayList<Employee>();

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Phone> phones = new java.util.HashSet<Phone>();

	@ManyToOne
	private Department department;

	@ManyToMany(mappedBy = "employees")
	private List<Project> projects = new ArrayList<Project>();

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Employee getBoss() {
		return boss;
	}

	public void setBoss(Employee boss) {
		this.boss = boss;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Employee> getDirects() {
		return directs;
	}

	public void setDirects(List<Employee> directs) {
		this.directs = directs;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// Convenience Methods

	public void addEmployee(Employee employee) {
		employee.setBoss(this);
		this.directs.add(employee);
	}

	public void removeEmployee(Employee employee) {
		employee.setBoss(null);
		this.directs.remove(employee);
	}

	public void addPhone(Phone phone) {
		phone.setEmployee(this);
		this.phones.add(phone);
	}

	public void removePhone(Phone phone) {
		phone.setEmployee(null);
		this.phones.remove(phone);
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", salary=" + salary
				+ "]";
	}

}