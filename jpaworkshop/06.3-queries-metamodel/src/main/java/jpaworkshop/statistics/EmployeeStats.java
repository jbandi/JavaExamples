package jpaworkshop.statistics;

import jpaworkshop.model.Address;

public class EmployeeStats {

	private String name;
	private long salary;
	private Address address;
	
	public EmployeeStats(String name, long salary, Address address) {
		this.name = name;
		this.salary = salary;
		this.address = address;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
}
