package jpaworkshop.statistics;

import jpaworkshop.model.Address;

public class EmployeeStats {

    private String name;
    private long salary;

    public EmployeeStats(String name, long salary) {
        this.name = name;
        this.salary = salary;
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
}
